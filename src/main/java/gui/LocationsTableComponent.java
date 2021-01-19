package gui;

import model.Book;
import model.WordLocation;
import service.WordService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.List;

/**
This is a component that used for view locations in a table
Used for searching word, phrase, or group
 */
public class LocationsTableComponent extends JTable {
    private DefaultTableModel tableModel;

    public LocationsTableComponent(){
        super(new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Line", "Paragraph"}),0));

        setFont(GuiConstants.MY_FONT);
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

    public ArrayList<Integer> searchWord(ArrayList<Book> books, int selectedBookIndex, String[] word){
        ArrayList<Integer> bookIdList = new ArrayList<>();

        int count = 1;
        tableModel.setRowCount(0);

        if (selectedBookIndex == 0){ //search in all books
            for (Book book : books){
                ArrayList<Integer> idList;
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

    public ArrayList<Integer> addLocations(int count, List<WordLocation> locations, Book book) {
        int index = count;
        ArrayList<Integer> idList = new ArrayList<>();

        for (WordLocation location : locations) {
            tableModel.addRow(new Object[]{index++ , book.getTitle(), book.getAuthor(),
                    location.getLine(), location.getParagraph() });
            idList.add( location.getBookId());
        }

        return idList;
    }

    public void clearTable(){
        tableModel.setRowCount(0);
    }

    public void addRow(Object[] data){
        tableModel.addRow(data);
    }


}
