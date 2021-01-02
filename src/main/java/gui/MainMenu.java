package gui;

import model.Book;
import service.DbConnection;
import service.FilesManager;
import service.WordService;
import service.XmlSerializer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
GUI of the main menu
Include buttons for all the options anf details view:
- Details about chosen books
- Statistics of chosen books
- Top words of chosen books
 */
public class MainMenu extends JFrame {
	private WaitingFrame waitingFrame;
	private JTextArea statTextArea, common;
	private JTable filesTable;
	private DefaultTableModel model;
	private JPanel buttons, center, bookDetails, bottomPanel;
	private JButton loadFile, showWords, showExp, showGroups, findBook, extractToXML, importFromXML;
	private FilesManager filesManager;
	private ArrayList<Book> selectedBooks;
	private ArrayList<Integer> selectedBooksId;

	final Color DEFAULT = new Color(206, 200, 200, 2);
	final Color PRIMARY = new Color(250, 160, 38);
	final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT, 18);
	final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 2);

	public MainMenu() {
		setTitle("Main Menu");

		//Create an instance of the files data base
		filesManager = FilesManager.getInstance();
		FilesManager.setMainMenu(this);

		selectedBooksId = new ArrayList<Integer>();
		selectedBooks = new ArrayList<>();

		filesTable = new JTable(new DefaultTableModel((new String[]{"Title", "Author", "Translator", "Release Date", "Path"}), 0) {
			public boolean isCellEditable(int row, int column) {
				return false;//all cells are not editable
			}
		});
		filesTable.setFont(MY_FONT);

		filesTable.setRowHeight(40);
		filesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		model = (DefaultTableModel) filesTable.getModel();

		updateBookTable();

		TableColumnModel columnModel = filesTable.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(280);
		columnModel.getColumn(1).setPreferredWidth(220);
		columnModel.getColumn(2).setPreferredWidth(200);
		columnModel.getColumn(3).setPreferredWidth(220);
		columnModel.getColumn(4).setPreferredWidth(180);

		JScrollPane tableSP = new JScrollPane(filesTable);
		tableSP.setVisible(true);

		filesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selectedBooksId.clear();
				selectedBooks.clear();
				super.mousePressed(e);

				int[] selectedRow = filesTable.getSelectedRows();
				for (int row : selectedRow) {
					selectedBooks.add(filesManager.getFile((String) filesTable.getValueAt(row, 0)));
					selectedBooksId.add(selectedBooks.get(selectedBooks.size() - 1).getId());

				}
				updateStatistics();
				updateTopWords();
			}
		});

		bookDetails = new JPanel();
		bookDetails.setLayout(new GridLayout(1, 2, 1, 1));

		common = new JTextArea();
		common.setEditable(false);
		common.setColumns(20);
		common.setRows(7);
		TitledBorder commonTitle = BorderFactory.createTitledBorder
				(BORDER, "Top 50 Words", 0, 0, new Font("Font", Font.BOLD, 18));
		commonTitle.setTitleJustification(TitledBorder.CENTER);
		common.setBorder(commonTitle);
		JScrollPane commonSP = new JScrollPane(common);

		//create the text area of statistics
		statTextArea = new JTextArea();
		statTextArea.setEditable(false);
		statTextArea.setColumns(20);
		statTextArea.setRows(7);
		TitledBorder statTitle = BorderFactory.createTitledBorder
				(BORDER, "Statistics", 0, 0, new Font("Font", Font.BOLD, 18));
		statTitle.setTitleJustification(TitledBorder.CENTER);
		statTextArea.setBorder(statTitle);
		JScrollPane statSP = new JScrollPane(statTextArea);

		//create the panel that contains the details and statistics
		center = new JPanel();
		center.setLayout(new GridLayout(2, 1, 0, 2));
		center.add(tableSP);
		center.add(statSP);

		//create buttons panels
		buttons = new JPanel();
		buttons.setBorder(BorderFactory.createMatteBorder(20, 100, 15, 100, DEFAULT));

		bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createMatteBorder(15, 100, 20, 100, DEFAULT));

		//create buttons
		loadFile = new JButton("Load New Files");
		loadFile.setFont(MY_FONT);
		loadFile.setAlignmentX(Component.CENTER_ALIGNMENT);

		loadFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AddBook addBook = new AddBook();
				addBook.setSize(700, 580);
				addBook.setVisible(true);
				addBook.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		showWords = new JButton("Show Words");
		showWords.setFont(MY_FONT);
		showWords.setAlignmentX(Component.CENTER_ALIGNMENT);
		showWords.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowWords showWords = new ShowWords();
				showWords.setSize(900, 650);
				showWords.setVisible(true);
				showWords.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		showExp = new JButton("Show Phrases");
		showExp.setFont(MY_FONT);
		showExp.setAlignmentX(Component.CENTER_ALIGNMENT);
		showExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowPhrases showPhrases = new ShowPhrases();
				showPhrases.setSize(866, 650);
				showPhrases.setVisible(true);
				showPhrases.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		showGroups = new JButton("Show Groups");
		showGroups.setFont(MY_FONT);
		showGroups.setAlignmentX(Component.CENTER_ALIGNMENT);
		showGroups.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowGroups showGroups = new ShowGroups();
				showGroups.setSize(1250, 1000);
				showGroups.setVisible(true);
				showGroups.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		findBook = new JButton("Find By Details");
		findBook.setFont(MY_FONT);
		findBook.setAlignmentX(Component.CENTER_ALIGNMENT);
		findBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FindByDetails findByDetails = new FindByDetails();
				findByDetails.setSize(1080, 600);
				findByDetails.setVisible(true);
				findByDetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		extractToXML = new JButton("Extract To XML");
		extractToXML.setFont(MY_FONT);
		extractToXML.setAlignmentX(Component.CENTER_ALIGNMENT);
		extractToXML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose folder to save XML:");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File folder = jfc.getSelectedFile();
					try {
						XmlSerializer.exportToXml(folder);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Failed to export to XML",
								"Error", JOptionPane.WARNING_MESSAGE);
					}
					JOptionPane.showMessageDialog(null, "Done",
							"Export to XML", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		importFromXML = new JButton("Import From XML");
		importFromXML.setFont(MY_FONT);
		importFromXML.setAlignmentX(Component.CENTER_ALIGNMENT);
		importFromXML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
				jfc.setFileFilter(filter);
				jfc.setDialogTitle("Choose xml file to import:");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					waitingFrame = new WaitingFrame("Importing DB from XML file . . .");
					waitingFrame.pack();
					waitingFrame.setVisible(true);
					Thread xmlImportThread = new Thread(new Runnable() {
						@Override
						public void run() {
							File xmlFile = jfc.getSelectedFile();
							try {
								XmlSerializer.importFromXml(xmlFile);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "Failed to import from XML",
										"Error", JOptionPane.WARNING_MESSAGE);
							}
							updateBookTable();
							waitingFrame.dispose();
							JOptionPane.showMessageDialog(null, "Done",
									"Import from XML", JOptionPane.INFORMATION_MESSAGE);
						}
					});
					xmlImportThread.start();
				}
			}
		});

		bottomPanel.add(extractToXML);
		bottomPanel.add(importFromXML);

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

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				DbConnection.getInstance().closeConnection();
			}
		});
	}

	private void updateTopWords() {
		common.setText("");
		int i = 1;

		if (selectedBooks.size() == model.getRowCount()) //Selected all books
			selectedBooksId = null;

		Map<String, Integer> topWords = WordService.getTopWordsAppearances(selectedBooksId, 50);

		for (Map.Entry<String, Integer> entry : topWords.entrySet())
			common.append((i++) + ")       " + entry.getKey() + "    [" + entry.getValue() + "]\n");

		selectedBooksId = new ArrayList<Integer>();

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
		while (iter.hasNext()) {
			Book current = iter.next();

			sumOfCharacters += current.characterCount;
			sumOfWords += current.wordCount;
			sumOfSentences += current.sentenceCount;
			sumOfLines += current.lineCount;
			sumOfParagraphs += current.paragraphCount;

		}

		statTextArea.append("Total number of characters = " + sumOfCharacters + "\n");
		statTextArea.append("Total word count = " + sumOfWords + "\n");
		statTextArea.append("Total number of sentences = " + sumOfSentences + "\n");
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
		} else
			statTextArea.append("No statistics to show");

	}

	private void updateBookTable() {
		int size = model.getRowCount();
		for (int i = 0; i < size; i++) {
			model.removeRow(0);
		}
		for (Book current : FilesManager.getInstance().getBooks()) {
			model.addRow(new Object[]{current.getTitle(), current.getAuthor(), current.getTranslator(), current.getDate(), current.getPath()});
		}
	}

	public void updateFileDetails() {
		Book current = FilesManager.getInstance().getBooks().get(FilesManager.getInstance().getBooks().size() - 1);
		model.addRow(new Object[]{current.getTitle(), current.getAuthor(), current.getTranslator(), current.getDate(), current.getPath()});
	}

}
