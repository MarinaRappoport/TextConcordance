package gui;

import model.Book;
import model.WordLocation;
import service.BookService;
import service.FilesManager;
import service.PhraseService;
import service.WordService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LocationsTableComponent extends JTable {
    private DefaultTableModel tableModel;

    final static Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final static Color DEFAULT = new Color(206, 200, 200, 2);
    private final static Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public LocationsTableComponent(){
        super(new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Line", "Paragraph"}),0));

        setFont(MY_FONT);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setRowHeight(40);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(45);
        columnModel.getColumn(1).setPreferredWidth(350);
        columnModel.getColumn(2).setPreferredWidth(220);
        columnModel.getColumn(3).setPreferredWidth(155);

        tableModel = (DefaultTableModel) getModel();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public ArrayList<Long> searchWord(ArrayList<Book> books, int selectedBookIndex, String[] word){
        ArrayList<Long> bookIdList = new ArrayList<>();

        int count = 1;
        tableModel.setRowCount(0);

        if (selectedBookIndex == 0){ //search in all books
            for (Book book : books){
                ArrayList<Long> idList;
                for (int i = 0 ; i < word.length ; i++ ) {
                    idList = addLocations
                            (count, WordService.findWordInBooks(word[i], book.getId()), book);
                    bookIdList.addAll(idList);
                    count += idList.size();
                }
            }
        } else {
            for (int i = 0 ; i < word.length ; i++ ) {
                bookIdList = addLocations
                        (count, WordService.findWordInBooks(word[i], books.get(selectedBookIndex - 1).getId()),
                                books.get(selectedBookIndex - 1));
            }
        }

        return bookIdList;

    }

    public ArrayList<Long> addLocations(int count, List<WordLocation> locations, Book book) {
        int index = count;
        ArrayList<Long> idList = new ArrayList<>();

        for (WordLocation location : locations) {
            tableModel.addRow(new Object[]{index++ , book.getTitle(), book.getAuthor(),
                    location.getLine(), location.getParagraph() });
            idList.add( location.getBookId());
        }

        return idList;
    }

    /*
    public void searchPhrase(int phraseRow, int selectedBookIndex){
        List<Long> bookIdList = new ArrayList<Long>();
        tableModel.setRowCount(0);
        int index = 1;
        List<WordLocation> wordLocations;

        //int phraseRow = phrasesTable.getSelectedRow();

        if( phraseRow == -1 ) //No phrase selected
            return;

        String currentPhrase = (String)phrasesTable.getValueAt(phraseRow, 1);
        int phraseId = getKey(allPhrases, currentPhrase);
        //if ( booksList.getSelectedIndex() == 0 ){
        if ( selectedBookIndex == 0 ){
            wordLocations = PhraseService.findPhraseInBooks(phraseId,null);
        }
        else {
            Long bookId = FilesManager.getFile((String) booksList.getSelectedItem()).getId();
            wordLocations = PhraseService.findPhraseInBooks(phraseId,bookId);
        }

        for (WordLocation location : wordLocations){
            Book book = BookService.findBookById(location.getBookId());
            tableModel.addRow(new Object[]{index++ , book.getTitle(), book.getAuthor(),
                    location.getLine(), location.getParagraph() });
            bookIdList.add( location.getBookId());
        }
    }

     */

    public void clearTable(){
        tableModel.setRowCount(0);
    }

    public void addRow(Object[] data){
        tableModel.addRow(data);
    }


}
