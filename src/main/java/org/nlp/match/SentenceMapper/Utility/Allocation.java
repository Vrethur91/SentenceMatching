package org.nlp.match.SentenceMapper.Utility;

import java.util.HashMap;
import java.util.Map;
import org.nlp.match.TokenMapper.Utility.TokenPair;

/**
 *
 * @author Bene
 */
public class Allocation {
    private int sentenceID1;
    private int sentenceID2;
    private HashMap<String, TokenPair> tokenPairList;
    private double avgTokenSimilarityRating;
    private double tokenSilimarityVariance;
    private double allocationRating;
    
    public Allocation(){
        this.tokenPairList = new HashMap<>();
    }
    /**
     * @return the sentenceID1
     */
    public int getSentenceID1() {
        return sentenceID1;
    }

    /**
     * @param sentenceID1 the sentenceID1 to set
     */
    public void setSentenceID1(int sentenceID1) {
        this.sentenceID1 = sentenceID1;
    }

    public int getSentenceID2() {
        return sentenceID2;
    }


    public void setSentenceID2(int sentenceID2) {
        this.sentenceID2 = sentenceID2;
    }

    public HashMap<String, TokenPair> getTokenPairList() {
        return tokenPairList;
    }

    public void setTokenPairList(HashMap<String, TokenPair> tokenPairList) {
        this.tokenPairList = tokenPairList;
    }
    
    public void addTokenPair(TokenPair tokenPair) {
        String key = tokenPair.getToken1().getTokenName() + "." + +tokenPair.getToken1().getWordPosition();
        if(tokenPairList.containsKey(key)){
            if(tokenPair.getSimilarity() > tokenPairList.get(key).getSimilarity()){
                tokenPairList.put(key, tokenPair);
                computeAvgRating();
                computeVariance();
            }
        }
        else{
            tokenPairList.put(key, tokenPair);
            computeAvgRating();
            computeVariance();
        }
        
    }
    
    public double getAvgTokenSimilarityRating(){
        return avgTokenSimilarityRating;
    }
    
    public double getTokenSimilarityRatingVariance(){
        return tokenSilimarityVariance;
    }

    
    public double getAllocationRating() {
        return allocationRating;
    }

    public void setAllocationRating(double rating) {
        this.allocationRating = rating;
    }
    
    private void computeAvgRating(){
        double tempAvgRating = 0.0;
        for(Map.Entry entry: tokenPairList.entrySet()){
            TokenPair tokenPair = (TokenPair) entry.getValue();
            tempAvgRating += tokenPair.getSimilarity();
        }
        avgTokenSimilarityRating = tempAvgRating / (double) tokenPairList.size();
    }
    
    private void computeVariance(){
        double tempVariance = 0.0;
        for(Map.Entry entry: tokenPairList.entrySet()){
            TokenPair tokenPair = (TokenPair) entry.getValue();
            tempVariance += Math.pow(tokenPair.getSimilarity() - avgTokenSimilarityRating, 2);
        }
        tokenSilimarityVariance = tempVariance;
    }
}
