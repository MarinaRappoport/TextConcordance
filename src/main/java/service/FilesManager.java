package service;

import gui.MainMenu;
import model.Book;
import model.WordLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Singleton class for handling files
public class FilesManager {
    private static ArrayList<Book> files;
    private static MainMenu menu = null;
    private static FilesManager single_instance = null;

    //static method to create a single instance of FileManager class
    public static FilesManager getInstance() {
        if (single_instance == null)
            single_instance = new FilesManager();

        return single_instance;
    }

    //Allow to set the main menu only once
    public static void setMainMenu(MainMenu mainMenu){
        if (menu == null)
            menu = mainMenu;
    }

    //private constructor
    private FilesManager() {
        files = new ArrayList<Book>();
    }

    //Add only new file. We cannot add the same file twice.
    public void addFile(Book file){
        if ( !files.contains(file)) {
	        List<WordLocation> wordLocationList = new FileParser().parseFile(file);
	        for (WordLocation wordLocation: wordLocationList) {
		        long wordId = WordService.insertWord(wordLocation.getWord());
		        wordLocation.setWordId(wordId);
		        WordService.addWordPosition(wordLocation);
	        }
            files.add(file);
        }
        menu.updateFileList(files);
    }

    public boolean isEmpty(){
        return files.isEmpty();
    }

    public Book getFile(String name){
        Book file;

        Iterator<Book> iter = files.iterator();
        while (iter.hasNext()) {
            Book current = iter.next();
            if ( current.getTitle().equals(name)) {
                file = current;
                return file;
            }
        }

        //if file name not found
        return null;

    }

}
