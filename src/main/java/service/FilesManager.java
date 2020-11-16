package service;

import gui.MainMenu;
import model.Book;
import model.WordLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//Singleton class for handling files
public class FilesManager {
	private static ArrayList<Book> files;
	private static Map<String, Long> words;
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
		files = BookService.getAllBooks();
		words = WordService.getAllWordsId();
	}

	//Add only new file. We cannot add the same file twice.
	public void addFile(Book book, List<WordLocation> wordLocationList) {
		if (!files.contains(book)) {
			//TODO check if book already in DB - alert
			long id = BookService.insertBook(book);
			for (WordLocation wordLocation : wordLocationList) {
				Long wordId = words.get(wordLocation.getWord());
				if (wordId == null)
					wordId = WordService.insertWord(wordLocation.getWord());
				if (wordId > 0) {
					wordLocation.setWordId(wordId);
					words.put(wordLocation.getWord(), wordId);
				}
			}
			WordService.addWordLocationList(wordLocationList, id);
			files.add(book);
		}
		menu.updateFileDetails();
	}

	public boolean isEmpty() {
		return files.isEmpty();
	}

	public Book getFile(String name) {
		Book file;

		Iterator<Book> iter = files.iterator();
		while (iter.hasNext()) {
			Book current = iter.next();
			if (current.getTitle().equals(name)) {
				file = current;
				return file;
			}
		}

		//if file name not found
		return null;

	}

	public Long getWordId(String word) {
		return words.get(word);
	}

	public ArrayList<Book> getFiles() {
		return files;
	}
}
