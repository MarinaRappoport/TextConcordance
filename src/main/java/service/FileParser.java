package service;

import model.Book;
import model.WordLocation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

	private final static String TITLE = "Title:";
	private final static String AUTHOR = "Author:";
	private final static String RELEASE_DATE = "Release Date:";
	private final static String TRANSLATOR = "Translator:";
	private final static String START_OF_BOOK = "*** START OF THIS PROJECT GUTENBERG EBOOK";
	private final static String END_OF_BOOK = "End of the Project Gutenberg EBook";
	private static final String EMPTY = "";

	//Parse file to words
	//Counting words, lines, sentences and paragraph
	public List<WordLocation> parseFile(Book book) {
		//init
		book.paragraphCount = 1;
		book.sentenceCount = 1;

		List<WordLocation> wordLocationList = new LinkedList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(book.getPath())));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error with buffer reader of file: " + book.getPath());
			return null;
		}
		String currentLine;
		String prevLine = "";
		boolean isBookStarted = false;
		boolean isParagraphOver = false;

		// Reading line by line from the
		// file until a null is returned
		try {
			while ((currentLine = reader.readLine()) != null) {
				if (isBookStarted) {
					if (currentLine.startsWith(END_OF_BOOK)) break;
					if (currentLine.equals("")) {
						if (isParagraphOver) {
							book.paragraphCount++;
							isParagraphOver = false;
						}
					} else {
						isParagraphOver = true;
						book.lineCount++;
						book.characterCount += currentLine.length();

						String[] wordList = currentLine.split("\\s+");

						// Add word location to the list
						wordLocationList.addAll(addWordLocationToList(book, wordList));

						// [!?.:]+ is the sentence delimiter in java
						String[] sentenceList = currentLine.split("[!?.:]+");

						// if there is continue of previous line's sentence in current line
						if (!prevLine.endsWith(".") && !prevLine.equals(""))
							book.sentenceCount += (sentenceList.length - 1);
						else
							book.sentenceCount += sentenceList.length;

						prevLine = currentLine;
					}
				} else {
					if (currentLine.startsWith(TITLE)) book.setTitle(currentLine.replace(TITLE, EMPTY).trim());
					else if (currentLine.startsWith(AUTHOR)) book.setAuthor(currentLine.replace(AUTHOR, EMPTY).trim());
					else if (currentLine.startsWith(RELEASE_DATE)) {
						try {
							book.setDate(currentLine.replace(RELEASE_DATE, EMPTY).split("\\[")[0].trim());
						} catch (Exception e) {
							System.out.println("failed to parse release date");
						}
					} else if (currentLine.startsWith(TRANSLATOR))
						book.setTranslator(currentLine.replace(TRANSLATOR, EMPTY).trim());
					else if (currentLine.startsWith(START_OF_BOOK)) isBookStarted = true;
				}
			}
		} catch (IOException e) {
			return null;
		}

		//TEST
		System.out.println("Total word count = " + book.wordCount + "\n");
		System.out.println("Total number of sentences = " + book.sentenceCount + "\n");
		System.out.println("Total number of characters = " + book.characterCount + "\n");
		System.out.println("Total number of lines = " + book.lineCount + "\n");
		System.out.println("Number of paragraphs = " + book.paragraphCount + "\n");

		return wordLocationList;
	}

	public List<WordLocation> addWordLocationToList(Book book, String[] wordList) {
		List<WordLocation> wordLocationList = new LinkedList<>();
		for (int i = 0; i < wordList.length; i++) {
			if (!wordList[i].isEmpty()) {
				String word = null;
				boolean isQuoteBefore = false;
				boolean isQuoteAfter = false;
				String punctuationMark = null;
				try {
					Matcher matcher = Pattern.compile("(\\W*)?([0-9a-zA-Z'’-]+)(\\W*)").matcher(wordList[i].replaceAll("_", ""));
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
				} catch (Exception e) {
				}
				if (word != null) {
					WordLocation current = new WordLocation(word, ++book.wordCount,
							book.lineCount, i + 1, book.sentenceCount, book.paragraphCount);
					current.setQuoteBefore(isQuoteBefore);
					current.setQuoteAfter(isQuoteAfter);
					current.setPunctuationMark(punctuationMark);
					wordLocationList.add(current);
				}
			}
		}
		return wordLocationList;
	}
}
