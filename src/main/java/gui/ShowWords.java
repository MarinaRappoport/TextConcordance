package gui;

import model.Book;
import model.WordLocation;
import service.BookService;
import service.WordService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ShowWords extends JFrame {
    private JPanel north, center, searchWords, chooseBook;
    private JButton search;
    private JTextField enterWord;
    private JLabel chooseBookLabel;
    private JComboBox<String> booksList;
    private JTextArea context;
    private JTable locationsTable;
    private ArrayList<Long> bookIdList;
    private ArrayList<Book> books;
    private int selectedBookIndex, count;
    private String word;
    private DefaultTableModel model;


    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);
    private final TitledBorder PREVIEW_TITLE = new TitledBorder(BorderFactory.createTitledBorder
            (BORDER, "Preview", 0, 0, new Font("Font", Font.BOLD,16)));

    public ShowWords(ArrayList<Book> books){
        this.books = books;
        word = "";
        selectedBookIndex = 0;
        bookIdList = new ArrayList<>();
        north = new JPanel();

        center = new JPanel();
        center.setLayout(new GridLayout(2,1,0,0));
        center.setBorder(BORDER);
        searchWords = new JPanel();
        searchWords.setBorder(BORDER);
        chooseBook = new JPanel();
        chooseBook.setBorder(BORDER);

        search = new JButton("Search");
        search.setFont(MY_FONT);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookIdList.clear();
                context.setText("");

                count = 1;
                model.setRowCount(0);

                selectedBookIndex = booksList.getSelectedIndex();
                word = enterWord.getText();

                if (selectedBookIndex == 0){ //search in all books
                    for (Book book : books){
                        addLocations(WordService.findWordInBooks(word, book.getId()), book);
                    }
                } else addLocations(WordService.findWordInBooks(word,books.get(selectedBookIndex-1).getId()),
                        books.get(selectedBookIndex-1));

            }
        });

        enterWord = new JTextField("Enter a Word");
        enterWord.setFont(MY_FONT);
        chooseBookLabel = new JLabel("Choose a book");
        chooseBookLabel.setFont(MY_FONT);

        locationsTable = new JTable( new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Line", "Phrase"}), 0){ //first column for numbering
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
        locationsTable.setFont(MY_FONT);
        locationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        locationsTable.setRowHeight(40);
        locationsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        model = (DefaultTableModel) locationsTable.getModel();
        TableColumnModel columnModel = locationsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(1).setPreferredWidth(340);
        columnModel.getColumn(2).setPreferredWidth(218);
        columnModel.getColumn(3).setPreferredWidth(145);
        columnModel.getColumn(4).setPreferredWidth(145);

        JScrollPane tableSP=new JScrollPane(locationsTable);
        tableSP.setVisible(true);

        locationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int row = locationsTable.getSelectedRow();
                createPreview(bookIdList.get(row), (int)locationsTable.getValueAt(row, 4));
            }
        });

        if (books.size() == 0){
            booksList = new JComboBox<>(new String[]{"No books to show"});
        }else {
            String[] booksArray = new String[books.size()+1];
            booksArray[0] = "All";
            int i = 1;
            for (Book curr : books) {
                booksArray[i] = curr.getTitle();
                i++;
            }
            booksList = new JComboBox<>(booksArray);
        }

        context = new JTextArea(14,150);
        context.setEditable(false);
        context.setLineWrap(true);
        context.setWrapStyleWord(true);
        TitledBorder title = BorderFactory.createTitledBorder
                (BORDER, "Preview", 0, 0, new Font("Font", Font.BOLD,18));
        title.setTitleJustification(TitledBorder.CENTER);
        context.setBorder(title);
        JScrollPane contextSP = new JScrollPane(context);

        searchWords.add(enterWord);
        searchWords.add(search);
        chooseBook.add(chooseBookLabel);
        chooseBook.add(booksList);
        north.add(searchWords);
        north.add(chooseBook);

        center.add(tableSP);
        center.add(contextSP);

        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void addLocations(List<WordLocation> locations, Book book) {

        for (WordLocation location : locations) {
            model.addRow(new Object[]{count++ , book.getTitle(), book.getAuthor(),
                    location.getLine(), location.getParagraph() });
            bookIdList.add( location.getBookId());
        }
    }

    private void createPreview(long bookId, int phrase){
        String text = WordService.buildPreview(bookId,phrase);
        context.setText(text);

        Highlighter highlighter = context.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.pink);

        int index = 0;
        while (index >= 0) {
            int p0 = text.indexOf(word, index);

            if ( p0 == -1 ) {
                break;
            }

            int p1 = p0 + word.length();

            if (!Character.isLetter(text.charAt(p0-1)) ) {
                if (!Character.isLetter(text.charAt(p1)) ) {
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

