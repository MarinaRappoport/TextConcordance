package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class Group {
	@JacksonXmlProperty(isAttribute = true)
	private int id;
	private String name;
	@JsonIgnore
	private List<String> words;

	public Group(String name) {
		this.name = name;
		words = new ArrayList<>();
	}

	public Group(String name, int id) {
		this.name = name;
		this.id = id;
	}

	//empty constructor for XML parsing
	public Group() {
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public List<String> getWords() {
		return words;
	}

	@Override
	public boolean equals(Object obj) {
		Group group = (Group) obj;
		return (name.equals(group.name));
	}

	public void addWord(String word) {
		words.add(word);
	}
}
