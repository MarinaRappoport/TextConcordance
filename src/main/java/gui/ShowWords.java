package gui;

import model.Book;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;

public class ShowWords extends JFrame {
    private JPanel north, center, searchWords, chooseBook;
    private JButton search;
    private JTextField enterWord;
    private JLabel chooseBookLabel;
    private JComboBox<String> booksList;
    private JList<String> words;
    private JTextArea context;
    private JTable locationsTable;

    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);
    private final TitledBorder PREVIEW_TITLE = new TitledBorder(BorderFactory.createTitledBorder
            (BORDER, "Preview", 0, 0, new Font("Font", Font.BOLD,16)));

    public ShowWords(ArrayList<Book> books){

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
        enterWord = new JTextField("  Enter a Word  ");
        enterWord.setFont(MY_FONT);
        chooseBookLabel = new JLabel("Choose a book");
        chooseBookLabel.setFont(MY_FONT);

        locationsTable = new JTable( new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Line", "Paragraph"}), 0){ //first column for numbering
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
        locationsTable.setFont(MY_FONT);

        locationsTable.setRowHeight(40);
        locationsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = locationsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(220);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);

        JScrollPane tableSP=new JScrollPane(locationsTable);
        tableSP.setVisible(true);

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

        context = new JTextArea();
        context.setEditable(false);
        context.setColumns(20);
        context.setRows(7);
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
