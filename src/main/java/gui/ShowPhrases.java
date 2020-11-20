package gui;

import model.Book;
import model.WordLocation;
import service.FilesManager;
import service.PreviewService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShowPhrases extends JFrame{
    private int phrasesIndex;
    private JLabel phraseLabel, inBookLabel;
    private JButton newPhrase, result;
    private JComboBox<String> booksList;
    private JTable allLocations;
    private DefaultTableModel locationsTableModel, phrasesTableModel;
    private JTextArea context;
    private ArrayList<Book> books;
    private JPanel top1, top2,center2, phrasesPanel, previewPanel;
    private List phrasesList;

    private static final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private static final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public ShowPhrases(){
        setTitle("Show Phrases");

        phrasesIndex = 0;
        books = FilesManager.getInstance().getFiles();
        //TODO initial phrasesList

        inBookLabel = new JLabel("In Book :");
        inBookLabel.setFont(MY_FONT);

        newPhrase = new JButton("Add New Phrase");
        newPhrase.setFont(MY_FONT);
        newPhrase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO add new phrase to list and table
            }
        });

        result = new JButton("Show Result");
        result.setFont(MY_FONT);
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
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

        JTable phrasesTable = new JTable( new DefaultTableModel(
                (new String[]{" ", "Phrases"}), 0){ //first column for numbering
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });

        phrasesTable.setFont(MY_FONT);
        phrasesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        phrasesTable.setRowHeight(40);
        phrasesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        phrasesTableModel = (DefaultTableModel) phrasesTable.getModel();
        JScrollPane phrasesSP =new JScrollPane(phrasesTable);
        phrasesSP.setVisible(true);

        TableColumnModel columnModel = phrasesTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(45);
        columnModel.getColumn(1).setPreferredWidth(800);

        allLocations = PreviewService.createLocationsTable();
        locationsTableModel = (DefaultTableModel) allLocations.getModel();
        JScrollPane locationsSP =new JScrollPane(allLocations);
        locationsSP.setVisible(true);

        context = PreviewService.createPreview();

        top1 = new JPanel();
        top1.add(newPhrase);

        phrasesPanel = new JPanel();
        phrasesPanel.setLayout(new BorderLayout());
        phrasesPanel.add(top1, BorderLayout.NORTH);
        phrasesPanel.add(phrasesSP, BorderLayout.CENTER);

        top2 = new JPanel();
        top2.add(inBookLabel);
        top2.add(booksList);
        top2.add(result);

        center2 = new JPanel();
        center2.setLayout(new BoxLayout (center2, BoxLayout.Y_AXIS));
        center2.add(locationsSP);
        center2.add(context);

        previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(top2, BorderLayout.NORTH);
        previewPanel.add(center2, BorderLayout.CENTER);

        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        );
        add(phrasesPanel);
        add(previewPanel);

    }

    private void addNewPhrase(String phrase){

        phrasesTableModel.addRow(new Object[]{phrasesIndex ,phrase});

    }

}
