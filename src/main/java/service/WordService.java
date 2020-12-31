package service;

import model.Word;
import model.WordLocation;

import java.sql.*;
import java.util.*;

public class WordService {
	private final static Connection connection = DbConnection.getInstance().getConnection();

	private final static String SQL_INSERT_WORD = "INSERT INTO word (value) VALUES (?)";
	private final static String SQL_SELECT_ALL_WORDS = "SELECT value,word_id from word ORDER by word_id";
	private final static String SQL_SELECT_BY_VALUE = "SELECT word_id from word WHERE value = ?";
	private final static String SQL_SELECT_BY_VALUE_LOWERCASE = "SELECT word_id from word WHERE lower(value) = ?";
	private final static String SQL_INSERT_WORD_LOCATION = "INSERT INTO word_in_book " +
			"(word_id,book_id,index,line,index_in_line,sentence,paragraph,is_quote_before,is_quote_after,punctuation_mark)" +
			" VALUES (?,?,?,?,?,?,?,?,?,?)";

	private final static String SQL_FIND_LOCATIONS_BY_WORD = "SELECT * from word_in_book where word_id IN (%s) ORDER by book_id, index";
	private final static String SQL_FIND_LOCATIONS_BY_WORD_AND_BOOK = "SELECT * from word_in_book where word_id IN (%s) AND book_id = ? ORDER by index";
	private final static String SQL_FIND_WORD_BY_LOCATION = "SELECT value from word_in_book, word where word.word_id = word_in_book.word_id AND book_id = ? AND line = ? AND index_in_line = ?";
	private final static String SQL_PREVIEW = "SELECT value, is_quote_before, is_quote_after, punctuation_mark FROM word, word_in_book where book_id = ? AND paragraph = ? AND word.word_id = word_in_book.word_id ORDER by index";

	private final static String SQL_FIND_WORDS_APPEARANCES_IN_BOOK = "SELECT DISTINCT lower(value) as lowercase_value, COUNT(value) from word, word_in_book where book_id = ? AND word.word_id = word_in_book.word_id GROUP BY lowercase_value ORDER by lowercase_value";
	private final static String SQL_FIND_WORDS_APPEARANCES = "SELECT DISTINCT lower(value) as lowercase_value, COUNT(value) from word, word_in_book where word.word_id = word_in_book.word_id GROUP BY lowercase_value ORDER by lowercase_value";
	private final static String SQL_TOP_WORDS_APPEARANCES_IN_BOOKS = "SELECT DISTINCT lower(value) as lowercase_value, COUNT(value) as word_counter from word, word_in_book where book_id IN (%s) AND word.word_id = word_in_book.word_id GROUP BY lowercase_value ORDER by word_counter DESC LIMIT ?";
	private final static String SQL_TOP_WORDS_APPEARANCES = "SELECT DISTINCT lower(value) as lowercase_value, COUNT(value) as word_counter from word, word_in_book where word.word_id = word_in_book.word_id GROUP BY lowercase_value ORDER by word_counter DESC LIMIT ?";
	private static final String SQL_SELECT_ALL_WORD_LOCATIONS = "SELECT * FROM word_in_book";


	public static int insertWord(String word) {
		int id = findWordIdByValue(word);
		if (id > 0) return id;
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, word);

			int affectedRows = statement.executeUpdate();

