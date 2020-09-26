package service;

import model.WordLocation;

import java.sql.*;

public class WordService {
	private final static Connection connection = DbConnection.getInstance().getConnection();

	private final static String SQL_INSERT_WORD = "INSERT INTO word (value) VALUES (?)";
	private final static String SQL_SELECT_BY_VALUE = "SELECT word_id from word WHERE value = ?";
	private final static String SQL_INSERT_WORD_LOCATION = "INSERT INTO word_in_book " +
			"(word_id,book_id,index,line,index_in_line,sentence,paragraph) VALUES (?,?,?,?,?,?,?)";

	public static long insertWord(String word) {
		long id = findWordByValue(word);
		if (id > 0) return id;
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, word);

			System.out.println(statement.toString());

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

	public static long findWordByValue(String word) {
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

	public static void addWordPosition(WordLocation wordLocation) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD_LOCATION);
			statement.setLong(1, wordLocation.getWordId());
			statement.setInt(2, wordLocation.getBookId());
			statement.setInt(3, wordLocation.getIndex());
			statement.setInt(4, wordLocation.getLine());
			statement.setInt(5, wordLocation.getIndexInLine());
			statement.setInt(6, wordLocation.getSentence());
			statement.setInt(7, wordLocation.getParagraph());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
