package org.nlp.match.TokenFilter;

import java.util.ArrayList;
import java.util.TreeMap;
import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.Sentenizer.Utility.Token;

/**
 * Implements TokenFilterInterface.
 * Returns all words with length >= minimum length
 * If minLength is not set by constructor or setter, it will be 4 by default
 * @author Benedict Preßler
 */
public class WordLengthFilter implements TokenFilterInterface{
    private int minLength;
    
    public WordLengthFilter(){
        this.minLength = 4;
    }
    
    public WordLengthFilter(int minLength){
        this.minLength = minLength;
    }
    
    @Override
    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList) {
        ArrayList<Token> tokenList = new ArrayList<>();
        
        for(int i = 1;i <= sentenceList.size(); i++){
            Sentence sentence = sentenceList.get(i);
            for(Token token:sentence.getTokens()){
                if(token.getTokenName().length()>=minLength){
                    tokenList.add(token);
                }
            }
        }
        
        return tokenList;
    }
    
    public void setMinLength(int minLength){
        this.minLength = minLength;
    }
}
