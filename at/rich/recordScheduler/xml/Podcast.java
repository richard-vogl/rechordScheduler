package at.rich.recordScheduler.xml;


public class Podcast {
	private String title;
	private String description;
	private  String image_url;
	private  String category;
	private  String date;
	private  String keywords;
	private  String size;
	private  String length;
	private  String mp3_url;
	private  String summary;
	private  String subtitle;
	private  String author;
	private String mp3_name;
	public Podcast(){
		
	}
	public Podcast(String b_title, String b_description, String b_image_url,
		    String b_category, String b_date, String b_keywords, String b_size, String b_length, String b_mp3_url, String b_mp3_name, String b_summary,
		    String b_subtitle, String b_author){
		
		this.title = b_title;
		this.description = b_description;
		this.image_url = b_image_url;
		this.category = b_category;
		this.date = b_date;
		this.keywords = b_keywords;
		this.size = b_size;
		this.length = b_length;
		this.mp3_url = b_mp3_url;
		this.summary = b_summary;
		this.subtitle = b_subtitle;
		this.author = b_author;
		this.mp3_name = b_mp3_name;
		
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getMp3_url() {
		return mp3_url;
	}
	public void setMp3_url(String mp3_url) {
		this.mp3_url = mp3_url;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setMp3_Name(String mp3name) {
		this.mp3_name = mp3name;		
	}
	public String getMp3_Name(){
		return this.mp3_name;
	}
}
