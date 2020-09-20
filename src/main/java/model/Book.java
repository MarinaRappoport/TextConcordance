package model;

import java.util.Date;

public class Book {
	private long id;
	private String title;
	private String author;
	private String translator;
	private Date releaseDate;

	public Book(String title, String author, String translator, Date releaseDate) {
		this.title = title;
		this.author = author;
		this.translator = translator;
		this.releaseDate = releaseDate;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getTranslator() {
		return translator;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setId(long id) {
		this.id = id;
	}
}
