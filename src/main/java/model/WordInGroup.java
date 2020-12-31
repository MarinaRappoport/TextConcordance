package model;

public class WordInGroup {
	private int wordId;
	private int groupId;

	public WordInGroup() {
	}

	public WordInGroup(int wordId, int groupId) {
		this.wordId = wordId;
		this.groupId = groupId;
	}

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
