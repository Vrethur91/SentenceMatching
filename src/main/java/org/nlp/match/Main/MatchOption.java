package org.nlp.match.Main;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.nlp.match.SentenceMapper.*;
import org.nlp.match.Sentenizer.*;
import org.nlp.match.TokenMapper.*;
import org.nlp.match.Utility.*;
import org.nlp.match.TokenFilter.*;

/**
 * Delivers simplified methods to match two texts 
 * @author Bene
 */
public class MatchOption {
    /**
     * Method to import two text files, sentenize and tokenize them.
     * Example:
     * 
     * DataHolder holder = new DataHolder();
     * MatchOption matchOption = new MatchOption();
     * 
     * matchOption.sentenize(holder, 
     *      "./docs/text1.txt", "gr",
     *      "./docs/text2.txt", "eng",
     *      new GreekSentenizer(), new LatinSentenizer());
     * 
     * @param holder DataHolder instance to store and get informations needed to proceed  
     * @param path1 path to the first text file
     * @param lang1 language of the first text
     * @param path2 path to the second text file
     * @param lang2 language of the second text
     * @param sentenizer1 sentenizer instance to sentenize the first text
     * @param sentenizer2 sentenizer instance to sentenize the second text. If null, sentenizer1 will be used instead
     */
    public void sentenize(DataHolder holder, String path1, String lang1, String path2, String lang2, SentenizerInterface sentenizer1, SentenizerInterface sentenizer2){
        System.out.println("> Run Sentenizer ");
        if(sentenizer2 == null){
            sentenizer2 = sentenizer1;
        }
        TreeMap<Integer, Sentence> sentenceList1 = sentenizer1.sentenize(path1, lang1);
        holder.setSentenceList1(sentenceList1);
        holder.setLanguage1(lang1);
        
        System.out.println(">> "+sentenceList1.size()+" Sentences for \""+lang1+"\" created");
        
        TreeMap<Integer, Sentence> sentenceList2 = sentenizer2.sentenize(path2, lang2);
        holder.setSentenceList2(sentenceList2);
        holder.setLanguage2(lang2);
        
        System.out.println(sentenceList2.size()+" Sentences for \""+lang2+"\" created");
    }
    
    /**
     * Method to search for specific tokens like Named Entities.
     * Requirements: sentenized and tokenized texts in the DataHolder instance
     * 
     * Supported options for opt1, opt2:
     * "All": take all tokens that start with an upper case character
     * "eng": use Standford NER to search for entities in an english text
     * 
     * It also gives the option to sort out the top occurring words for both result lists
     * 
     * Example:
     * 
     * DataHolder holder = new DataHolder();
     * MatchOption matchOption = new MatchOption();
     * 
     * matchOption.defineToken(holder, 
     *      "All", "eng",
     *      0.1, 0.1);
     * 
     * @param holder DataHolder instance to store and get informations needed to proceed  
     * @param opt1 search mode for the first text
     * @param opt2 search mode for the second text
     * @param diff1 decimal value (between 0.0 and 1.0) for the top occurrence filter. 
     * @param diff2 decimal value (between 0.0 and 1.0) for the top occurrence filter. 
     */
    public void defineToken(DataHolder holder, String opt1, String opt2, double diff1, double diff2){
        System.out.println("> Define Token ");
        TokenFilter filter = new TokenFilter();
        ArrayList<Token> tokenList1 = null;
        ArrayList<Token> tokenList2 = null;
        
        switch(opt1){
            case "eng": tokenList1 = filter.filterToken(holder.getSentenceList1());
                        break;
            case "All": tokenList1 = filter.filterToken(holder.getSentenceList1(), "All");
                        break;
        }
        
        if(diff1>0.0&&tokenList1!=null){
            tokenList1 = Tweaker.removeTopOccurences(tokenList1, diff1);
        }
        
        System.out.println(tokenList1.size()+" Token for \""+ holder.getLanguage1() +"\" created");
        
        switch(opt2){
            case "eng": tokenList2 = filter.filterToken(holder.getSentenceList2());
                        break;
            case "All": tokenList2 = filter.filterToken(holder.getSentenceList2(), "All");
                        break;
        }
        
        if(diff2>0.0&&tokenList2!=null){
            tokenList2 = Tweaker.removeTopOccurences(tokenList2, diff2);
        }
        
        System.out.println(tokenList2.size()+" Token for \""+ holder.getLanguage2() +"\" created");
        
        holder.setTokenList1(tokenList1);
        holder.setTokenList2(tokenList2);
    }

