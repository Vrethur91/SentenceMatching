package org.nlp.match.SentenceMapper;

import org.nlp.match.SentenceMapper.Utility.AssociationMap;
import org.nlp.match.SentenceMapper.Utility.Allocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.nlp.match.TokenMapper.Utility.TokenPair;
import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.General.DataHolder;

/**
 *
 * @author Bene
 */
public class LinearSentenceMapper implements SentenceMapperInterface {

    private int maxRecDepth = 20;
    private TreeMap<Integer, Allocation> sentenceMap;
    private TreeMap<Integer, Sentence> sentenceList1;
    private TreeMap<Integer, Sentence> sentenceList2;
    private AssociationMap associationMap;
    private boolean swap;

    @Override
    public TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap) {

        sentenceMap = new TreeMap<>();
        if(swap){
            sentenceList1 = holder.getSentenceList2();
            sentenceList2 = holder.getSentenceList1();
        }else{
            sentenceList1 = holder.getSentenceList1();
            sentenceList2 = holder.getSentenceList2();
        }
        this.swap = swap;
        
        associationMap = createAssociationMap(holder.getSimilarityList());
        initialize();
        iterate(0);

        return sentenceMap;
    }

    public TreeMap<Integer, Allocation> mapSentences(DataHolder holder,boolean swap, int s1min, int s1max, int s2min, int s2max) {
        sentenceMap = new TreeMap<>();
        sentenceList1 = new TreeMap<>();
        if (swap) {
            sentenceList1 = new TreeMap<>();
            TreeMap<Integer, Sentence> tempSentenceList1 = holder.getSentenceList2();
            for (int i = s1min; i <= s1max; i++) {
                sentenceList1.put(i, tempSentenceList1.get(i));
            }

            sentenceList2 = new TreeMap<>();
            TreeMap<Integer, Sentence> tempSentenceList2 = holder.getSentenceList1();
            for (int j = s2min; j <= s2max; j++) {
                sentenceList2.put(j, tempSentenceList2.get(j));
            }
        } else {
            sentenceList1 = new TreeMap<>();
            TreeMap<Integer, Sentence> tempSentenceList1 = holder.getSentenceList1();
            for (int i = s1min; i <= s1max; i++) {
                sentenceList1.put(i, tempSentenceList1.get(i));
            }

            sentenceList2 = new TreeMap<>();
            TreeMap<Integer, Sentence> tempSentenceList2 = holder.getSentenceList2();
            for (int j = s2min; j <= s2max; j++) {
                sentenceList2.put(j, tempSentenceList2.get(j));
            }
        }
        
        this.swap = swap;

        associationMap = createAssociationMap(holder.getSimilarityList(), s1min, s1max, s2min, s2max);
        initialize();
        iterate(0);

        return sentenceMap;
    }

    private void initialize() {

        HashMap<Integer, HashSet<Integer>> keyMap = associationMap.getKeyMap();
        for (Map.Entry key1Entry : keyMap.entrySet()) {
            int key1 = (int) key1Entry.getKey();
            if(sentenceList1.get(key1) == null){
                System.out.println(key1);
            }
            int sentenceLength1 = sentenceList1.get(key1).getTokens().size();
            Allocation bestComparison = null;
            double bestCRating = 0.0;
            HashSet<Integer> keySet = (HashSet<Integer>) key1Entry.getValue();
            for (Integer key2 : keySet) {
                int sentenceLength2 = sentenceList2.get(key2).getTokens().size();
                Allocation comparison = associationMap.get(key1, key2);
                int diffPosition = Math.abs(key1 - key2);
                int diffLength = Math.abs(sentenceLength1 - sentenceLength2);
                double avgRating = comparison.getAvgTokenSimilarityRating();
                double comparisonRating = avgRating * comparison.getTokenPairList().size() / (Math.sqrt(diffLength) + Math.pow(diffPosition, 2) + 1);

                if (comparisonRating < 0.01) {
                    if (keySet.size() == 1) {
                        continue;
                    }
                }

                if (comparisonRating > bestCRating) {
                    comparison.setAllocationRating(comparisonRating);
                    bestComparison = comparison;
                    bestCRating = comparisonRating;
                }
            }
            if (bestComparison != null) {
                sentenceMap.put(key1, bestComparison);
            }
        }

    }

    private void iterate(int recDepth) {
        HashMap<Integer, HashSet<Integer>> keyMap = associationMap.getKeyMap();
        Map.Entry nextTreeMapEntry = sentenceMap.firstEntry();
        while (nextTreeMapEntry != null) {
            int key1 = (int) nextTreeMapEntry.getKey();
            int sentenceLength1 = sentenceList1.get(key1).getTokens().size();
            Allocation bestComparison = null;
            double bestCRating = 0.0;
            HashSet<Integer> keySet = (HashSet<Integer>) keyMap.get(key1);
            for (Integer key2 : keySet) {
                int sentenceLength2 = sentenceList2.get(key2).getTokens().size();
                Allocation comparison = associationMap.get(key1, key2);

                int diffLength = Math.abs(sentenceLength1 - sentenceLength2);
                double avgRating = comparison.getAvgTokenSimilarityRating();
                Map.Entry nextEntry;
                int entryKey = key1;
                int neighbourCount = 1;
                int indexCounter = 0;
                SimpleRegression regression = new SimpleRegression();
                while (indexCounter < 3) {
                    indexCounter++;
                    nextEntry = sentenceMap.lowerEntry(entryKey);
                    if (nextEntry != null) {
                        Allocation nextAllocation = (Allocation) nextEntry.getValue();
                        regression.addData(nextAllocation.getSentenceID1(), nextAllocation.getSentenceID2());
                        neighbourCount++;
                        entryKey = (int) nextEntry.getKey();
                    } else {
                        indexCounter = 5;
                    }
                }

                entryKey = key1;
                indexCounter = 0;
                while (indexCounter < 3) {
                    indexCounter++;
                    nextEntry = sentenceMap.higherEntry(entryKey);
                    if (nextEntry != null) {
                        Allocation nextAllocation = (Allocation) nextEntry.getValue();
                        regression.addData(nextAllocation.getSentenceID1(), nextAllocation.getSentenceID2());
                        neighbourCount++;
                        entryKey = (int) nextEntry.getKey();
                    } else {
                        indexCounter = 5;
                    }
                }

                regression.addData(1.0, 1.0);
                regression.addData(sentenceList1.size(), sentenceList2.size());

                double regValue = regression.predict(key1);
                double diffPosition = Math.abs(regValue - key2);
                double comparisonRating = avgRating * comparison.getTokenPairList().size() / (Math.sqrt(diffLength) + Math.pow(diffPosition, 2) + 1);

                if (comparisonRating < 0.01) {
                    if (keySet.size() == 1) {
                        continue;
                    }
                }

                if (comparisonRating > bestCRating) {
                    bestCRating = comparisonRating;
                    comparison.setAllocationRating(comparisonRating);
                    bestComparison = comparison;
                }
            }
            if (bestComparison != null) {
                sentenceMap.put(key1, bestComparison);
            }
            nextTreeMapEntry = sentenceMap.higherEntry(key1);
        }
        if (recDepth < maxRecDepth) {
            iterate(recDepth + 1);
        }
    }

    private AssociationMap createAssociationMap(ArrayList<TokenPair> similarityList) {
        AssociationMap allocationMap = new AssociationMap();

        for (TokenPair tokenPair : similarityList) {
            
            int sentenceID1 = 0;
            int sentenceID2 = 0;
            
            if(swap){
                sentenceID1 = tokenPair.getToken2().getSentenceNumber();
                sentenceID2 = tokenPair.getToken1().getSentenceNumber();
            }else{
                sentenceID1 = tokenPair.getToken1().getSentenceNumber();
                sentenceID2 = tokenPair.getToken2().getSentenceNumber();
            }

            if (allocationMap.contains(sentenceID1, sentenceID2)) {
                allocationMap.get(sentenceID1, sentenceID2).addTokenPair(tokenPair);
            } else {
                Allocation newComparison = new Allocation();
                newComparison.setSentenceID1(sentenceID1);
                newComparison.setSentenceID2(sentenceID2);
                newComparison.addTokenPair(tokenPair);
                allocationMap.put(sentenceID1, sentenceID2, newComparison);
            }
        }

        return allocationMap;
    }

    private AssociationMap createAssociationMap(ArrayList<TokenPair> similarityList, int s1min, int s1max, int s2min, int s2max) {
        AssociationMap allocationMap = new AssociationMap();

        for(TokenPair tokenPair : similarityList) {
            
            int sentenceID1 = 0;
            int sentenceID2 = 0;
            
            if(swap){
                sentenceID1 = tokenPair.getToken2().getSentenceNumber();
                sentenceID2 = tokenPair.getToken1().getSentenceNumber();
            }else{
                sentenceID1 = tokenPair.getToken1().getSentenceNumber();
                sentenceID2 = tokenPair.getToken2().getSentenceNumber();
            }

            if ((sentenceID1 >= s1min) && (sentenceID1 <= s1max) && (sentenceID2 >= s2min) && (sentenceID2 <= s2max)) {
                if (allocationMap.contains(sentenceID1, sentenceID2)) {
                    allocationMap.get(sentenceID1, sentenceID2).addTokenPair(tokenPair);
                } else {
                    Allocation newComparison = new Allocation();
                    newComparison.setSentenceID1(sentenceID1);
                    newComparison.setSentenceID2(sentenceID2);
                    newComparison.addTokenPair(tokenPair);
                    allocationMap.put(sentenceID1, sentenceID2, newComparison);
                }
            }

        }

        return allocationMap;
    }
}
