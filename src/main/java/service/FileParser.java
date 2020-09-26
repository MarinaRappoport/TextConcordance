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

		// Reading line by line from the
		// file until a null is returned
		try {
			while ((currentLine = reader.readLine()) != null) {
				book.lineCount++;
				if (currentLine.equals("")) {
					book.paragraphCount++;
				} else {
					book.characterCount += currentLine.length();

					// \\s+ is the space delimiter in java
					String[] wordList = currentLine.split("\\s+");

					// Add words to data base
					for (int i = 0; i < wordList.length; i++) {
						WordLocation current = new WordLocation(wordList[i], 1, ++book.countWord,
								book.lineCount, i + 1, book.sentenceCount, book.paragraphCount);
						wordLocationList.add(current);
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
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}


		//TEST
		System.out.println("Total word count = " + book.countWord + "\n");
		System.out.println("Total number of sentences = " + book.sentenceCount + "\n");
		System.out.println("Total number of characters = " + book.characterCount + "\n");
		System.out.println("Total number of lines = " + book.lineCount + "\n");
		System.out.println("Number of paragraphs = " + book.paragraphCount + "\n");

		return wordLocationList;
	}
}
