package gui;

import model.Book;
import service.FilesManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

//GUI of the main menu
public class MainMenu extends JFrame {
    private JTextArea statTextArea, common;
    private JTable filesTable;
    private JPanel buttons, center, bookDetails , bottomPanel;
    private JButton loadFile, showWords, showExp, showGroups, findBook, extractToXML;
    private FilesManager filesManager;
    private ArrayList<Book> allBooks;
    private ArrayList<Book> selectedBooks;

    final Color DEFAULT = new Color(206, 200, 200, 2);
    final Color PRIMARY = new Color(250, 160, 38);
    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public MainMenu(){

        //Create an instance of the files data base
        filesManager = FilesManager.getInstance();
        FilesManager.setMainMenu(this);

        allBooks = new ArrayList<>();
        selectedBooks = new ArrayList<>();

        filesTable = new JTable( new DefaultTableModel((new String[]{"Title", "Author", "Release Date", "Path"}), 0){
            public boolean isCellEditable(int row, int column)
            {
                return false;//all cells are not editable
            }
        });
        filesTable.setFont(MY_FONT);

        filesTable.setRowHeight(40);
        filesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = filesTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(300);
        columnModel.getColumn(1).setPreferredWidth(240);
        columnModel.getColumn(2).setPreferredWidth(240);
        columnModel.getColumn(3).setPreferredWidth(300 );

        JScrollPane tableSP=new JScrollPane(filesTable);
        tableSP.setVisible(true);

        filesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedBooks.clear();
                super.mousePressed(e);

                int[] selectedRow = filesTable.getSelectedRows();
                for (int row : selectedRow){
                    selectedBooks.add(filesManager.getFile( (String)filesTable.getValueAt(row,0)) );
                }
                updateStatistics();
            }
        });

        bookDetails = new JPanel();
        bookDetails.setLayout(new GridLayout(1,2,1,1));

        common = new JTextArea();
        common.setEditable(false);
        common.setColumns(20);
        common.setRows(7);
        TitledBorder commonTitle = BorderFactory.createTitledBorder
                (BORDER, "Top Words (50)", 0, 0, new Font("Font", Font.BOLD,18));
        commonTitle.setTitleJustification(TitledBorder.CENTER);
        common.setBorder(commonTitle);
        JScrollPane commonSP = new JScrollPane(common);

        //create the text area of statistics
        statTextArea = new JTextArea();
        statTextArea.setEditable(false);
        statTextArea.setColumns(20);
        statTextArea.setRows(7);
        TitledBorder statTitle = BorderFactory.createTitledBorder
                (BORDER, "Statistics", 0, 0, new Font("Font", Font.BOLD,18));
        statTitle.setTitleJustification(TitledBorder.CENTER);
        statTextArea.setBorder(statTitle);
        JScrollPane statSP = new JScrollPane(statTextArea);

        //create the panel that contains the details and statistics
        center = new JPanel();
        center.setLayout(new GridLayout(2,1,0,2));
        center.add(tableSP);
        center.add(statSP);

        //create buttons panels
        buttons = new JPanel();
        buttons.setBorder(BorderFactory.createMatteBorder(20, 100, 15, 100,DEFAULT));

        bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createMatteBorder(15, 100, 20, 100,DEFAULT));

        //create buttons
        loadFile = new JButton("Load New Files");
        loadFile.setFont(MY_FONT);
        loadFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AddFileFrame addFileFrame = new AddFileFrame();
                addFileFrame.setSize(700, 580);
                addFileFrame.setVisible(true);
                addFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        showWords = new JButton("Show Words");
        showWords.setFont(MY_FONT);
        showWords.setAlignmentX(Component.CENTER_ALIGNMENT);
        showWords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWords showWords = new ShowWords(allBooks);
                showWords.setSize(925, 650);
                showWords.setVisible(true);
                showWords.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        showExp = new JButton("Show Expressions");
        showExp.setFont(MY_FONT);
        showExp.setAlignmentX(Component.CENTER_ALIGNMENT);

        showGroups = new JButton("Show Groups");
        showGroups.setFont(MY_FONT);
        showGroups.setAlignmentX(Component.CENTER_ALIGNMENT);
        showGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowGroups showGroups  = new ShowGroups(allBooks);
                showGroups.setSize(1050, 650);
                showGroups.setVisible(true);
                showGroups.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        findBook = new JButton("Find Book");
        findBook.setFont(MY_FONT);
        findBook.setAlignmentX(Component.CENTER_ALIGNMENT);

        extractToXML = new JButton("Extract to XML");
        extractToXML.setFont(MY_FONT);
        extractToXML.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(extractToXML);

        buttons.add(loadFile);
        buttons.add(findBook);
        buttons.add(showWords);
        buttons.add(showGroups);
        buttons.add(showExp);

        bookDetails.add(statSP);
        bookDetails.add(commonSP);

        center.add(tableSP);
        center.add(bookDetails);

        add(buttons, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

    }

    public void updateStatistics() {

        int sumOfCharacters = 0;
        int sumOfWords = 0;
        int sumOfSentences = 0;
        int sumOfLines = 0;
        int sumOfParagraphs = 0;

        //Remove previous statistics
        statTextArea.setText("");

        //Iterating on files ArrayList and add all details to filesDetailsPanel
        Iterator<Book> iter = selectedBooks.iterator();
        while (iter.hasNext()){
            Book current = iter.next();

            sumOfCharacters += current.characterCount;
            sumOfWords += current.wordCount;
            sumOfSentences += current.sentenceCount;
            sumOfLines += current.lineCount;
            sumOfParagraphs += current.paragraphCount;

        }

        statTextArea.append("Total number of characters = " + sumOfCharacters+ "\n");
        statTextArea.append("Total word count = " + sumOfWords + "\n");
        statTextArea.append("Total number of sentences = " + sumOfSentences+ "\n");
        statTextArea.append("Total number of lines = " + sumOfLines + "\n");
        statTextArea.append("Number of paragraphs = " + sumOfParagraphs + "\n");
        statTextArea.append("*" + "\n");

        if (sumOfWords > 0) {
            statTextArea.append("Average characters in word = " + (sumOfCharacters / sumOfWords) + "\n");
            statTextArea.append("Average characters in sentence = " + (sumOfCharacters / sumOfSentences) + "\n");
            statTextArea.append("Average characters in paragraph  = " + (sumOfCharacters / sumOfParagraphs) + "\n");
            statTextArea.append("*" + "\n");

            statTextArea.append("Average words in sentence = " + (sumOfWords / sumOfSentences) + "\n");
            statTextArea.append("Average words in paragraph = " + (sumOfWords / sumOfParagraphs) + "\n");
            statTextArea.append("Average sentences in paragrph = " + (sumOfSentences / sumOfParagraphs) + "\n");
        }
        else
            statTextArea.append("No statistics to show");

    }

    public void updateFileList(ArrayList<Book> files) {

        allBooks.clear();

        Iterator<Book> iter = files.iterator();
        while (iter.hasNext()) {
            allBooks.add(iter.next());

        }

        updateFileDetails();
    }

    public void updateFileDetails() {

        DefaultTableModel model = (DefaultTableModel) filesTable.getModel();
        Book current = allBooks.get(allBooks.size()-1);
        model.addRow(new Object[]{current.getTitle(), current.getAuthor(), current.getDate(), current.getPath()});

    }



}
