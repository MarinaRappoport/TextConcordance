package gui;

import model.Book;
import service.FilesManager;
import service.PreviewService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    private DefaultTableModel tableModel;

    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public ShowWords(){
        setTitle("Show Words");

        this.books = FilesManager.getInstance().getFiles();
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
                context.setText("");
                word = enterWord.getText();
                bookIdList = PreviewService.searchWord(books, booksList.getSelectedIndex(), new String[]{word}, tableModel);
            }
        });

        enterWord = new JTextField("Enter a Word");
        enterWord.setFont(MY_FONT);
        chooseBookLabel = new JLabel("Choose a book");
        chooseBookLabel.setFont(MY_FONT);

        locationsTable = PreviewService.createLocationsTable();
        tableModel = (DefaultTableModel) locationsTable.getModel();

        JScrollPane tableSP=new JScrollPane(locationsTable);
        tableSP.setVisible(true);

        locationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int row = locationsTable.getSelectedRow();
                PreviewService.createPreview(context, new String[]{word}, bookIdList.get(row), (int)locationsTable.getValueAt(row, 4));
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
}

