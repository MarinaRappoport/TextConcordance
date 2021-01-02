package gui;

import model.Book;
import model.WordLocation;
import service.FileParser;
import service.FilesManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
GUI class of adding file window
 */
public class AddBook extends JFrame {
	private WaitingFrame waitingFrame;
	private JButton addFile, ok, prev, next;
	private int currBook;
	private JLabel title, releaseDate, author, translator, chooseFile, currPage, note;
	private JPanel mainPanel, pagesPanel, bottomPanel, okPanel;
	private JTextField dateTF, authorTF, titleTF, translatorTF;
	private FilesManager filesManager;
	private Map<Book, List<WordLocation>> bookMap;
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");

	private static final Font MY_FONT = new Font("Font", Font.TRUETYPE_FONT, 20);
	private static final Color DEFAULT = new Color(206, 200, 200, 2);
	private static final Border BORDER = BorderFactory.createLineBorder(DEFAULT, 35);

	public AddBook() {
		setTitle("Add Book");

		bookMap = new HashMap<>();
		filesManager = FilesManager.getInstance();
		currBook = 0;

		mainPanel = new JPanel();
		mainPanel.setBorder(BORDER);
		mainPanel.setLayout(new GridLayout(6, 2, 20, 20));

		bottomPanel = new JPanel();
		bottomPanel.setBorder(BORDER);
		bottomPanel.setLayout(new BorderLayout());

		okPanel = new JPanel();
		okPanel.setBorder(BorderFactory.createMatteBorder(1, 90, 1, 90, DEFAULT));

		pagesPanel = new JPanel();
		pagesPanel.setBorder(BORDER);
		pagesPanel.setLayout(new GridLayout(1, 3, 30, 15));

		//Create labels and text fields in the main panel :

		title = new JLabel("Title");
		title.setFont(MY_FONT);
		author = new JLabel("Author");
		author.setFont(MY_FONT);
		translator = new JLabel("Translator");
		translator.setFont(MY_FONT);
		releaseDate = new JLabel("Release date");
		releaseDate.setFont(MY_FONT);
		chooseFile = new JLabel("Choose files");
		chooseFile.setFont(MY_FONT);
		note = new JLabel("*Press enter to save changes");

		//Choose file with JFileChooser
		addFile = new JButton("Select files");
		addFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("File selection:");
				jfc.setMultiSelectionEnabled(true);
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File[] files = jfc.getSelectedFiles();

					Arrays.asList(files).forEach(x -> {
						Book newBook = new Book(x.getAbsolutePath());
						List<WordLocation> list = new FileParser().parseFile(newBook);
						if (list == null) {
							JOptionPane.showMessageDialog(null, "Failed to parse file: " + x.getAbsolutePath(),
									"Error", JOptionPane.WARNING_MESSAGE);
						} else
							bookMap.put(newBook, list);
					});

					updateBookDetails();
				}
			}
		});

		ActionListener textFieldListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] books = bookMap.keySet().toArray();
				Book book = (Book) books[currBook - 1];
				book.setTitle(titleTF.getText());
				book.setTranslator(translatorTF.getText());
				if (validateDate(dateTF))
					book.setDate(dateTF.getText());
				else dateTF.setText(book.getDate());
				book.setAuthor(authorTF.getText());
			}
		};

		titleTF = new JTextField();
		translatorTF = new JTextField();
		dateTF = new JTextField();
		authorTF = new JTextField();

		titleTF.addActionListener(textFieldListener);
		translatorTF.addActionListener(textFieldListener);
		dateTF.addActionListener(textFieldListener);
		authorTF.addActionListener(textFieldListener);

		//Create labels and buttons on the bottom panel :

		ActionListener btnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == prev) {
					if (currBook > 0)
						currBook -= 1;
				} else {
					if (currBook < bookMap.size())
						currBook += 1;
				}

				updateBookDetails();
			}
		};

		prev = new JButton("Previous");
		prev.addActionListener(btnListener);
		next = new JButton("Next");
		next.addActionListener(btnListener);
		currPage = new JLabel(currBook + "/0");


		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				waitingFrame = new WaitingFrame("Please wait for all files to be uploaded . . .");
				waitingFrame.pack();
				waitingFrame.setVisible(true);
				dispose();

				Thread addFilesThread = new Thread(new Runnable() {
					@Override
					public void run() {
						for (Map.Entry<Book, List<WordLocation>> entry : bookMap.entrySet()) {
							filesManager.addFile(entry.getKey(), entry.getValue());
						}

						waitingFrame.dispose();
					}
				});
				addFilesThread.start();
			}

		});
		getRootPane().setDefaultButton(ok);

		mainPanel.add(chooseFile);
		mainPanel.add(addFile);
		mainPanel.add(title);
		mainPanel.add(titleTF);
		mainPanel.add(author);
		mainPanel.add(authorTF);
		mainPanel.add(translator);
		mainPanel.add(translatorTF);
		mainPanel.add(releaseDate);
		mainPanel.add(dateTF);
		mainPanel.add(note);

		okPanel.add(ok);

		pagesPanel.add(prev);
		pagesPanel.add(currPage);
		pagesPanel.add(next);
		pagesPanel.setBorder(BORDER);

		bottomPanel.add(pagesPanel, BorderLayout.CENTER);
		bottomPanel.add(okPanel, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	private boolean validateDate(JTextField dateTF) {
		DATE_FORMAT.setLenient(false);
		try {
			DATE_FORMAT.parse(dateTF.getText());
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null, "Invalid date format!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void updateBookDetails() {
		if ((currBook == 0) && !bookMap.isEmpty())
			currBook = 1;

		Object[] books = bookMap.keySet().toArray();
		Book book = (Book) books[currBook - 1];
		currPage.setText(currBook + "/" + bookMap.size());
		titleTF.setText(book.getTitle());
		authorTF.setText(book.getAuthor());
		translatorTF.setText(book.getTranslator());
		dateTF.setText(book.getDate());
	}
}
