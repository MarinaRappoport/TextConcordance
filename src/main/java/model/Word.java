package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Word {
	@JacksonXmlProperty(isAttribute = true)
	private int id;
	private String value;

	//empty constructor for XML parsing
	public Word() {
	}

	public Word(int id, String value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
