package service;

import model.WordLocation;

import java.sql.*;
import java.util.*;

public class PhraseService {

	private final static String SQL_CREATE_NEW_PHRASE = "INSERT INTO phrase DEFAULT VALUES";
	private final static String SQL_SAVE_NEW_PHRASE = "INSERT INTO word_in_phrase (phrase_id, word_id, index_in_phrase) VALUES (?,?,?)";
	private final static String SQL_FIND_ALL_PHRASES = "SELECT phrase_id, value, index_in_phrase from word_in_phrase, word where word.word_id = word_in_phrase.word_id ORDER by phrase_id, index_in_phrase";
	private final static String SQL_FIND_WORDS_IN_PHRASES = "SELECT word_id from word_in_phrase where phrase_id = ? ORDER by index_in_phrase";

	private final static String SQL_FIND_POTENTIAL_PHRASES = "SELECT * FROM word_in_book WHERE(book_id,sentence) IN (" +
			"SELECT book_id,sentence FROM (SELECT book_id,sentence, COUNT(distinct word_id) w_count " +
			"FROM word_in_book WHERE word_id IN (%s) GROUP BY book_id,sentence) AS docs_sentences_with_words " +
			"WHERE docs_sentences_with_words.w_count = %d) AND word_id IN (%s) ORDER BY book_id, sentence, index";

	private final static String SQL_FIND_POTENTIAL_PHRASES_IN_BOOK = "SELECT * FROM word_in_book" +
			" WHERE(sentence) IN (SELECT sentence FROM (SELECT sentence, COUNT(distinct word_id) w_count " +
			"FROM word_in_book WHERE word_id IN (%s) and book_id = %d GROUP BY sentence) " +
			"AS docs_sentences_with_words WHERE docs_sentences_with_words.w_count = %d) " +
			"AND word_id IN (%s) ORDER BY sentence, index";

	private final static Connection connection = DbConnection.getInstance().getConnection();

	private static int createNewPhrase() {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_CREATE_NEW_PHRASE,
					Statement.RETURN_GENERATED_KEYS);
			int affectedRows = statement.executeUpdate();
			if (affectedRows != 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int id = generatedKeys.getInt(1);
						statement.close();
						return id;
					} else {
						throw new SQLException("Creating phrase failed, no ID obtained.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	//TODO and check if phrase already exists

	/**
	 * Save new phrase in DB
	 *
	 * @param phrase text
	 * @return phrase id
	 */
	public static int saveNewPhrase(String phrase) {
		int id = createNewPhrase();
		String[] words = phrase.split(" ");
		int ordinal = 1;
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_SAVE_NEW_PHRASE);
			for (String word : words) {
				Integer wordId = FilesManager.getInstance().getWordId(word);
				if (wordId == null) {
					wordId = WordService.insertWord(word);
					if (wordId == -1) return -1;
				}

				statement.setInt(1, id);
				statement.setInt(2, wordId);
				statement.setInt(3, ordinal++);
				statement.addBatch();
				statement.clearParameters();
			}
			statement.executeBatch();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}


	/**
	 * @return map of phrase id and phrase text
	 */
	public static Map<Integer, String> getAllPhrases() {
		Map<Integer, String> phrases = new LinkedHashMap<>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_FIND_ALL_PHRASES);
			while (rs.next()) {
				int phraseId = rs.getInt("phrase_id");
				String word = rs.getString("value");
				String phrase = phrases.get(phraseId);
				if (phrase == null) phrase = word;
				else phrase = phrase + " " + word;
				phrases.put(phraseId, phrase);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return phrases;
	}

	private static List<Integer> getWordsInPhrase(int phraseId) {
		List<Integer> wordList = new ArrayList<>();
		PreparedStatement statement = null;
		long id = -1;
		try {
			statement = connection.prepareStatement(SQL_FIND_WORDS_IN_PHRASES);
			statement.setInt(1, phraseId);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				wordList.add(rs.getInt("word_id"));
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordList;
	}

	/**
	 * @param phraseId phraseId to search
	 * @param bookId   use null to search in all books
	 * @return location of the first word in phrase
	 */
	public static List<WordLocation> findPhraseInBooks(Integer phraseId, Integer bookId) {
		List<WordLocation> wordLocationsAll = new LinkedList<>();
		List<Integer> wordList = getWordsInPhrase(phraseId);
		StringJoiner joiner = new StringJoiner(",");
		for (Integer id : wordList) {
			joiner.add(String.valueOf(id));
		}
		String wordIds = joiner.toString();
		String sql;
		if (bookId == null)
			sql = String.format(SQL_FIND_POTENTIAL_PHRASES, wordIds, wordList.size(), wordIds);
		else
			sql = String.format(SQL_FIND_POTENTIAL_PHRASES_IN_BOOK, wordIds, bookId, wordList.size(), wordIds);

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

		List<WordLocation> listFiltered = new ArrayList<>();

		//check that order of the words and filter only correct locations
		int y = 0;
		int index = 0;
		for (int i = 0; i < wordLocationsAll.size(); i++) {
			if (wordLocationsAll.get(i).getWordId() == wordList.get(y)
					&& (index == 0 || wordLocationsAll.get(i).getIndex() == index + 1)) {
				y++;
				index = wordLocationsAll.get(i).getIndex();
			} else {
				i++;
				y = 0;
				index = 0;
			}
			if (y == wordList.size()) {
				listFiltered.add(wordLocationsAll.get(i + 1 - y));
				y = 0;
				index = 0;
			}
		}
		return listFiltered;
	}
}
