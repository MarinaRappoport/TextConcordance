package model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String name;
	private List<String> words;

	public Group(String name) {
		this.name = name;
		words = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public List<String> getWords() {
		return words;
	}

	@Override
	public boolean equals(Object obj) {
		Group group = (Group) obj;
		return ( name.equals(group.name));
	}

    public void addWord(String word) {
		words.add(word);
    }
}
