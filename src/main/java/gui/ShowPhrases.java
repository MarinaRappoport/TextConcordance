package gui;

import model.Book;
import model.WordLocation;
import service.BookService;
import service.FilesManager;
import service.PhraseService;
import service.PreviewService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShowPhrases extends JFrame{
    private int phrasesIndex;
    private JLabel inBookLabel;
    private JButton newPhrase;
    private JComboBox<String> booksList;
    private JTable allLocations, phrasesTable;
    private DefaultTableModel locationsTableModel, phrasesTableModel;
    private JTextArea context;
    private ArrayList<Book> books;
    private JPanel top1, top2,center2, phrasesPanel, previewPanel;
    private Map<Integer, String> allPhrases;
	private List<Integer> bookIdList;
    private String currentPhrase;

    private static final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private static final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public ShowPhrases(){
        Iterator itr;
        allPhrases = PhraseService.getAllPhrases();
        setTitle("Show Phrases");

        phrasesIndex = 1;
        books = FilesManager.getInstance().getFiles();
	    bookIdList = new ArrayList<Integer>();

        inBookLabel = new JLabel("In Book :");
        inBookLabel.setFont(MY_FONT);

        newPhrase = new JButton("Add New Phrase");
        newPhrase.setFont(MY_FONT);
        newPhrase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPhrase = JOptionPane.showInputDialog("Enter new phrase");

                if ( !allPhrases.containsValue(newPhrase) ) {
                    int newId = PhraseService.saveNewPhrase(newPhrase);
                    allPhrases.put(newId, newPhrase);
                    addNewPhrase(newPhrase);
                } else JOptionPane.showMessageDialog(null, "Phrase already exists", "Error", JOptionPane.ERROR_MESSAGE);
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
        booksList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPhrase();
            }
        });

        phrasesTable = new JTable( new DefaultTableModel(
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

        phrasesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                searchPhrase();
            }
        });
        for(itr = this.allPhrases.entrySet().iterator(); itr.hasNext();) {
            addNewPhrase((String)((Map.Entry)itr.next()).getValue());
        }
        context = PreviewService.createPreview();

        allLocations = PreviewService.createLocationsTable();
        locationsTableModel = (DefaultTableModel) allLocations.getModel();
        JScrollPane locationsSP =new JScrollPane(allLocations);
        locationsSP.setVisible(true);

        allLocations.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                String[] words = currentPhrase.split("\\s+");

                int row = allLocations.getSelectedRow();
                PreviewService.createPhrasePreview(context, words, bookIdList.get(row), (int)allLocations.getValueAt(row, 4));
            }
        });


        top1 = new JPanel();
        top1.add(newPhrase);

        phrasesPanel = new JPanel();
        phrasesPanel.setLayout(new BorderLayout());
        phrasesPanel.add(top1, BorderLayout.NORTH);
        phrasesPanel.add(phrasesSP, BorderLayout.CENTER);

        top2 = new JPanel();
        top2.add(inBookLabel);
        top2.add(booksList);

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

        phrasesTableModel.addRow(new Object[]{phrasesIndex++ ,phrase});

    }

    public int getKey(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public void searchPhrase(){
        bookIdList.clear();
        locationsTableModel.setRowCount(0);
        int index = 1;
        List<WordLocation> wordLocations;

        int row = phrasesTable.getSelectedRow();

        if( row == -1 ) //No phrase selected
            return;

        currentPhrase = (String)phrasesTable.getValueAt(row, 1);
        int phraseId = getKey(allPhrases, currentPhrase);
        if ( booksList.getSelectedIndex() == 0 ){
            wordLocations = PhraseService.findPhraseInBooks(phraseId,null);
        }
        else {
	        int bookId = FilesManager.getFile((String) booksList.getSelectedItem()).getId();
            wordLocations = PhraseService.findPhraseInBooks(phraseId,bookId);
        }

        for (WordLocation location : wordLocations){
            Book book = BookService.findBookById(location.getBookId());
            locationsTableModel.addRow(new Object[]{index++ , book.getTitle(), book.getAuthor(),
                    location.getLine(), location.getParagraph() });
            bookIdList.add( location.getBookId());
        }
    }


}
