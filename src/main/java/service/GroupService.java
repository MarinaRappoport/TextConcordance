package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupService {

	private final static String SQL_CREATE_NEW_GROUP = "INSERT INTO groups (group_name) VALUES (?)";
	private final static String SQL_FIND_ALL_GROUPS = "SELECT * from groups ORDER by group_name";
	private final static String SQL_INSERT_WORD_IN_GROUP = "INSERT INTO word_in_group (word_id,group_id) VALUES (?,?)";
	private final static String SQL_FIND_WORDS_IN_GROUP = "SELECT value from word, word_in_group where group_id = ? AND word.word_id = word_in_group.word_id ORDER by word";

	private final static Connection connection = DbConnection.getInstance().getConnection();

	public static int createNewGroup(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_CREATE_NEW_GROUP,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, name);

			int affectedRows = statement.executeUpdate();

			if (affectedRows != 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int id = generatedKeys.getInt(1);
						statement.close();
						return id;
					} else {
						throw new SQLException("Creating group failed, no ID obtained.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static Map<String, Integer> getAllGroups() {
		Map<String, Integer> groups = new LinkedHashMap<>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_FIND_ALL_GROUPS);
			while (rs.next())
				groups.put(rs.getString("group_name"), rs.getInt("group_id"));
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groups;
	}

	public static void addWordToGroup(String word, int groupId) {
		Long wordId = FilesManager.getInstance().getWordId(word);
		if (wordId != null)
			wordId = WordService.insertWord(word);
		if (wordId > 0) {
			try {
				PreparedStatement statement = connection.prepareStatement(SQL_INSERT_WORD_IN_GROUP);
				statement.setLong(1, wordId);
				statement.setInt(2, groupId);
				statement.executeUpdate();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> getAllWordsForGroup(int groupId) {
		List<String> words = new ArrayList<>();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_FIND_WORDS_IN_GROUP);
			statement.setInt(1, groupId);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				words.add(rs.getString("value"));
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return words;
	}
}
