package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents table 'word_in_book'
 */
public class WordLocation {
	private int wordId;
	@JsonIgnore
	private String word;
	private int bookId;
	private int index;
	private int line;
	private int indexInLine;
	private int sentence;
	private int paragraph;
	private boolean isQuoteBefore;
	private boolean isQuoteAfter;
	private String punctuationMark;

	/**
	 * Constructor using string value of word
	 */
	public WordLocation(String wrd, int index, int line, int indexInLine, int sentence, int paragraph) {
		this.word = wrd;
		this.index = index;
		this.line = line;
		this.indexInLine = indexInLine;
		this.sentence = sentence;
		this.paragraph = paragraph;
	}

	/**
	 * Constructor using word id (for parsing from DB row)
	 */
	public WordLocation(int wordId, int index, int line, int indexInLine, int sentence, int paragraph) {
		this.wordId = wordId;
		this.index = index;
		this.line = line;
		this.indexInLine = indexInLine;
		this.sentence = sentence;
		this.paragraph = paragraph;
	}

	//empty constructor for XML parsing
	public WordLocation() {
	}

	//getters & setters
	@JsonIgnore
	public String getWord() {
		return word;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public int getWordId() {
		return wordId;
	}

	public int getBookId() {
		return bookId;
	}

	public int getIndex() {
		return index;
	}

	public int getSentence() {
		return sentence;
	}

	public int getLine() {
		return line;
	}

	public int getIndexInLine() {
		return indexInLine;
	}

	public int getParagraph() {
		return paragraph;
	}

	public boolean isQuoteBefore() {
		return isQuoteBefore;
	}

	public void setQuoteBefore(boolean quoteBefore) {
		isQuoteBefore = quoteBefore;
	}

	public boolean isQuoteAfter() {
		return isQuoteAfter;
	}

	public void setQuoteAfter(boolean quoteAfter) {
		isQuoteAfter = quoteAfter;
	}

	public String getPunctuationMark() {
		return punctuationMark;
	}

	public void setPunctuationMark(String punctuationMark) {
		if (punctuationMark != null && !punctuationMark.isEmpty())
			this.punctuationMark = punctuationMark;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
}
