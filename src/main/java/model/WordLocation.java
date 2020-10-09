package model;

public class WordLocation {
	private long wordId;
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


	public WordLocation(String wrd, int index, int line, int indexInLine, int sentence, int paragraph) {
		this.word = wrd;
		this.index = index;
		this.line = line;
		this.indexInLine = indexInLine;
		this.sentence = sentence;
		this.paragraph = paragraph;

		if ( '\"' == (word.charAt(0)) ){
			isQuoteBefore = true;
			word = word.substring(1);
		} else
			isQuoteBefore = false;

		findPunctuation();

		if ( '\"' == (word.charAt(word.length()-1)) ){
			isQuoteAfter = true;
			word = word.substring(0, word.length()-2);
		} else
			isQuoteAfter = false;

		findPunctuation();

	}

	public void findPunctuation(){
		String punctuations = ".,:;?";

		if ( punctuations.contains(String.valueOf(word.charAt(word.length()-1)) ) ){
			punctuationMark = String.valueOf(word.charAt(word.length()-1));
			word = word.substring(0, word.length()-1);
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
		this.punctuationMark = punctuationMark;
	}
}
