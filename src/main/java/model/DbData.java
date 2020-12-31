package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import service.BookService;
import service.GroupService;
import service.PhraseService;
import service.WordService;

import java.util.List;

public class DbData {
	@JacksonXmlElementWrapper(localName = "books")
	@JacksonXmlProperty(localName = "book")
	private List<Book> books;

	@JacksonXmlElementWrapper(localName = "words")
	@JacksonXmlProperty(localName = "word")
	private List<Word> words;

	@JacksonXmlElementWrapper(localName = "word_in_book_list")
	@JacksonXmlProperty(localName = "word_in_book")
	private List<WordLocation> wordLocations;

	@JacksonXmlElementWrapper(localName = "groups")
	@JacksonXmlProperty(localName = "group")
	private List<Group> groups;

	@JacksonXmlElementWrapper(localName = "word_in_group_list")
	@JacksonXmlProperty(localName = "word_in_group")
	private List<WordInGroup> wordInGroupList;

	@JacksonXmlElementWrapper(localName = "phrases")
	@JacksonXmlProperty(localName = "phrase_id")
	private List<Integer> phrases;

	@JacksonXmlElementWrapper(localName = "word_in_phrase_list")
	@JacksonXmlProperty(localName = "word_in_phrase")
	private List<WordInPhrase> wordInPhraseList;

	public DbData() {
	}

	public void initFromDB() {
		books = BookService.getAllBooks();
		words = WordService.getAllWords();
		wordLocations = WordService.getAllWordLocations();
		groups = GroupService.getAllGroups();
		wordInGroupList = GroupService.getAllWordInGroups();
		phrases = PhraseService.getAllPhraseIds();
		wordInPhraseList = PhraseService.getAllWordsInPhrases();
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

	public List<WordInGroup> getWordInGroupList() {
		return wordInGroupList;
	}

	public void setWordInGroupList(List<WordInGroup> wordInGroupList) {
		this.wordInGroupList = wordInGroupList;
	}

	public List<WordInPhrase> getWordInPhraseList() {
		return wordInPhraseList;
	}

	public void setWordInPhraseList(List<WordInPhrase> wordInPhraseList) {
		this.wordInPhraseList = wordInPhraseList;
	}
}
