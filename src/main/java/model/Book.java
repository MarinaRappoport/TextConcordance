package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents table 'book'
 */
public class Book {
	@JacksonXmlProperty(isAttribute = true)
	private int id;
	private String title;
	@JsonIgnore
	private String date;
	private String author, path;
	private String translator;
	private Date releaseDate;
	public int wordCount, lineCount, sentenceCount, paragraphCount, characterCount;
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

	// constructors
	public Book(String title, String author, String translator, Date releaseDate) {
		this.title = title;
		this.author = author;
		this.translator = translator;
		this.releaseDate = releaseDate;
		if (releaseDate != null)
			date = DATE_FORMAT.format(releaseDate);
	}

	public Book(String path) {
		this.path = path;
	}

	//empty constructor for XML parsing
	public Book() {
	}

	//getters & setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDate(String date) {
		this.date = date;
		if (date != null && !date.isEmpty()) {
			try {
				this.releaseDate = DATE_FORMAT.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTranslator(String translator) {
		if (translator.equals("")) translator = null;
		else this.translator = translator;
	}

	public String getDate() {
		return date;
	}

	public String getAuthor() {
		return author;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTranslator() {
		return translator;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public int getParagraphCount() {
		return paragraphCount;
	}
}