    /**
     * Method to compare and store similar token.
     * Requirements: defined token in the DataHolder instance
     * Example:
     * 
     * DataHolder holder = new DataHolder();
     * MatchOption matchOption = new MatchOption();
     * 
     * matchOption.mapToken(holder,
     *      new LevenshteinTokenMapper(
     *          new new LevenshteinDistanceEnglishGreek(5, 3, 1)),
     *      0.6, false);
     * 
     * @param holder DataHolder instance to store and get informations needed to proceed  
     * @param tokenMapper instance of an implementation of the TokenMapperInterface
     * @param threshold minimum similarity value to get stored
     * @param inLanguage set true to additional compare all specific token within a text
     */
    public void mapToken(DataHolder holder, TokenMapperInterface tokenMapper, double threshold, boolean inLanguage){
        System.out.println("> Map Token ");
        tokenMapper.setThreshold(threshold);
        ArrayList<TokenPair> similarityList = tokenMapper.measureTokenSimilarity(holder.getTokenList1(), holder.getTokenList2(), inLanguage);
        holder.setSimilarityList(similarityList);
        System.out.println(similarityList.size() + " Matches for Token found");
    }
    
    /**
     * Method to compare sentences based on similar token.
     * Requirements: list of similar tokens and sentenized texts in the DataHolder instance
     * The SentenceMapper usually finds corresponding sentences from the second text for the first text.
     * If swap is set to true, it additional swap sentence list, so that it finds corresponding sentences from the first text to the second text.
     * Then it compares the resulting lists and put correlating matches into a new list.
     * Example:
     * 
     * DataHolder holder = new DataHolder();
     * MatchOption matchOption = new MatchOption();
     * 
     * matchOption.mapSentence(holder, 
     *      new LinearSentenceMapper(), true);
     * 
     * @param holder DataHolder instance to store and get informations needed to proceed  
     * @param sentenceMapper instance of an implementation of the SentenceMapperInterface
     * @param swap set true to additional swap sentence lists and compare again
     */
    public void mapSentences(DataHolder holder, SentenceMapperInterface sentenceMapper, boolean swap){
        System.out.println("> Map Sentences ");
        TreeMap<Integer, Allocation> sentenceMap = sentenceMapper.mapSentences(holder, false);
        if(swap){
            TreeMap<Integer, Allocation> sentenceMapSwap = sentenceMapper.mapSentences(holder, true);
            sentenceMap = matchMappings(sentenceMap, sentenceMapSwap);
        }
        
        holder.setSentenceMap(sentenceMap);
        System.out.println(sentenceMap.size() + " Matches for Sentences found");
    }
    
    /**
     * Method to compare sentences based on similar token.
     * Requirements: list of similar tokens and sentenized texts in the DataHolder instance
     * The SentenceMapper usually finds corresponding sentences from the second text for the first text.
     * If swap is set to true, it additional swap sentence list, so that it finds corresponding sentences from the first text to the second text.
     * Then it compares the resulting lists and put correlating matches into a new list.
     * This method uses a divide-and-conquer based algorithm.
     * Example: 
     * 
     * DataHolder holder = new DataHolder();
     * MatchOption matchOption = new MatchOption();
     * 
     * matchOption.mapSentenceDC(holder, 3, true);
     * 
     * @param holder DataHolder instance to store and get informations needed to proceed  
     * @param split defines the number of windows of the split algorithm. Example: 3 results in 3^2 = 9 windows
     * @param swap set true to additional swap sentence lists and compare again
     */
    public void mapSentencesDC(DataHolder holder, int split, boolean swap){
        System.out.println("> Map Sentences ");
        DCSentenceMapper sentenceMapper = new DCSentenceMapper();
        TreeMap<Integer, Allocation> sentenceMap = sentenceMapper.divideAndConquer(holder, split, false);
        if(swap){
            TreeMap<Integer, Allocation> sentenceMapSwap = sentenceMapper.divideAndConquer(holder, split, true);
            sentenceMap = matchMappings(sentenceMap, sentenceMapSwap);
        }
        
        holder.setSentenceMap(sentenceMap);
        System.out.println(sentenceMap.size() + " Matches for Sentences found");
    }

    /**
     * Method to run a small analysis over the result of the sentence mapping
     * Requirements: sentence map in the DataHolder instance
     * @param holder DataHolder instance to store and get informations needed to proceed  
     */
    public void runAnalysis(DataHolder holder){
        System.out.println("> Run Analysis");
        SimpleRegression regression = new SimpleRegression();
        for (Entry entry : holder.getSentenceMap().entrySet()) {
            regression.addData((int) entry.getKey(), ((Allocation) entry.getValue()).getSentenceID2());

        }
        System.out.println("MSE: "+regression.getMeanSquareError());
        int xmax = holder.getSentenceList1().size();
        System.out.println("x=1, y="+regression.predict(1)+" x="+xmax + ", y="+regression.predict(xmax));
    }
    
    private TreeMap<Integer, Allocation> matchMappings(TreeMap<Integer, Allocation> sentenceMap1, TreeMap<Integer, Allocation> sentenceMap2){
        TreeMap<Integer, Allocation> sentenceMap = new TreeMap<>();
        for(Entry<Integer, Allocation> entry : sentenceMap1.entrySet()){
            int map1Key1 = entry.getKey();
            int map1Key2 = entry.getValue().getSentenceID2();
            if(sentenceMap2.containsKey(map1Key2)){
                if(sentenceMap2.get(map1Key2).getSentenceID2() == map1Key1){
                    sentenceMap.put(map1Key1, entry.getValue());
                }
            }
        }
        
        return sentenceMap;
    }


}
