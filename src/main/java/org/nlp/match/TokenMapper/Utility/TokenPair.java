package org.nlp.match.TokenMapper.Utility;

import org.nlp.match.Sentenizer.Utility.Token;

/**
 *
 * @author Bene
 */
public class TokenPair {
    
    private Token token1;
    private Token token2;
    private double similarity;

    /**
     * @return the token1
     */
    public Token getToken1() {
        return token1;
    }

    /**
     * @param token1 the token1 to set
     */
    public void setToken1(Token token1) {
        this.token1 = token1;
    }

    /**
     * @return the token2
     */
    public Token getToken2() {
        return token2;
    }

    /**
     * @param token2 the token2 to set
     */
    public void setToken2(Token token2) {
        this.token2 = token2;
    }

    /**
     * @return the similarity
     */
    public double getSimilarity() {
        return similarity;
    }

    /**
     * @param similarity the similarity to set
     */
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

}
