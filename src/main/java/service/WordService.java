package service;

import model.Word;

import java.sql.*;

public class WordService {
	private final static Connection connection = DbConnection.getInstance().getConnection();

	private final static String SQL_INSERT = "INSERT INTO word (value) VALUES (?)";
	private final static String SQL_SELECT_BY_VALUE = "SELECT word_id from word WHERE value = ?";

	public static long insertWord(String word) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, word);

			System.out.println(statement.toString());

			int affectedRows = statement.executeUpdate();

			if (affectedRows != 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						long id = generatedKeys.getLong(1);
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
}
