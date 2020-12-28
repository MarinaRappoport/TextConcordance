package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents table 'book'
 */
public class Book {
	private long id;
	private String title, date;
	private String author, path;
	private String translator;
	private Date releaseDate;
	public int wordCount, lineCount, sentenceCount, paragraphCount, characterCount;
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public int getParagraphCount() { return paragraphCount; }
}
