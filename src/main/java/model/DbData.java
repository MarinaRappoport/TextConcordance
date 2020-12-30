package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import service.BookService;
import service.GroupService;
import service.WordService;

import java.util.List;

public class DbData {
	@JacksonXmlElementWrapper(localName = "books")
	@JacksonXmlProperty(localName = "book")
	private List<Book> books;

	@JacksonXmlElementWrapper(localName = "words")
	@JacksonXmlProperty(localName = "word")
	private List<Word> words;

	@JacksonXmlElementWrapper(localName = "word_locations")
	@JacksonXmlProperty(localName = "word_location")
	private List<WordLocation> wordLocations;

	@JacksonXmlElementWrapper(localName = "groups")
	@JacksonXmlProperty(localName = "group")
	private List<Group> groups;

	private List<Integer> phrases;

	public DbData() {
	}

	public void initFromDB() {
		books = BookService.getAllBooks();
		words = WordService.getAllWords();
		wordLocations = WordService.getAllWordLocations();
		groups = GroupService.getAllGroups();
	}

	public DbData(List<Book> books) {
		this.books = books;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public List<WordLocation> getWordLocations() {
		return wordLocations;
	}

	public void setWordLocations(List<WordLocation> wordLocations) {
		this.wordLocations = wordLocations;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Integer> getPhrases() {
		return phrases;
	}

	public void setPhrases(List<Integer> phrases) {
		this.phrases = phrases;
	}
}
