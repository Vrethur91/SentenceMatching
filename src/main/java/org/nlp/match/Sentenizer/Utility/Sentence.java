package org.nlp.match.Sentenizer.Utility;

import org.nlp.match.Sentenizer.Utility.Token;
import java.util.ArrayList;

/**
 *
 * @author Bene
 */
public class Sentence {
    private int sentenceNumber;
    private String fulltext;
    private String language;
    private ArrayList<Token> tokens;
    private String additionalNumber;

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
     * @return the fulltext
     */
    public String getFulltext() {
        return fulltext;
    }

    /**
     * @param fulltext the fulltext to set
     */
    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    /**
     * @return the tokens
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
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
     * @return the additionalNumber
     */
    public String getAdditionalNumber() {
        return additionalNumber;
    }

    /**
     * @param additionalNumber the additionalNumber to set
     */
    public void setAdditionalNumber(String additionalNumber) {
        this.additionalNumber = additionalNumber;
    }
}
