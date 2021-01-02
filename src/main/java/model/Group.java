package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Represents table 'groups'
 */
public class Group {
	@JacksonXmlProperty(isAttribute = true)
	private int id;
	private String name;

	/**
	 * Main Constructor
	 */
	public Group(String name, int id) {
		this.name = name;
		this.id = id;
	}

	//empty constructor for XML parsing
	public Group() {
	}

	//getters & setters
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

	@Override
	public boolean equals(Object obj) {
		Group group = (Group) obj;
		return (name.equals(group.name));
	}
}
