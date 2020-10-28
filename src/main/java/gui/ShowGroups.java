package gui;

import model.Book;
import model.Group;
import service.GroupService;
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

public class ShowGroups extends JFrame {
    private JButton addWord, addGroup, showResult;
    private JLabel selectBook, selectGroup, resultJLabel;
    private JComboBox<String> booksList, groupsList;
    private int selectedBookIndex, selectedGroupIndex;
    private JTextField enterWord, enterGroup;;
    private ArrayList<Group> groups;
    private Group currentGroup;
    private DefaultTableModel locationsTableModel, wordsTableModel;
    private ArrayList<Long> bookIdList;
    private ArrayList<Book> books;

    private JPanel west, north, center, chooseBookPanel, groupsPanel, addWordPanel, resultPanel, userSelections;
    private JTextArea context;
    private JTable locationsTable, wordsTable;

    final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public ShowGroups(ArrayList<Book> books){
        this.books = books;
        groups = new ArrayList<>();

        chooseBookPanel = new JPanel();
        groupsPanel = new JPanel();
        addWordPanel = new JPanel();
        resultPanel = new JPanel();

        north = new JPanel();
        north.setLayout(new BorderLayout());

        userSelections = new JPanel();
        userSelections.setLayout(new GridLayout(2,2,30,10));

        west = new JPanel();
        west.setPreferredSize(new Dimension(165,400));
        west.setLayout(new BorderLayout());

        center = new JPanel();
        center.setLayout( new GridLayout(2,1,0,2));

        selectBook = new JLabel("Select a Book");
        selectBook.setFont(MY_FONT);

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
        booksList.setFont(MY_FONT);

        selectGroup = new JLabel("Select a Group");
        selectGroup.setFont(MY_FONT);

        if (groups.size() == 0){
            groupsList = new JComboBox<>(new String[]{"      "});
        }else {
            String[] groupsArray = new String[groups.size()+1];
            int i = 0;
            for (Group curr : groups) {
                groupsArray[i] = curr.getName();
                i++;
            }
            groupsList = new JComboBox<>(groupsArray);
        }
        groupsList.setFont(MY_FONT);
        groupsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 updateWords();
            }
        });

        enterGroup = new JTextField("New Group");
        enterGroup.setFont(MY_FONT);
        addGroup = new JButton("Add New Group");
        addGroup.setFont(MY_FONT);

        addGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupName = JOptionPane.showInputDialog("Enter new group name");
                Group newGroup = new Group(groupName);
                if (!groups.contains(newGroup)){
                    groups.add(newGroup);
                    groupsList.addItem(groupName);
                }
                else JOptionPane.showMessageDialog(null, "Group already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        enterWord = new JTextField("Enter Word");
        enterWord.setFont(MY_FONT);
        addWord = new JButton("Add Word To Group");
        addWord.setFont(MY_FONT);

        addWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( groupsList.getSelectedIndex() == 0 )
                    JOptionPane.showMessageDialog(null, "You must choose a group", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    Group currentGroup = groups.get(groupsList.getSelectedIndex() - 1);
                    currentGroup.addWord(enterWord.getText());
                    enterWord.setText("");
                    updateWords();
                }
            }
        });

        locationsTable = PreviewService.createLocationsTable();
        locationsTableModel = (DefaultTableModel) locationsTable.getModel();
        JScrollPane locationsSP =new JScrollPane(locationsTable);
        locationsSP.setVisible(true);

        locationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int row = locationsTable.getSelectedRow();
                PreviewService.createPreview(context, currentGroup.getWords().toArray(new String[0]), bookIdList.get(row), (int)locationsTable.getValueAt(row, 4));
            }
        });

        context = PreviewService.createPreview();
        JScrollPane contextSP =new JScrollPane(context);
        contextSP.setVisible(true);


        wordsTable = new JTable( new DefaultTableModel(
                (new String[]{"Words In Group"}), 0){
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
        wordsTable.setFont(MY_FONT);

        wordsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordsTable.setRowHeight(40);
        wordsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        wordsTableModel = (DefaultTableModel) wordsTable.getModel();
        TableColumnModel columnModel = wordsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(160);
        JScrollPane wordsTableSP =new JScrollPane(wordsTable);
        wordsTableSP.setVisible(true);

        showResult = new JButton("Show Result");
        showResult.setFont(MY_FONT);
        showResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (groupsList.getSelectedIndex() > 0) {
                    currentGroup = groups.get(groupsList.getSelectedIndex() - 1);
                    bookIdList = PreviewService.searchWord(books, booksList.getSelectedIndex(), currentGroup.getWords().toArray(new String[0]), locationsTableModel);
                }
            }
        });

        resultJLabel = new JLabel("");

        groupsPanel.add(selectGroup);
        groupsPanel.add(groupsList);
        groupsPanel.add(addGroup);
        chooseBookPanel.add(selectBook);
        chooseBookPanel.add(booksList);
        addWordPanel.add(enterWord);
        addWordPanel.add(addWord);

        resultPanel.add(showResult);
        resultPanel.add(resultJLabel);

        userSelections.add(groupsPanel);
        userSelections.add(chooseBookPanel);
        userSelections.add(addWordPanel);

        north.add(userSelections, BorderLayout.CENTER);
        north.add(resultPanel, BorderLayout.SOUTH);

        center.add(locationsSP);
        center.add(contextSP);

        west.add(wordsTableSP, BorderLayout.CENTER);

        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(west, BorderLayout.WEST);

    }

    private void updateWords() {
        if (groupsList.getSelectedIndex() != 0) {
            wordsTableModel.setRowCount(0);

            for (String word : groups.get(groupsList.getSelectedIndex() - 1).getWords()) {
                wordsTableModel.addRow((new Object[]{word}));
            }
        }
    }


}
