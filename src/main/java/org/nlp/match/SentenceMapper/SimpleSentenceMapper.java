package org.nlp.match.SentenceMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.nlp.match.TokenMapper.TokenPair;
import org.nlp.match.Sentenizer.Sentence;
import org.nlp.match.Utility.DataHolder;

/**
 *
 * @author Bene
 */
public class SimpleSentenceMapper implements SentenceMapperInterface {

    private TreeMap<Integer, Allocation> sentenceMap;
    private TreeMap<Integer, Sentence> sentenceList1;
    private TreeMap<Integer, Sentence> sentenceList2;
    private boolean swap;
    private AssociationMap associationMap;

    @Override
    public TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap) {
        sentenceMap = new TreeMap<>();
        if (swap) {
            sentenceList1 = holder.getSentenceList2();
            sentenceList2 = holder.getSentenceList1();
        } else {
            sentenceList1 = holder.getSentenceList1();
            sentenceList2 = holder.getSentenceList2();
        }

        this.swap = swap;
        associationMap = createAssociationMap(holder.getSimilarityList());
        initialize();

        System.out.println(">> " + sentenceMap.size() + " Matches for Sentences found");
        return sentenceMap;
    }

    @Override
    public TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap, int s1min, int s1max, int s2min, int s2max) {
        sentenceMap = new TreeMap<>();

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

        return sentenceMap;
    }

    private void initialize() {
        int total = 0;
        int hit = 0;
        int skip = 0;
        HashMap<Integer, HashSet<Integer>> keyMap = associationMap.getKeyMap();

        for (Map.Entry<Integer, HashSet<Integer>> key1Entry : keyMap.entrySet()) {
            String debug = "";
            boolean selfValue = false;
            int key1 = key1Entry.getKey();
            int sentenceLength1 = sentenceList1.get(key1).getTokens().size();
            double bestCRating = 0.0;
            Allocation bestComparison = null;

            HashSet<Integer> keySet = key1Entry.getValue();
            for (Integer key2 : keySet) {
                if (key1 == key2) {
                    selfValue = true;
                }
                debug += key1 + " - " + key2;
                int sentenceLength2 = sentenceList2.get(key2).getTokens().size();
                Allocation allocation = associationMap.get(key1, key2);
                int diffPosition = Math.abs(key1 - key2);
                int diffLength = Math.abs(sentenceLength1 - sentenceLength2);
                /*
                 if(diffLength>= 2*Math.min(sentenceList1.get(key1).getTokens().size(), (double) sentenceList2.get(key2).getTokens().size())){
                 skip++;
                 continue;
                 }
                 */

                double avgRating = allocation.getAvgTokenSimilarityRating();
                double var = 0;
                for (Entry entry : allocation.getTokenPairList().entrySet()) {
                    TokenPair pair = (TokenPair) entry.getValue();
                    var += Math.pow(pair.getSimilarity() - avgRating, 2);
                }

                double rating = avgRating * allocation.getTokenPairList().size() / (Math.min((double) sentenceList1.get(key1).getTokens().size(), (double) sentenceList2.get(key2).getTokens().size()) + diffLength * 2);//Math.pow(avgRating, allocation.getTokenPairList().size());

                debug += ", Rating: " + rating + ", Var: " + var + ", AvgRating: " + avgRating + ", DiffLength: " + diffLength + "\n";
                debug += sentenceList1.get(key1).getTokens().size() + " " + sentenceList2.get(key2).getTokens().size() + "\n";

                for (Entry entry : allocation.getTokenPairList().entrySet()) {
                    TokenPair tokenPair = (TokenPair) entry.getValue();
                    debug += tokenPair.getToken1().getTokenName() + "." + tokenPair.getToken1().getWordPosition() + "-" + tokenPair.getToken2().getTokenName() + "." + tokenPair.getToken2().getWordPosition() + ":" + tokenPair.getSimilarity() + "\n";
                }

                if (rating < 0.01) {
                    if (keySet.size() == 1) {
                        skip++;
                        continue;
                    }
                }

                if (rating > bestCRating) {
                    debug += "New Best: " + rating + ", before: " + bestCRating + "\n";
                    allocation.setAllocationRating(rating);
                    bestComparison = allocation;
                    bestCRating = rating;

                }
                debug += "\n";
            }
            if (bestComparison != null) {
                debug += "Best: " + key1 + " " + bestComparison.getSentenceID2();
                sentenceMap.put(key1, bestComparison);
                if (selfValue) {
                    total++;
                    debug += "\n-------------------------------------------------------------------";
                    //System.out.println(debug);
                    if (key1 == bestComparison.getSentenceID2()) {
                        hit++;
                    }
                }
                if (keySet.size() == 1 && !selfValue) {
                    debug += "\n-------------------------------------------------------------------";
                    //System.out.println(debug);
                }

            }

        }
        //System.out.println(">> Skips: "+skip);
        //System.out.println(">> "+hit + " / " + total);
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
