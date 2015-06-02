package org.nlp.match.Tools;

import org.nlp.match.Sentenizer.Utility.Token;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Bene
 */
public class Analyzer {
    public static HashMap<String, Integer> countWordOccurence(ArrayList<Token> tokenList){
        HashMap<String, Integer> occurenceMap = new HashMap<>();
        
        for(Token token:tokenList){
            String tokenName = token.getTokenName();
            if(occurenceMap.containsKey(tokenName)){
                occurenceMap.put(tokenName, occurenceMap.get(tokenName)+1);
            }else{
                occurenceMap.put(tokenName, 1);
            }
        }
        
        return sortMapByValue(occurenceMap);
    }
    
    private static <K, V extends Comparable<? super V>> HashMap<K, V> sortMapByValue(HashMap<K, V> map) {
        HashMap<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();
        
        st.sorted(Comparator.comparing(e -> e.getValue()))
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        
        return result;
    }
    
}
