package model;

public class WordInPhrase {
	private int wordId;
	private int phraseId;
	private int indexInPhrase;


	public WordInPhrase(int wordId, int phraseId, int indexInPhrase) {
		this.wordId = wordId;
		this.phraseId = phraseId;
		this.indexInPhrase = indexInPhrase;
	}

	public WordInPhrase() {
	}

	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public int getPhraseId() {
		return phraseId;
	}

	public void setPhraseId(int phraseId) {
		this.phraseId = phraseId;
	}

	public int getIndexInPhrase() {
		return indexInPhrase;
	}

	public void setIndexInPhrase(int indexInPhrase) {
		this.indexInPhrase = indexInPhrase;
	}
}
