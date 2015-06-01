package org.nlp.match.Sentenizer;

/**
 *
 * @author Bene
 */
public class Token {
    private String tokenName;
    private String baseName;
    private int sentenceNumber;
    private int wordPosition;
    private String language;

    /**
     * @return the tokenName
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * @param tokenName the tokenName to set
     */
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * @return the sentenceNumber
     */
    public int getSentenceNumber() {
        return sentenceNumber;
    }

    /**
     * @param sentenceNumber the sentenceNumber to set
     */
    public void setSentenceNumber(int sentenceNumber) {
        this.sentenceNumber = sentenceNumber;
    }

    /**
     * @return the wordPosition
     */
    public int getWordPosition() {
        return wordPosition;
    }

    /**
     * @param wordPosition the wordPosition to set
     */
    public void setWordPosition(int wordPosition) {
        this.wordPosition = wordPosition;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the baseName
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * @param baseName the baseName to set
     */
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }


}
