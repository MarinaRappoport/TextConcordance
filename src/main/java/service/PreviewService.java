package service;

import model.Book;
import model.WordLocation;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PreviewService {

    final static Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final static Color DEFAULT = new Color(206, 200, 200, 2);
    private final static Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public static ArrayList<Long> searchWord(ArrayList<Book> books, int selectedBookIndex, String[] word, DefaultTableModel model){
        ArrayList<Long> bookIdList = new ArrayList<>();

        int count = 1;
        model.setRowCount(0);

        if (selectedBookIndex == 0){ //search in all books
            for (Book book : books){
                ArrayList<Long> idList;
                for (int i = 0 ; i < word.length ; i++ ) {
                    idList = PreviewService.addLocations
                            (count, model, WordService.findWordInBooks(word[i], book.getId()), book);
                    bookIdList.addAll(idList);
                    count += idList.size();
                }
            }
        } else {
            for (int i = 0 ; i < word.length ; i++ ) {
                bookIdList = PreviewService.addLocations
                        (count, model, WordService.findWordInBooks(word[i], books.get(selectedBookIndex - 1).getId()),
                                books.get(selectedBookIndex - 1));
            }
        }

        return bookIdList;

    }

    public static JTable createLocationsTable(){
        JTable table = new JTable( new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Line", "Phrase"}), 0){ //first column for numbering
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });

        table.setFont(MY_FONT);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setRowHeight(40);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(45);
        columnModel.getColumn(1).setPreferredWidth(350);
        columnModel.getColumn(2).setPreferredWidth(220);
        columnModel.getColumn(3).setPreferredWidth(155);

        return table;
    }

    public static JTextArea createPreview(){
        JTextArea preview = new JTextArea(14,150);
        preview.setEditable(false);
        preview.setLineWrap(true);
        preview.setWrapStyleWord(true);
        TitledBorder title = BorderFactory.createTitledBorder
                (BORDER, "Preview", 0, 0, new Font("Font", Font.BOLD,18));
        title.setTitleJustification(TitledBorder.CENTER);
        preview.setBorder(title);

        return preview;
    }

    public static ArrayList<Long> addLocations(int count, DefaultTableModel model, List<WordLocation> locations, Book book) {
        int index = count;
        ArrayList<Long> idList = new ArrayList<>();

        for (WordLocation location : locations) {
            model.addRow(new Object[]{index++ , book.getTitle(), book.getAuthor(),
                    location.getLine(), location.getParagraph() });
            idList.add( location.getBookId());
        }

        return idList;
    }

    public static void createPreview(JTextArea context, String[] word, long bookId, int phrase){
        String text = WordService.buildPreview(bookId,phrase);
        context.setText(text);

        Highlighter highlighter = context.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.pink);


        for (int i = 0 ; i < word.length ; i++ ) {
            int index = 0;
            while (index >= 0) {
                int p0 = text.indexOf(word[i], index);

                if (p0 == -1) {
                    break;
                }

                int p1 = p0 + word[i].length();

                if ( (p0 == 0) || (!Character.isLetter(text.charAt(p0 - 1))) ) {
                    if (!Character.isLetter(text.charAt(p1))) {
                        try {
                            highlighter.addHighlight(p0, p1, painter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                index = p1;
            }
        }
    }
}
