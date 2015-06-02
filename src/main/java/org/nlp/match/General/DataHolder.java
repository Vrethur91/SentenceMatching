package org.nlp.match.General;

import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.Sentenizer.Utility.Token;
import org.nlp.match.Sentenizer.*;
import java.util.ArrayList;
import java.util.TreeMap;
import org.nlp.match.SentenceMapper.Utility.Allocation;
import org.nlp.match.TokenMapper.Utility.TokenPair;

/**
 *
 * @author Bene
 */
public class DataHolder {
    private TreeMap<Integer, Sentence> sentenceList1;
    private TreeMap<Integer, Sentence> sentenceList2;
    private String language1;
    private String language2;
    private ArrayList<Token> tokenList1;
    private ArrayList<Token> tokenList2;
    private ArrayList<TokenPair> similarityList;
    private TreeMap<Integer, Allocation> sentenceMap;

    /**
     * @return the sentenceList1
     */
    public TreeMap<Integer, Sentence> getSentenceList1() {
        return sentenceList1;
    }

    /**
     * @param sentenceList1 the sentenceList1 to set
     */
    public void setSentenceList1(TreeMap<Integer, Sentence> sentenceList1) {
        this.sentenceList1 = sentenceList1;
    }

    /**
     * @return the sentenceList2
     */
    public TreeMap<Integer, Sentence> getSentenceList2() {
        return sentenceList2;
    }

    /**
     * @param sentenceList2 the sentenceList2 to set
     */
    public void setSentenceList2(TreeMap<Integer, Sentence> sentenceList2) {
        this.sentenceList2 = sentenceList2;
    }

    /**
     * @return the tokenList1
     */
    public ArrayList<Token> getTokenList1() {
        return tokenList1;
    }

    /**
     * @param tokenList1 the tokenList1 to set
     */
    public void setTokenList1(ArrayList<Token> tokenList1) {
        this.tokenList1 = tokenList1;
    }

    /**
     * @return the tokenList2
     */
    public ArrayList<Token> getTokenList2() {
        return tokenList2;
    }

    /**
     * @param tokenList2 the tokenList2 to set
     */
    public void setTokenList2(ArrayList<Token> tokenList2) {
        this.tokenList2 = tokenList2;
    }

    /**
     * @return the similarityList
     */
    public ArrayList<TokenPair> getSimilarityList() {
        return similarityList;
    }

    /**
     * @param similarityList the similarityMap to set
     */
    public void setSimilarityList(ArrayList<TokenPair> similarityList) {
        this.similarityList = similarityList;
    }

    /**
     * @return the sentenceMap
     */
    public TreeMap<Integer, Allocation> getSentenceMap() {
        return sentenceMap;
    }

    /**
     * @param sentenceMap the sentenceMap to set
     */
    public void setSentenceMap(TreeMap<Integer, Allocation> sentenceMap) {
        this.sentenceMap = sentenceMap;
    }

    /**
     * @return the language1
     */
    public String getLanguage1() {
        return language1;
    }

    /**
     * @param language1 the language1 to set
     */
    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    /**
     * @return the language2
     */
    public String getLanguage2() {
        return language2;
    }

    /**
     * @param language2 the language2 to set
     */
    public void setLanguage2(String language2) {
        this.language2 = language2;
    }
}
