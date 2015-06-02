package org.nlp.match.Tools;

import org.nlp.match.General.DataHolder;
import org.nlp.match.Sentenizer.Utility.Token;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.nlp.match.SentenceMapper.Utility.Allocation;

/**
 *
 * @author Bene
 */
public class Tweaker {

    public static TreeMap<Integer, Allocation> removeFalseBibleMatches(TreeMap<Integer, Allocation> sentenceMap){
        TreeMap<Integer, Allocation> resultMap = new TreeMap<>();
        for (Entry entry : sentenceMap.entrySet()) {
            int key1 = (int) entry.getKey();
            int key2 = ((Allocation)entry.getValue()).getSentenceID2();
            if(key1==key2){
                resultMap.put(key1, (Allocation)entry.getValue());
            }
        }
        
        return resultMap;
    }
    
    public static void filterResults(DataHolder holder, double diff) {
        TreeMap<Integer, Allocation> sentenceMap = holder.getSentenceMap();
        TreeMap<Integer, Allocation> resultMap = new TreeMap<>();
        SimpleRegression regression = computeRegression(sentenceMap);
        double meanTextLength = (holder.getSentenceList1().size() + holder.getSentenceList2().size()) / 2.0;
        for (Entry entry : sentenceMap.entrySet()) {
            int key1 = (int)entry.getKey();
            Allocation allocation = (Allocation) entry.getValue();
            double regValue = regression.predict(key1);
            int key2 = allocation.getSentenceID2();
            double diffKey = Math.abs(regValue - key2);
            if (diffKey / meanTextLength <= diff) {
                resultMap.put(key1, allocation);
            }
        }
        
        System.out.println(">> " + (sentenceMap.size()-resultMap.size()) + " Matches removed");

        holder.setSentenceMap(resultMap);
    }

    public static ArrayList<Token> removeTopOccurences(ArrayList<Token> tokenList, double percentage){
        if(percentage>1.0||percentage<0.0){
            System.out.println("Wrong decimal value for top occurrence filter: "+percentage);
            return tokenList;
        }
        
        ArrayList<Token> returnList = new ArrayList<>();
        Map<String, Integer> occMap = Analyzer.countWordOccurence(tokenList);
        HashSet<String> relevantNames = new HashSet<>();
        int highestRelevantIndex = (int) (occMap.size() * ( 1.0 - percentage));
        
        int indexCount = 0;
        for(Entry entry:occMap.entrySet()){
            if(indexCount<highestRelevantIndex){
                relevantNames.add((String)entry.getKey());
            }else{
                break;
            }
            indexCount++;
        }
        
        for(Token token:tokenList){
            if(relevantNames.contains(token.getTokenName())){
                returnList.add(token);
            }
        }
        
        System.out.println(">> " + (tokenList.size()-returnList.size()) + " Token removed");
        return returnList;
    }
    
    private static SimpleRegression computeRegression(TreeMap<Integer, Allocation> windowSentenceMap) {
        SimpleRegression regression = new SimpleRegression();

        for (Entry entry : windowSentenceMap.entrySet()) {
            regression.addData((int) entry.getKey(), ((Allocation) entry.getValue()).getSentenceID2());
        }

        return regression;
    }
}
