package service;

import gui.MainMenu;
import model.Book;
import model.WordLocation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Singleton class for handling books
 */
public class FilesManager {
	private static ArrayList<Book> books;
	private static Map<String, Integer> words;
	private static MainMenu menu = null;
	private static FilesManager single_instance = null;

	//static method to create a single instance of FileManager class
	public static FilesManager getInstance() {
		if (single_instance == null)
			single_instance = new FilesManager();

		return single_instance;
	}

	//Allow to set the main menu only once
	public static void setMainMenu(MainMenu mainMenu) {
		if (menu == null)
			menu = mainMenu;
	}

	//private constructor
	private FilesManager() {
		init();
	}

	/**
	 * Init books and words from DB
	 */
	public static void init() {
		books = BookService.getAllBooks();
		words = WordService.getAllWordsId();
	}

	/**
	 * Add only new file. We cannot add the same file twice.
	 */
	public void addFile(Book book, List<WordLocation> wordLocationList) {
		int id = BookService.insertBook(book);
		if (id > 0) {
			for (WordLocation wordLocation : wordLocationList) {
				Integer wordId = words.get(wordLocation.getWord());
				if (wordId == null)
					wordId = WordService.insertWord(wordLocation.getWord());
				if (wordId > 0) {
					wordLocation.setWordId(wordId);
					words.put(wordLocation.getWord(), wordId);
				}
			}
			WordService.addWordLocationList(wordLocationList, id);
			books.add(book);
			menu.updateFileDetails();
		} else {
			JOptionPane.showMessageDialog(null, "Book already exists", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Get book file by title
	 *
	 * @return null if file not found
	 */
	public static Book getFile(String title) {
		Book file;

		Iterator<Book> iter = books.iterator();
		while (iter.hasNext()) {
			Book current = iter.next();
			if (current.getTitle().equals(title)) {
				file = current;
				return file;
			}
		}

		//if file name not found
		return null;
	}

	/**
	 * Get book file by id
	 * @return null if file not found
	 */
	public static Book getFile(long id) {
		Book file;

		Iterator<Book> iter = books.iterator();
		while (iter.hasNext()) {
			Book current = iter.next();
			if (current.getId() == id) {
				file = current;
				return file;
			}
		}

		//if file name not found
		return null;
	}


	/**
	 * found word id by its value
	 */
	public Integer getWordId(String word) {
		return words.get(word);
	}

	/**
	 * @return list of all books
	 */
	public ArrayList<Book> getBooks() {
		return books;
	}
}
