package gui;

import model.Book;
import service.FilesManager;
import service.GroupService;

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
import java.util.Map;

public class ShowGroups extends JFrame {
    private JButton  addGroup, showResult;
    private JLabel addWord, selectBook, selectGroup, resultJLabel;
    private JComboBox<String> booksList, groupsList;
    private JTextField enterWord, enterGroup;
    private Map<String, Integer> groups;
    private String currentGroup;
    private Integer currentGroupId;
	private DefaultTableModel wordsTableModel;
	private ArrayList<Integer> bookIdList;
    private ArrayList<Book> books;

    private JPanel west, north, center, chooseBookPanel, groupsPanel, addWordPanel, resultPanel, userSelections;
	private TextPreviewComponent context;
	private LocationsTableComponent locationsTable;
	private JTable wordsTable;

	private static final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT,18);
    private static final Color DEFAULT = new Color(206, 200, 200, 2);
    private final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

    public ShowGroups(){
        setTitle("Show Groups");

        books = FilesManager.getInstance().getFiles();
	    groups = GroupService.getAllGroupsId();

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
            String[] groupsArray = new String[groups.size()];

            int i = 0;
            Iterator<Map.Entry<String, Integer>> itr = groups.entrySet().iterator();

            while ( itr.hasNext() ) {
                groupsArray[i] = itr.next().getKey();
                i++;
            }

            groupsList = new JComboBox<>(groupsArray);
        }
        groupsList.setFont(MY_FONT);
        groupsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( groupsList.getItemCount() == 0 )
                    return;
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

                if ((groupName == null) || (groupName.equals("")))
                    return;

                if (groups.containsKey(groupName)) {
                    JOptionPane.showMessageDialog(null, "Group already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Integer id = (int) (long) GroupService.createNewGroup(groupName);
                    groups.put(groupName, id);

                    if (groups.size()==1)
                        groupsList.removeAllItems();

                    groupsList.addItem(groupName);
                }

            }
        });

        enterWord = new JTextField("");
        enterWord.setColumns(20);
        enterWord.setFont(MY_FONT);
        addWord = new JLabel("Add Word To Group : ");
        addWord.setFont(MY_FONT);

        enterWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentGroup = (String)groupsList.getSelectedItem();
                String word = enterWord.getText().trim();
                if (!word.equals("")) {
                    if ( groups.size() > 0 ) {
                        if (GroupService.getAllWordsForGroup(currentGroupId).contains(word))
                            return;

                        GroupService.addWordToGroup(word, groups.get(currentGroup));
                        enterWord.setText("");
                        updateWords();
                    }
                }
            }
        });

	    locationsTable = new LocationsTableComponent();
        JScrollPane locationsSP =new JScrollPane(locationsTable);
        locationsSP.setVisible(true);

        locationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int row = locationsTable.getSelectedRow();

	            context.createPreview(GroupService.getAllWordsForGroup(currentGroupId).toArray(new String[0]), bookIdList.get(row), (int) locationsTable.getValueAt(row, 4));
            }
        });

	    context = new TextPreviewComponent(false);

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
                currentGroup = (String)groupsList.getSelectedItem();
                Integer currentGroupID = groups.get(currentGroup);
	            bookIdList = locationsTable.searchWord
			            (books, booksList.getSelectedIndex(), GroupService.getAllWordsForGroup(currentGroupID).toArray(new String[0]));

            }
        });

        resultJLabel = new JLabel("");

        groupsPanel.add(selectGroup);
        groupsPanel.add(groupsList);
        groupsPanel.add(addGroup);
        chooseBookPanel.add(selectBook);
        chooseBookPanel.add(booksList);
        addWordPanel.add(addWord);
        addWordPanel.add(enterWord);

        resultPanel.add(showResult);
        resultPanel.add(resultJLabel);

        userSelections.add(groupsPanel);
        userSelections.add(chooseBookPanel);
        userSelections.add(addWordPanel);

        north.add(userSelections, BorderLayout.CENTER);
        north.add(resultPanel, BorderLayout.SOUTH);

        center.add(locationsSP);
	    center.add(context);

        west.add(wordsTableSP, BorderLayout.CENTER);

        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(west, BorderLayout.WEST);

    }

    private void updateWords() {
        wordsTableModel.setRowCount(0);

        currentGroup = (String)groupsList.getSelectedItem();
        if (currentGroup.trim().isEmpty())
            return;

        currentGroupId = groups.get(currentGroup);

        for (String word : GroupService.getAllWordsForGroup(currentGroupId)) {
            wordsTableModel.addRow((new Object[]{word}));
        }
    }
}
