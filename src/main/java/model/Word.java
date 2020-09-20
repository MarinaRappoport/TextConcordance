package model;

public class Word {
	private long id;
    private String value;

    public Word(String value, int line, int paragraph){
        this.value = value;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	@Override
    public boolean equals(Object obj) {
        Word other = (Word)obj;
        return value.equals(other.value);
    }
}
