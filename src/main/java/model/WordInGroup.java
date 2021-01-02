package model;

/**
 * Represents table 'word_in_group'
 */
public class WordInGroup {
	private int wordId;
	private int groupId;

	//empty constructor for XML parsing
	public WordInGroup() {
	}

	/**
	 * Main Constructor
	 */
	public WordInGroup(int wordId, int groupId) {
		this.wordId = wordId;
		this.groupId = groupId;
	}

	//getters & setters
	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
