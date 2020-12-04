package gui;

import model.Book;
import service.BookService;
import service.FilesManager;
import service.WordService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class FindByDetails extends JFrame {
    private JPanel findBook, wordSearchPanel, findWord, bookDetails, bookSearchPanel, wordDetails;
    private JLabel findBookTitle, findWordTitle, title, author, translator, fromDate, toDate, line, index, wordResult, inBook;
    private JButton searchBook, searchWord;
    private JTextField titleField, authorField, translatorField, lineField, indexField;
    private JFormattedTextField toDateField, fromDateField;
    private Date Date_FromDate, Date_ToDate;
    private JTable bookResultTable;
    private ArrayList<Book> books;
    private java.util.List<Book> bookListResult;
    private JComboBox<String> booksList;
    private DefaultTableModel tableModel;

    final static Border SEPARATOR = BorderFactory.createMatteBorder(0,0,1,0,Color.black);
    final static Font TITLE = new Font("Title", Font.BOLD,18);
    final static Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final static Color DEFAULT = new Color(206, 200, 200, 2);
    private final static Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public FindByDetails(){
        bookListResult = new ArrayList<>();
        setTitle("Find By Details");
        books = FilesManager.getInstance().getFiles();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String[] booksArray;
        int i = 0;
        Iterator itr;
        if (this.books.size() == 0) {
            this.booksList = new JComboBox(new String[]{"No books to show"});
        } else {
            booksArray = new String[this.books.size()];

            for(itr = this.books.iterator(); itr.hasNext(); ++i) {
                Book curr = (Book)itr.next();
                booksArray[i] = curr.getTitle();
            }

            this.booksList = new JComboBox(booksArray);
        }

        findBookTitle = new JLabel("Find Book By Details");
        findBookTitle.setFont(TITLE);
        findBookTitle.setBorder(SEPARATOR);
        findWordTitle = new JLabel("Find Word By Position");
        findWordTitle.setFont(TITLE);
        findWordTitle.setBorder(SEPARATOR);


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
        fromDateField = new JFormattedTextField(df);
        fromDateField.setFont(MY_FONT);
        toDateField = new JFormattedTextField(df);
        toDateField.setFont(MY_FONT);

        searchBook = new JButton("Search Book");
        searchBook.setFont(MY_FONT);
        searchBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                int index = 1;

                java.sql.Date sql_FromDate;
                java.sql.Date sql_ToDate;

                if (fromDateField.getText().equals(""))
                    sql_FromDate = null;
                else {
                    try {
                        Date_FromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateField.getText());
                    } catch (Exception exception){
                        JOptionPane.showMessageDialog(null, "Invalid release from date", "Error", JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }
                    sql_FromDate = new java.sql.Date(Date_FromDate.getTime());
                }

                if (toDateField.getText().equals(""))
                    sql_ToDate = null;
                else {
                    try {
                        Date_FromDate = new SimpleDateFormat("dd/MM/yyyy").parse(toDateField.getText());
                    } catch (Exception exception){
                        JOptionPane.showMessageDialog(null, "Invalid release from date", "Error", JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }
                    sql_ToDate = new java.sql.Date(Date_FromDate.getTime());
                }

                bookListResult = BookService.findBookByDetails(titleField.getText().equals("")? null : titleField.getText(),
                        authorField.getText().equals("")? null : authorField.getText(),
                        translatorField.getText().equals("")? null : translatorField.getText(),
                        sql_FromDate ,sql_ToDate);

                for(Book curr : bookListResult){
                    tableModel.addRow(new Object[]{index++, curr.getTitle(), curr.getAuthor(), curr.getTranslator(), curr.getDate()});
                }
            }
        });

        line = new JLabel("Line Number : ");
        line.setFont(MY_FONT);
        index = new JLabel("Index Number : ");
        index.setFont(MY_FONT);
        wordResult = new JLabel("Result : ");
        wordResult.setFont(MY_FONT);
        inBook = new JLabel("In Book : ");
        inBook.setFont(MY_FONT);

        lineField = new JTextField();
        lineField.setFont(MY_FONT);
        indexField = new JTextField();
        indexField.setFont(MY_FONT);

        searchWord = new JButton("Search Word");
        searchWord.setFont(MY_FONT);
        searchWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = WordService.findWordByLocation(books.get(booksList.getSelectedIndex()).getId(),
                        Integer.valueOf(lineField.getText()), Integer.valueOf(indexField.getText()));

                if ( result == null )
                    result = "Invalid location (White space or after the line was ended)";

                wordResult.setText("Result : " + result);
            }
        });

        bookResultTable = new JTable(new DefaultTableModel(
                (new String[]{" ", "Title", "Author", "Translator", "Release Date"}), 0){
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });

        tableModel = (DefaultTableModel) bookResultTable.getModel();
        bookResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookResultTable.setRowHeight(40);
        bookResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = bookResultTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(35);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(250);

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
        wordDetails.setLayout(new GridLayout(3,2,5,10));

        wordDetails.add(line);
        wordDetails.add(lineField);
        wordDetails.add(index);
        wordDetails.add(indexField);
        wordDetails.add(inBook);
        wordDetails.add(booksList);

        wordSearchPanel = new JPanel();
        wordSearchPanel.setLayout(new BorderLayout());
        wordSearchPanel.add(wordDetails, BorderLayout.CENTER);
        wordSearchPanel.add(searchWord, BorderLayout.SOUTH);

        findWord = new JPanel();
        findWord.add(wordSearchPanel);
        findWord.add(wordResult);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20,10,0,10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(findBookTitle, gbc);

        gbc.insets = new Insets(5,10,20,10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(bookDetails, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(searchBook, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridheight = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 3.0;
        gbc.weighty = 3.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(tableSP, gbc);

        gbc.insets = new Insets(20,10,0,10);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridheight = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(findWordTitle, gbc);

        gbc.insets = new Insets(5,10,20,10);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        gbc.gridheight = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(wordDetails, gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(searchWord, gbc);

        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.gridheight = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(wordResult, gbc);

    }
}
