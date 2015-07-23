package at.rich.recordScheduler.recorder;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.tritonus.share.sampled.AudioFileTypes;
import org.tritonus.share.sampled.Encodings;

import at.rich.recordScheduler.scheduler.RecorderConfig;

/**
 * @author richard vogl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LineAudioRecorder extends Thread{
	
	private TargetDataLine			m_line;
	private AudioFileFormat.Type	m_targetFileType;
	private AudioFormat	    		m_sourceType;
	private AudioInputStream		m_audioInputStream;
	private File					m_outputFile;
	
	// currently, there is no convenient method in Java Sound to specify non-standard Encodings.
	// using tritonus' proposal to overcome this.
	private static final AudioFormat.Encoding	MPEG1L3 = Encodings.getEncoding("MPEG1L3");
	private static final AudioFileFormat.Type	MP3 = AudioFileTypes.getType("MP3", "mp3");

	private boolean isRecording = false;
	
	private boolean vbr = false;
	private int quality;
	private int bitrate;
	
	public LineAudioRecorder(File file, RecorderConfig recorderConfig)
	{
		super("Recorder for: "+file.getName());
		
		setConfig(recorderConfig);

		/* input audio format for line encoding
		 */
		m_sourceType = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				44100.0F, 16, 2, 4, 44100.0F, false);
		
		/* Now, we are trying to get a TargetDataLine. The
		 TargetDataLine is used later to read audio data from it.
		 If requesting the line was successful, we are opening
		 it (important!).
		 */
		
		DataLine.Info	info = new DataLine.Info(TargetDataLine.class, m_sourceType);
		m_line = null;
		try
		{
			m_line = (TargetDataLine) AudioSystem.getLine(info);
			m_line.open(m_sourceType);
		}
		catch (LineUnavailableException e)
		{
			System.out.println("unable to get a recording line");
			e.printStackTrace();
			System.exit(1);
		}
		
		m_targetFileType = MP3;	

		m_audioInputStream = new AudioInputStream(m_line);
		
		setOptions();
		
		try {
			m_audioInputStream = getConvertedStream(m_audioInputStream, MPEG1L3);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		m_outputFile = file;
	}
	
	/**
	 * 
	 */
	private void setOptions() {
		//org.tritonus.share.TDebug.TraceAudioConverter=true;
		System.setProperty("tritonus.lame.vbr", (vbr)?"true":"false");
		System.setProperty("tritonus.lame.quality", RecorderConfig.getQualityString(quality)); //One of: lowest, low, middle, high, highest
		System.setProperty("tritonus.lame.bitrate", String.valueOf(bitrate)); // One of: 32 40 48 56 64 80 96 112 128 160 192 224 256 320 (MPEG1)
			            										//Or: 8 16 24 32 40 48 56 64 80 96 112 128 144 160 (MPEG2 and MPEG2.5)
	}
	
	public static AudioInputStream getConvertedStream(
	    	AudioInputStream sourceStream,
	    	AudioFormat.Encoding targetEncoding)
			throws Exception {
		AudioFormat sourceFormat = sourceStream.getFormat();

		// construct a converted stream
		AudioInputStream targetStream  = AudioSystem.getAudioInputStream(targetEncoding, sourceStream);
		if (targetStream == null) {
			throw new Exception("conversion not supported");
		}
		return targetStream;
	}

	
	/** Starts the recording.
	 To accomplish this, (i) the line is started and (ii) the
	 thread is started.
	 */
	public void startRecording()
	{
		/* Starting the TargetDataLine. It tells the line that
		 we now want to read data from it. If this method
		 isn't called, we won't
		 be able to read data from the line at all.
		 */
		m_line.start();
		
		/* Starting the thread. This call results in the
		 method 'run()' (see below) being called. There, the
		 data is actually read from the line.
		 */
		this.isRecording = true;
		start();
	}
	/** Stops the recording.
	 
	 Note that stopping the thread explicitely is not necessary. Once
	 no more data can be read from the TargetDataLine, no more data
	 be read from our AudioInputStream. And if there is no more
	 data from the AudioInputStream, the method 'AudioSystem.write()'
	 (called in 'run()' returns. Returning from 'AudioSystem.write()'
	 is followed by returning from 'run()', and thus, the thread
	 is terminated automatically.
	 
	 It's not a good idea to call this method just 'stop()'
	 because stop() is a (deprecated) method of the class 'Thread'.
	 And we don't want to override this method.
	 */
	
	public void stopRecording()
	{
		m_line.stop();
		m_line.close();
	}
	
	/** Main working method.
	 You may be surprised that here, just 'AudioSystem.write()' is
	 called. But internally, it works like this: AudioSystem.write()
	 contains a loop that is trying to read from the passed
	 AudioInputStream. Since we have a special AudioInputStream
	 that gets its data from a TargetDataLine, reading from the
	 AudioInputStream leads to reading from the TargetDataLine. The
	 data read this way is then written to the passed File. Before
	 writing of audio data starts, a header is written according
	 to the desired audio file type. Reading continues untill no
	 more data can be read from the AudioInputStream. In our case,
	 this happens if no more data can be read from the TargetDataLine.
	 This, in turn, happens if the TargetDataLine is stopped or closed
	 (which implies stopping). (Also see the comment above.) Then,
	 the file is closed and 'AudioSystem.write()' returns.
	 */
	public void run()
	{
		try
		{
			AudioSystem.write(
					m_audioInputStream,
					m_targetFileType,
					m_outputFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		isRecording=false;
	}
	
	
	
	/**
	 * @return Returns true if the recorder is currently recording something.
	 */
	public boolean isRecording() {
		return isRecording;
	}

	public RecorderConfig getConfig(){
		return new RecorderConfig(vbr, quality, bitrate);
	}
	
	public void setConfig(RecorderConfig rc){
		this.vbr = rc.vbr;
		this.quality = rc.quality;
		this.bitrate = rc.bitrate;
	}
	/**
	 * main method to test functionality
	 * @param args
	 */
	public static void main(String args[]){
		File file = new File("out.mp3");
		LineAudioRecorder recorder = new LineAudioRecorder(file, new RecorderConfig(true, RecorderConfig.Q_HIGHEST, 192));	
		recorder.startRecording();
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		recorder.stopRecording();
	}
}
