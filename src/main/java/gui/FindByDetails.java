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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

/*
Window of find word by location
And find book by details
 */
public class FindByDetails extends JFrame {
	private JPanel findBook, wordSearchPanel, findWord, bookDetails, bookSearchPanel, wordDetails;
	private JLabel findBookTitle, findWordTitle, title, author, translator, fromDate, toDate, dateFormatNote, nothing,
			line, index, wordResult, inBook;
	private JButton searchBook, searchWord, clearToDate, clearFromDate;
	private JTextField titleField, authorField, translatorField, lineField, indexField;
	private JTextField toDateField, fromDateField;
	private JTable bookResultTable;
	private ArrayList<Book> books;
	private java.util.List<Book> bookListResult;
	private JComboBox<String> booksList;
	private DefaultTableModel tableModel;

	private final static Border SEPARATOR = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
	private final static Font TITLE = new Font("Title", Font.BOLD, 18);
	private final static Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT, 18);
	private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	public FindByDetails() {
		bookListResult = new ArrayList<>();
		setTitle("Find By Details");
		books = FilesManager.getInstance().getFiles();

		dateFormatNote = new JLabel("Expected date : dd/mm/yyyy");
		nothing = new JLabel("");

		String[] booksArray;
		int i = 0;
		Iterator itr;
		if (this.books.size() == 0) {
			this.booksList = new JComboBox(new String[]{"No books to show"});
		} else {
			booksArray = new String[this.books.size()];

			for (itr = this.books.iterator(); itr.hasNext(); ++i) {
				Book curr = (Book) itr.next();
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
		titleField.setColumns(10);
		titleField.setMinimumSize(titleField.getPreferredSize());
		authorField = new JTextField();
		authorField.setFont(MY_FONT);
		authorField.setColumns(10);
		translatorField = new JTextField();
		translatorField.setFont(MY_FONT);
		translatorField.setColumns(10);
		fromDateField = new JTextField();
		fromDateField.setFont(MY_FONT);
		fromDateField.setColumns(10);
		toDateField = new JTextField();
		toDateField.setFont(MY_FONT);
		toDateField.setColumns(10);

		clearToDate = new JButton("Clear");
		clearToDate.setFont(MY_FONT);
		clearFromDate = new JButton("Clear");
		clearFromDate.setFont(MY_FONT);

		clearFromDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fromDateField.setText("");
			}
		});

		clearToDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toDateField.setText("");
			}
		});

		searchBook = new JButton("Search Book");
		searchBook.setFont(MY_FONT);
		searchBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!validDateFormat(fromDateField)) {
					return;
				}
				if (!validDateFormat(toDateField)) {
					return;
				}
				if (!validDatesRange()) {
					JOptionPane.showMessageDialog(null, "Invalid dates range",
							"Error", JOptionPane.ERROR_MESSAGE);
					fromDateField.setText("");
					toDateField.setText("");
					return;
				}

				tableModel.setRowCount(0);
				int index = 1;

				String string_fromDate;
				String string_toDate;

				if (fromDateField.getText().equals(""))
					string_fromDate = null;
				else string_fromDate = fromDateField.getText();

				if (toDateField.getText().equals(""))
					string_toDate = null;
				else string_toDate = toDateField.getText();

				bookListResult = BookService.findBookByDetails(titleField.getText().equals("") ? null : titleField.getText(),
						authorField.getText().equals("") ? null : authorField.getText(),
						translatorField.getText().equals("") ? null : translatorField.getText(),
						string_fromDate, string_toDate);

				for (Book curr : bookListResult) {
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

				if (result == null)
					result = "Invalid location (White space or after the line was ended)";

				wordResult.setText("Result : " + result);
			}
		});

		bookResultTable = new JTable(new DefaultTableModel(
				(new String[]{" ", "Title", "Author", "Translator", "Release Date"}), 0) {
			public boolean isCellEditable(int row, int column) {
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

		JScrollPane tableSP = new JScrollPane(bookResultTable);
		tableSP.setVisible(true);

		bookDetails = new JPanel();
		bookDetails.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 10, 5, 10);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		bookDetails.add(title, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 2.5;
		bookDetails.add(titleField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		bookDetails.add(author, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		bookDetails.add(authorField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		bookDetails.add(translator, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		bookDetails.add(translatorField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		bookDetails.add(fromDate, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		bookDetails.add(fromDateField, gbc);

		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		bookDetails.add(clearFromDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 2.5;
		bookDetails.add(toDate, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		bookDetails.add(toDateField, gbc);

		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		bookDetails.add(clearToDate, gbc);

		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		bookDetails.add(dateFormatNote, gbc);

		bookSearchPanel = new JPanel();
		bookSearchPanel.setLayout(new BorderLayout());
		bookSearchPanel.add(bookDetails, BorderLayout.CENTER);
		bookSearchPanel.add(searchBook, BorderLayout.SOUTH);

		findBook = new JPanel();
		findBook.add(bookSearchPanel);
		findBook.add(tableSP);

		wordDetails = new JPanel();
		wordDetails.setLayout(new GridLayout(3, 2, 5, 10));

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
		gbc.insets = new Insets(20, 10, 0, 10);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(findBookTitle, gbc);

		gbc.insets = new Insets(5, 10, 20, 10);
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

		gbc.insets = new Insets(20, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridheight = 1;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(findWordTitle, gbc);

		gbc.insets = new Insets(5, 10, 20, 10);
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

	private boolean validDateFormat(JTextField dateField) {
		String dateStr = dateField.getText();
		if (!dateStr.isEmpty()) {
			try {
				DATE_FORMAT.setLenient(false);
				DATE_FORMAT.parse(dateStr);
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(null, "Invalid date format!",
						"Error", JOptionPane.ERROR_MESSAGE);
				fromDateField.setText("");
				toDateField.setText("");
				return false;
			}
		}
		return true;
	}

	private boolean validDatesRange() {
		if (fromDateField.getText().isEmpty() || toDateField.getText().isEmpty())
			return true;

		boolean isValid = true;

		int fromYear = Integer.valueOf(fromDateField.getText().substring(6));
		int fromMonth = Integer.valueOf(fromDateField.getText().substring(3, 5));
		int fromDay = Integer.valueOf(fromDateField.getText().substring(0, 2));
		int toYear = Integer.valueOf(toDateField.getText().substring(6));
		int toMonth = Integer.valueOf(toDateField.getText().substring(3, 5));
		int toDay = Integer.valueOf(toDateField.getText().substring(0, 2));

		if (fromYear > toYear)
			isValid = false;
		else if (fromYear == toYear) {
			if (fromMonth > toMonth)
				isValid = false;
			else if (fromMonth == toMonth) {
				if (fromDay > toDay)
					isValid = false;
			}
		}
		return isValid;
	}
}
