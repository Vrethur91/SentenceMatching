package org.nlp.match.TokenMapper;

import org.nlp.match.TokenMapper.Utility.TokenPair;
import org.nlp.match.Sentenizer.Utility.Token;
import java.util.ArrayList;
import java.util.HashMap;
import org.nlp.match.Levenshtein.*;

/**
 *
 * @author Bene
 */
public class LevenshteinTokenMapper implements TokenMapperInterface{

    private LevenshteinDistance levenshteinDistance;
    private double threshold = 0.6;

    public LevenshteinTokenMapper(LevenshteinDistance levenshteinDistance) {
        this.levenshteinDistance = levenshteinDistance;
    }
    
    public LevenshteinTokenMapper(){
        this.levenshteinDistance = new LevenshteinDistance();
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public ArrayList<TokenPair> measureTokenSimilarity(ArrayList<Token> list1, ArrayList<Token> list2, boolean inLanguage) {
        //LevenshteinDistance ld = new LevenshteinDistance();
        ArrayList<TokenPair> resultMap = new ArrayList<>();
        
        compareTokenLists(list1, list2, resultMap);

        if(inLanguage){
            compareTokenLists(list1, list1, resultMap);
            compareTokenLists(list2, list2, resultMap);
        }
        
        return resultMap;
    }
    
    private void compareTokenLists(ArrayList<Token> list1, ArrayList<Token> list2,
            ArrayList<TokenPair> resultMap) {

        HashMap<String, Double> matchStore = new HashMap<>();
        for(Token token1: list1){
            for(Token token2: list2){
                if (!(token1.getTokenName() + "-" + token1.getLanguage()).equals(token2.getTokenName() + "-" + token2.getLanguage())) {
                    double similarity;
                    if (matchStore.containsKey(token1.getTokenName() + "-" + token1.getLanguage() + "-" + token2.getTokenName() + "-" + token2.getLanguage())) {
                        similarity = matchStore.get(token1.getTokenName() + "-" + token1.getLanguage() + "-" + token2.getTokenName() + "-" + token2.getLanguage());
                    }else{
                        similarity = levenshteinDistance.levenshteinDistance(token1.getTokenName(), token2.getTokenName());
                        matchStore.put(token1.getTokenName() + "-" + token1.getLanguage() + "-" + token2.getTokenName() + "-" + token2.getLanguage(), similarity);
                    }

                    if (similarity >= threshold) {
                        TokenPair pair = new TokenPair();
                        pair.setToken1(token1);
                        pair.setToken2(token2);
                        pair.setSimilarity(similarity);
                        resultMap.add(pair);
                    }

                }
            }
        }
    }

}
