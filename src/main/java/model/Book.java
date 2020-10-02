package model;

import java.util.Date;
import java.util.LinkedList;

public class Book {
	private long id;
	private String title, date;
	private String author, path;
	private String translator;
	private Date releaseDate;
	public int countWord, lineCount, sentenceCount, paragraphCount, characterCount;

	//This is too many constructors - We'll deal with this later.

	public Book(String title, String author, String translator, String releaseDate, String path) {
		this.title = title;
		this.author = author;
		this.translator = translator;
		this.date = releaseDate; //TODO string date to Date
		this.path = path;
	}

	public Book(String title, String author, String translator, String releaseDate) {
		this.title = title;
		this.author = author;
		this.translator = translator;
		this.date = releaseDate;
	}

	public Book(String title, String author, String translator, Date releaseDate) {
		this.title = title;
		this.author = author;
		this.translator = translator;
		this.releaseDate = releaseDate;
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

	public String getDate() {
		return date;
	}

	public String getAuthor() {
		return author;
	}

	public String getPath() {
		return path;
	}

	public String getTranslator() {
		return translator;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}
}
