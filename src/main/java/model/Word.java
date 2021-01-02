package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Represents table 'word'
 */
public class Word {
	@JacksonXmlProperty(isAttribute = true)
	private int id;
	private String value;

	//empty constructor for XML parsing
	public Word() {
	}

	/**
	 * Main Constructor
	 */
	public Word(int id, String value) {
		this.id = id;
		this.value = value;
	}

	//getters & setters
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
