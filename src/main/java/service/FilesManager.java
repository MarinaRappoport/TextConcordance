package service;

import gui.MainMenu;
import model.FileDetails;

import java.util.ArrayList;
import java.util.Iterator;

//Singleton class for handling files
public class FilesManager {
    private static ArrayList<FileDetails> files;
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
        files = new ArrayList<FileDetails>();
    }

    //Add only new file. We cannot add the same file twice.
    public void addFile(FileDetails file){
        if ( !files.contains(file)) {
            file.parseFile();
            files.add(file);
        }
        menu.updateFileList(files);
    }

    public boolean isEmpty(){
        return files.isEmpty();
    }

    public FileDetails getFile(String name){
        FileDetails file;

        Iterator<FileDetails> iter = files.iterator();
        while (iter.hasNext()) {
            FileDetails current = iter.next();
            if ( current.name.equals(name)) {
                file = current;
                return file;
            }
        }

        //if file name not found
        return null;

    }

}
