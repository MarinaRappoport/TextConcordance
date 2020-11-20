package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindByDetails extends JFrame {
    private JPanel findBook, wordSearchPanel, findWord, bookDetails, bookSearchPanel, wordDetails;
    private JLabel findBookTitle, findWordTitle, title, author, translator, fromDate, toDate, line, index, wordResult;
    private JButton searchBook, searchWord;
    private JTextField titleField, authorField, translatorField, toDateField, fromDateField, lineField, indexField;
    private JTable bookResultTable;

    final static Font TITLE = new Font("Font", Font.BOLD,20);
    final static Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final static Color DEFAULT = new Color(206, 200, 200, 2);
    private final static Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public FindByDetails(){
        setTitle("Find By Details");

        findBookTitle = new JLabel("Find Book By Details");
        findBookTitle.setFont(TITLE);
        title = new JLabel("Title : ");
        title.setFont(MY_FONT);
        author = new JLabel("Author : ");
        author.setFont(MY_FONT);
        translator = new JLabel("Translator : ");
        translator.setFont(MY_FONT);
        fromDate = new JLabel("Release Date From : ");
        fromDate.setFont(MY_FONT);
        toDate = new JLabel("To : ");
        toDate.setFont(MY_FONT);

        titleField = new JTextField();
        titleField.setFont(MY_FONT);
        authorField = new JTextField();
        authorField.setFont(MY_FONT);
        translatorField = new JTextField();
        translatorField.setFont(MY_FONT);
        fromDateField = new JTextField();
        fromDateField.setFont(MY_FONT);
        toDateField = new JTextField();
        toDateField.setFont(MY_FONT);

        searchBook = new JButton("Search Book");
        searchBook.setFont(MY_FONT);
        searchBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO search the details
                //TODO add cooks to table
            }
        });

        findWordTitle = new JLabel("Find Word By Location");
        findWordTitle.setFont(TITLE);
        line = new JLabel("Line Number : ");
        line.setFont(MY_FONT);
        index = new JLabel("Index Number : ");
        index.setFont(MY_FONT);
        wordResult = new JLabel("Result : ");
        wordResult.setFont(MY_FONT);

        lineField = new JTextField();
        lineField.setFont(MY_FONT);
        indexField = new JTextField();
        indexField.setFont(MY_FONT);

        searchWord = new JButton("Search Word");
        searchWord.setFont(MY_FONT);
        searchWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO find the word
            }
        });

        bookResultTable = new JTable(new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Translator", "Release Date"}), 0){
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });

        DefaultTableModel tableModel = (DefaultTableModel) bookResultTable.getModel();
        bookResultTable.setFont(MY_FONT);
        bookResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookResultTable.setRowHeight(40);
        bookResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = bookResultTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(45);
        columnModel.getColumn(1).setPreferredWidth(350);
        columnModel.getColumn(2).setPreferredWidth(220);
        columnModel.getColumn(3).setPreferredWidth(155);

        JScrollPane tableSP =new JScrollPane(bookResultTable);
        tableSP.setVisible(true);

        bookDetails = new JPanel();
        bookDetails.setLayout(new GridLayout(5,2, 5,10));
        bookDetails.add(title);
        bookDetails.add(titleField);
        bookDetails.add(author);
        bookDetails.add(authorField);
        bookDetails.add(translator);
        bookDetails.add(translatorField);
        bookDetails.add(fromDate);
        bookDetails.add(fromDateField);
        bookDetails.add(toDate);
        bookDetails.add(toDateField);

        bookSearchPanel = new JPanel();
        bookSearchPanel.setLayout(new BorderLayout());
        bookSearchPanel.add(bookDetails, BorderLayout.CENTER);
        bookSearchPanel.add(searchBook, BorderLayout.SOUTH);

        findBook = new JPanel();
        findBook.add(bookSearchPanel);
        findBook.add(tableSP);

        wordDetails = new JPanel();
        wordDetails.setLayout(new GridLayout(2,2,5,10));
        wordDetails.add(line);
        wordDetails.add(lineField);
        wordDetails.add(index);
        wordDetails.add(indexField);

        wordSearchPanel = new JPanel();
        wordSearchPanel.setLayout(new BorderLayout());
        wordSearchPanel.add(wordDetails, BorderLayout.CENTER);
        wordSearchPanel.add(searchWord, BorderLayout.SOUTH);

        findWord = new JPanel();
        findWord.add(wordSearchPanel);
        findWord.add(wordResult);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,5,10,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(findBookTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(bookSearchPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tableSP, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(findWordTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(wordSearchPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(wordResult, gbc);

    }
}
