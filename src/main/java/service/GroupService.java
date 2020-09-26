package service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GroupService {

	private final static Connection connection = DbConnection.getInstance().getConnection();

	public long createNewGroup(String name){
		return -1;
	}

	public void addWordToGroup(String word, int groupId){

	}

	public List<String> getAllWordsForGroup(int groupId){
		List<String> words = new ArrayList<>();
		return words;
	}
}
