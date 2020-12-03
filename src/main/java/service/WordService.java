package service;

import model.WordLocation;

import java.sql.*;
import java.util.*;

public class WordService {
	private final static Connection connection = DbConnection.getInstance().getConnection();

	private final static String SQL_INSERT_WORD = "INSERT INTO word (value) VALUES (?)";
	private final static String SQL_SELECT_ALL_WORDS = "SELECT value,word_id from word";
	private final static String SQL_SELECT_BY_VALUE = "SELECT word_id from word WHERE value = ?";
	private final static String SQL_INSERT_WORD_LOCATION = "INSERT INTO word_in_book " +
			"(word_id,book_id,index,line,index_in_line,sentence,paragraph,is_quote_before,is_quote_after,punctuation_mark)" +
			" VALUES (?,?,?,?,?,?,?,?,?,?)";

	private final static String SQL_FIND_LOCATIONS_BY_WORD = "SELECT * from word_in_book where word_id = ? ORDER by book_id, index";
	private final static String SQL_FIND_LOCATIONS_BY_WORD_AND_BOOK = "SELECT * from word_in_book where word_id = ? AND book_id = ? ORDER by index";
	private final static String SQL_FIND_WORD_BY_LOCATION = "SELECT value from word_in_book, word where where word.word_id = word_in_book.word_id AND book_id = ? AND line = ? AND index_in_line = ?";
	private final static String SQL_PREVIEW = "SELECT value, is_quote_before, is_quote_after, punctuation_mark FROM word, word_in_book where book_id = ? AND paragraph = ? AND word.word_id = word_in_book.word_id ORDER by index";

	private final static String SQL_FIND_WORDS_APPEARANCES_IN_BOOK = "SELECT DISTINCT value, COUNT(value) from word, word_in_book where book_id = ? AND word.word_id = word_in_book.word_id GROUP BY value ORDER by word";
	private final static String SQL_TOP_WORDS_APPEARANCES_IN_BOOK = "SELECT DISTINCT value, COUNT(value) from word, word_in_book where book_id = ? AND word.word_id = word_in_book.word_id GROUP BY value ORDER by COUNT(value) DESC LIMIT ?";
	private final static String SQL_FIND_WORDS_APPEARANCES = "SELECT DISTINCT value, COUNT(value) from word, word_in_book where word.word_id = word_in_book.word_id GROUP BY value ORDER by word";
	private final static String SQL_TOP_WORDS_APPEARANCES = "SELECT DISTINCT value, COUNT(value) from word, word_in_book where word.word_id = word_in_book.word_id GROUP BY value ORDER by COUNT(value) DESC LIMIT ?";


	public static long insertWord(String word) {
		long id = findWordIdByValue(word);
		if (id > 0) return id;
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, word);

//			System.out.println(statement.toString());

			int affectedRows = statement.executeUpdate();

			if (affectedRows != 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						id = generatedKeys.getLong(1);
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

	public static long findWordIdByValue(String word) {
		PreparedStatement statement = null;
		long id = -1;
		try {
			statement = connection.prepareStatement(SQL_SELECT_BY_VALUE);
			statement.setString(1, word);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				id = rs.getLong("word_id");
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static Map<String, Long> getAllWordsId() {
		Map<String, Long> words = new HashMap<>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL_WORDS);
			while (rs.next())
				words.put(rs.getString(1), rs.getLong(2));
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return words;
	}

	public static void addWordLocationList(List<WordLocation> wordLocationList, long bookId) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD_LOCATION);
			for (WordLocation wordLocation : wordLocationList) {
				statement.setLong(1, wordLocation.getWordId());
				statement.setLong(2, bookId);
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

	public static List<WordLocation> findWordInBooks(String word, Long bookId) {
		List<WordLocation> wordLocations = new LinkedList<>();
		Long wordId = FilesManager.getInstance().getWordId(word);
		if (wordId != null) {
			PreparedStatement statement = null;
			try {
				if (bookId == null)
					statement = connection.prepareStatement(SQL_FIND_LOCATIONS_BY_WORD);

				else {
					statement = connection.prepareStatement(SQL_FIND_LOCATIONS_BY_WORD_AND_BOOK);
					statement.setLong(2, bookId);
				}
				statement.setLong(1, wordId);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					WordLocation location = new WordLocation(word, rs.getInt("index"), rs.getInt("line"),
							rs.getInt("index_in_line"), rs.getInt("sentence"), rs.getInt("paragraph"));
					location.setBookId(rs.getLong("book_id"));
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

	public static String buildPreview(long bookId, int paragraph) {
		StringBuilder sb = new StringBuilder();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(SQL_PREVIEW);
			statement.setLong(1, bookId);
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
	public static Map<String, Integer> getWordsAppearances(Long bookId) {
		Map<String, Integer> wordMap = new LinkedHashMap<>();
		PreparedStatement statement = null;
		try {
			if (bookId == null)
				statement = connection.prepareStatement(SQL_FIND_WORDS_APPEARANCES);

			else {
				statement = connection.prepareStatement(SQL_FIND_WORDS_APPEARANCES_IN_BOOK);
				statement.setLong(1, bookId);
			}
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				wordMap.put(rs.getString("value"), rs.getInt(2));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordMap;
	}


	/**
	 * @param bookId - use null to count word appearances in all books
	 * @return map of top X pairs word:appearances starting with the most frequent
	 */
	public static Map<String, Integer> getTopWordsAppearances(Long bookId, int limit) {
		Map<String, Integer> wordMap = new LinkedHashMap<>();
		PreparedStatement statement = null;
		try {
			if (bookId == null) {
				statement = connection.prepareStatement(SQL_TOP_WORDS_APPEARANCES);
				statement.setInt(1, limit);
			} else {
				statement = connection.prepareStatement(SQL_TOP_WORDS_APPEARANCES_IN_BOOK);
				statement.setLong(1, bookId);
				statement.setInt(2, limit);
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				wordMap.put(rs.getString("value"), rs.getInt(2));
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
	public static String findWordByLocation(long bookId, int line, int indexInLine) {
		PreparedStatement statement = null;
		String word = null;
		try {
			statement = connection.prepareStatement(SQL_FIND_WORD_BY_LOCATION);
			statement.setLong(1, bookId);
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
}
