package model;

import java.util.List;

public class Group {
	private String name;
	private List<String> words;

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<String> getWords() {
		return words;
	}
}
