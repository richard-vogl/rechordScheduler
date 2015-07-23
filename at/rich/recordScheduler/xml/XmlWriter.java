package at.rich.recordScheduler.xml;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLEncoder;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlWriter {
	static Document document;
	public static void main(String[] argv){
		 if (argv.length != 1) {
		        
		    }
		 addBroadcast("Funkaufklaerer.xml", null, "Title", "Description", "http://url-to-image.jpg","Radio Broadcast","29.01.2007","Campusradio, other keywords",
				 "2:00:00", "102340", "http://radio.oeh.jku.at/sendungen/", "te s t.mp3", "Summary", "Subtitle", "Author" );
	}
	
	
	public static void addBroadcast(String filename, PrintStream outstream, Podcast pcast){
		addBroadcast(filename,outstream, pcast.getTitle(), pcast.getDescription(), pcast.getImage_url(),
				pcast.getCategory(), pcast.getDate(), pcast.getKeywords(), pcast.getSize(), pcast.getLength(), pcast.getMp3_url(), pcast.getMp3_Name(), pcast.getSummary(),
				pcast.getSubtitle(), pcast.getAuthor())	;	
	}
	public static void addBroadcast(String filename, PrintStream outstream, String b_title, String b_description, String b_image_url,
		    String b_category, String b_date, String b_keywords, String b_size, String b_length, String b_mp3_url, String b_mp3_name, String b_summary,
		    String b_subtitle, String b_author) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    try {
		    	   DocumentBuilder builder = factory.newDocumentBuilder();
		    	   File file = new File(filename);
		    	   document = builder.parse(file);

				    OutputFormat of = new OutputFormat("XML","UTF-8",true);
				    of.setIndent(1);
				    of.setIndenting(true);
				    if (document.getDoctype()!=null){
				    	of.setDoctype(null,document.getDoctype().toString());
				    }
				    
				    Element item = document.createElementNS(null,"item"),
				    		title = document.createElementNS(null, "title"),
				    		description = document.createElementNS(null, "description"),
				    		iImage = document.createElementNS("itunes", "itunes:image"),
				    		iAuthor = document.createElementNS("itunes", "itunes:author"),
				    		iSubtitle = document.createElementNS("itunes", "itunes:subtitle"),
				    		iSummary = document.createElementNS("itunes", "itunes:summary"),
				    		enclosure = document.createElementNS(null, "enclosure"),
				    		guid = document.createElementNS(null, "guid"),
				    		iDuration = document.createElementNS("itunes", "itunes:duration"),
				    		iKeywords = document.createElementNS("itunes", "itunes:keywords"),
				    		iCategory = document.createElementNS("itunes", "itunes:category"),
				    		pubDate = document.createElementNS("", "pubDate");
				    
				    
					Node n = document.createTextNode(b_title);
				    title.appendChild(n);
				    item.appendChild(title);
				    
				    n = document.createTextNode(b_description);
				    description.appendChild(n);
				    item.appendChild(description);
				    
				    n = document.createTextNode(b_image_url);
				    iImage.appendChild(n);
				    item.appendChild(iImage);

					n = document.createTextNode(b_author);
				    iAuthor.appendChild(n);
				    item.appendChild(iAuthor);
				    
					n = document.createTextNode(b_subtitle);
				    iSubtitle.appendChild(n);
				    item.appendChild(iSubtitle);

					n = document.createTextNode(b_summary);
				    iSummary.appendChild(n);
				    item.appendChild(iSummary);
				    
		    		enclosure.setAttribute("length", b_size);
		    		enclosure.setAttribute("type", "audio/mpeg");
		    		enclosure.setAttribute("url", b_mp3_url+b_mp3_name);
				    item.appendChild(enclosure);
		    		
				    n = document.createTextNode(b_mp3_url+b_mp3_name);
				    guid.appendChild(n);
				    guid.setAttribute("isPermaLink", "true");
				    item.appendChild(guid);

					n = document.createTextNode(b_length);
				    iDuration.appendChild(n);
				    item.appendChild(iDuration);
		    		
					n = document.createTextNode(b_keywords);
				    iKeywords.appendChild(n);
				    item.appendChild(iKeywords);
		    		
				    iCategory.setAttribute("text", b_category);
				    item.appendChild(iCategory);
		    		
					n = document.createTextNode(b_date);
		    		pubDate.appendChild(n);
				    item.appendChild(pubDate);
				    
				    NodeList l = document.getElementsByTagName("channel");
				    Node channel = l.item(0);
				    
				    //System.out.println(channel);//.getFirstChild()); //.appendChild(e);
				    channel.appendChild(item);
				    if (outstream == null){
				    	outstream = new PrintStream(new File(filename));
				    }
				    XMLSerializer serializer = new XMLSerializer(outstream,of);
//				     As a DOM Serializer
				    serializer.asDOMSerializer();
				    serializer.serialize( document );
				    outstream.close();
		    } catch (SAXException sxe) {
		       // Error generated during parsing
		       Exception  x = sxe;
		       if (sxe.getException() != null)
		           x = sxe.getException();
		       x.printStackTrace();

		    } catch (ParserConfigurationException pce) {
		       // Parser with specified options can't be built
		       pce.printStackTrace();

		    } catch (IOException ioe) {
		       // I/O error
		       ioe.printStackTrace();
		    }
	}
}
