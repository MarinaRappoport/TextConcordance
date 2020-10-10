package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordLocation {
	private long wordId;
	private String word;
	private long bookId;
	private int index;
	private int line;
	private int indexInLine;
	private int sentence;
	private int paragraph;
	private boolean isQuoteBefore;
	private boolean isQuoteAfter;
	private String punctuationMark;


	public WordLocation(String wrd, int index, int line, int indexInLine, int sentence, int paragraph) {
		this.word = wrd;
		this.index = index;
		this.line = line;
		this.indexInLine = indexInLine;
		this.sentence = sentence;
		this.paragraph = paragraph;


		Matcher matcher = Pattern.compile("(\\W*)?(\\w+)(\\W*)").matcher(word);
		if (matcher.find() && matcher.groupCount() == 3) {
			if (!matcher.group(1).isEmpty())
				isQuoteBefore = "\"“\'".contains(matcher.group(1));
			word = matcher.group(2);
			String after = matcher.group(3);
			if (!after.isEmpty()) {
				isQuoteAfter = "\"”\'".contains(String.valueOf(after.charAt(0)));
				if (isQuoteAfter) after = after.substring(1);
				if (!after.isEmpty()) punctuationMark = after;
			}
		}
	}

	public String getWord() {
		return word;
	}

	public void setWordId(long wordId) {
		this.wordId = wordId;
	}

	public long getWordId() {
		return wordId;
	}

	public long getBookId() {
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
		this.punctuationMark = punctuationMark;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
}
