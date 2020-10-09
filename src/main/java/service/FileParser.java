package service;

import model.Book;
import model.WordLocation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class FileParser {

	private final static String TITLE = "Title:";
	private final static String AUTHOR = "Author:";
	private final static String RELEASE_DATE = "Release Date:";
	private final static String TRANSLATOR = "Translator:";
	private final static String START_OF_BOOK = "*** START OF THIS PROJECT GUTENBERG EBOOK";
	private final static String END_OF_BOOK = "End of the Project Gutenberg EBook";
	public static final String EMPTY = "";

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
			System.exit(1);
		}
		String currentLine;
		String prevLine = "";
		boolean isBookStarted = false;

		// Reading line by line from the
		// file until a null is returned
		try {
			while ((currentLine = reader.readLine()) != null) {
				if (isBookStarted) {
					if (currentLine.startsWith(END_OF_BOOK)) break;
					book.lineCount++;
					if (currentLine.equals("")) {
						book.paragraphCount++;
					} else {
						book.characterCount += currentLine.length();

						// \\s+ is the space delimiter in java
						String[] wordList = currentLine.split("\\s+");

						// Add words to data base
						for (int i = 0; i < wordList.length; i++) {
							if ( !wordList[i].isEmpty() ) {
								WordLocation current = new WordLocation(wordList[i], ++book.wordCount,
										book.lineCount, i + 1, book.sentenceCount, book.paragraphCount);
								wordLocationList.add(current);
							}
						}

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
			e.printStackTrace();
			System.exit(1);
		}

		//TEST
		System.out.println("Total word count = " + book.wordCount + "\n");
		System.out.println("Total number of sentences = " + book.sentenceCount + "\n");
		System.out.println("Total number of characters = " + book.characterCount + "\n");
		System.out.println("Total number of lines = " + book.lineCount + "\n");
		System.out.println("Number of paragraphs = " + book.paragraphCount + "\n");

		return wordLocationList;
	}
}