			if (affectedRows != 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						id = generatedKeys.getInt(1);
						statement.close();
						return id;
					} else {
						throw new SQLException("Creating word failed, no ID obtained.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int findWordIdByValue(String word) {
		PreparedStatement statement = null;
		int id = -1;
		try {
			statement = connection.prepareStatement(SQL_SELECT_BY_VALUE);
			statement.setString(1, word);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				id = rs.getInt("word_id");
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static List<Integer> findWordIdByValueCaseInsensitive(String word) {
		List<Integer> ids = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(SQL_SELECT_BY_VALUE_LOWERCASE);
			statement.setString(1, word.toLowerCase());
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				ids.add(rs.getInt("word_id"));
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ids;
	}

	public static Map<String, Integer> getAllWordsId() {
		Map<String, Integer> words = new LinkedHashMap<>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL_WORDS);
			while (rs.next())
				words.put(rs.getString(1), rs.getInt(2));
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return words;
	}

	public static List<Word> getAllWords() {
		List<Word> words = new ArrayList<>();
		Map<String, Integer> wordsMap = getAllWordsId();
		for (Map.Entry<String, Integer> entry : wordsMap.entrySet()) {
			words.add(new Word(entry.getValue(), entry.getKey()));
		}
		return words;
	}

	public static List<WordLocation> getAllWordLocations() {
		return getWordLocationList(SQL_SELECT_ALL_WORD_LOCATIONS);
	}

	public static void addWordLocationList(List<WordLocation> wordLocationList, Integer bookId) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD_LOCATION);
			for (WordLocation wordLocation : wordLocationList) {
				statement.setInt(1, wordLocation.getWordId());
				statement.setInt(2, bookId == null ? wordLocation.getBookId() : bookId);
				statement.setInt(3, wordLocation.getIndex());
				statement.setInt(4, wordLocation.getLine());
				statement.setInt(5, wordLocation.getIndexInLine());
				statement.setInt(6, wordLocation.getSentence());
				statement.setInt(7, wordLocation.getParagraph());
				statement.setBoolean(8, wordLocation.isQuoteBefore());
				statement.setBoolean(9, wordLocation.isQuoteAfter());
				statement.setString(10, wordLocation.getPunctuationMark());
				statement.addBatch();
				statement.clearParameters();
			}
			int[] results = statement.executeBatch();
			System.out.println("Loaded " + results.length + " word locations");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<WordLocation> findWordInBooks(String word, Integer bookId) {
		List<WordLocation> wordLocations = new LinkedList<>();
		List<Integer> wordIds = findWordIdByValueCaseInsensitive(word);
		if (!wordIds.isEmpty()) {
			try {
				StringJoiner sj = new StringJoiner(",");
				for (int i = 0; i < wordIds.size(); i++)
					sj.add("?");
				String sql = String.format(bookId == null ?
						SQL_FIND_LOCATIONS_BY_WORD : SQL_FIND_LOCATIONS_BY_WORD_AND_BOOK, sj.toString());
				PreparedStatement statement = connection.prepareStatement(sql);

				for (int i = 0; i < wordIds.size(); i++)
					statement.setInt(i + 1, wordIds.get(i));
				if (bookId != null)
					statement.setInt(wordIds.size() + 1, bookId);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					WordLocation location = new WordLocation(word, rs.getInt("index"), rs.getInt("line"),
							rs.getInt("index_in_line"), rs.getInt("sentence"), rs.getInt("paragraph"));
					location.setBookId(rs.getInt("book_id"));
					wordLocations.add(location);
				}
				rs.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wordLocations;
	}

	public static String buildPreview(int bookId, int paragraph) {
		StringBuilder sb = new StringBuilder();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(SQL_PREVIEW);
			statement.setInt(1, bookId);
			statement.setInt(2, paragraph);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				sb.append(rs.getBoolean("is_quote_before") ? "\"" : "").append(rs.getString("value"))
						.append(rs.getBoolean("is_quote_after") ? "\"" : "");
				String punctuationMark = rs.getString("punctuation_mark");
				if (punctuationMark != null) sb.append(punctuationMark);
				sb.append(" ");
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * @param bookId - use null to count word appearances in all books
	 * @return map of pairs word:appearances in alphabetic order
	 */
	public static Map<String, Integer> getWordsAppearances(Integer bookId) {
		Map<String, Integer> wordMap = new LinkedHashMap<>();
		PreparedStatement statement = null;
		try {
			if (bookId == null)
				statement = connection.prepareStatement(SQL_FIND_WORDS_APPEARANCES);

			else {
				statement = connection.prepareStatement(SQL_FIND_WORDS_APPEARANCES_IN_BOOK);
				statement.setInt(1, bookId);
			}
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				wordMap.put(rs.getString("lowercase_value"), rs.getInt(2));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordMap;
	}


	/**
	 * @param bookIds - use null to count word appearances in all books
	 * @return map of top X pairs word:appearances starting with the most frequent
	 * all words are in lowercase
	 */
	public static Map<String, Integer> getTopWordsAppearances(List<Integer> bookIds, int limit) {
		Map<String, Integer> wordMap = new LinkedHashMap<>();
		PreparedStatement statement = null;
		try {
			if (bookIds == null || bookIds.isEmpty()) {
				statement = connection.prepareStatement(SQL_TOP_WORDS_APPEARANCES);
				statement.setInt(1, limit);
			} else {
				StringJoiner sj = new StringJoiner(",");
				for (int i = 0; i < bookIds.size(); i++) {
					sj.add("?");
				}
				String sql = String.format(SQL_TOP_WORDS_APPEARANCES_IN_BOOKS, sj.toString());
				statement = connection.prepareStatement(sql);
				for (int i = 1; i <= bookIds.size(); i++) {
					statement.setInt(i, bookIds.get(i - 1));
				}
				statement.setInt(bookIds.size() + 1, limit);
			}

			System.out.println(statement.toString());

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				wordMap.put(rs.getString("lowercase_value"), rs.getInt("word_counter"));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordMap;
	}

	/**
	 * @return word in location and book specified, or null if location is not valid
	 */
	public static String findWordByLocation(int bookId, int line, int indexInLine) {
		PreparedStatement statement = null;
		String word = null;
		try {
			statement = connection.prepareStatement(SQL_FIND_WORD_BY_LOCATION);
			statement.setInt(1, bookId);
			statement.setInt(2, line);
			statement.setInt(3, indexInLine);
			ResultSet rs = statement.executeQuery();

			while (rs.next())
				word = rs.getString("value");
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return word;
	}

	public static List<WordLocation> getWordLocationList(String sql) {
		List<WordLocation> wordLocationsAll = new ArrayList<>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				WordLocation location = new WordLocation(rs.getInt("word_id"), rs.getInt("index"), rs.getInt("line"),
						rs.getInt("index_in_line"), rs.getInt("sentence"), rs.getInt("paragraph"));
				location.setBookId(rs.getInt("book_id"));
				wordLocationsAll.add(location);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordLocationsAll;
	}

	public static void addWords(List<Word> words) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD);
			for (Word word : words) {
				statement.setString(1, word.getValue());
				statement.addBatch();
				statement.clearParameters();
			}
			int[] results = statement.executeBatch();
			System.out.println("Loaded " + results.length + " words");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
