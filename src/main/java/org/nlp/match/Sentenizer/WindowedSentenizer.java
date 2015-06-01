package org.nlp.match.Sentenizer;

import com.ibm.icu.text.Transliterator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bene
 */
public class WindowedSentenizer implements SentenizerInterface{

    @Override
    public TreeMap<Integer, Sentence> sentenize(String filepath, String language) {
        
        int windowSize = 10;
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"))){
            StringBuilder text = new StringBuilder();
            int sentenceNumber = 1;
            int tokenNumber = 1;
            TreeMap<Integer, Sentence> sentenceList = new TreeMap<>();
            Sentence sentence = new Sentence();
            sentence.setLanguage(language);
            sentence.setSentenceNumber(sentenceNumber);
            ArrayList<Token> tokenList = new ArrayList<>();
            String fulltext = "";
            
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] sstring = line.split("\\s+");
                for(String substring:sstring){
                    fulltext += substring+" ";
                    
                    Token token = new Token();
                    token.setBaseName(substring);
                    String id = "Any-Latin; NFD; [^a-zA-Z\\s] Remove"; //^a-zA-Z\s //^\\p{Word}
                    String latin = Transliterator.getInstance(id).transform(substring);
                    if (latin.equals("")) continue;
                    token.setTokenName(latin);
                    token.setLanguage(language);
                    token.setSentenceNumber(sentenceNumber);
                    token.setWordPosition(tokenNumber);
                    tokenNumber++;
                    tokenList.add(token);
                    
                    if(tokenList.size()==windowSize){
                        sentence.setFulltext(fulltext);
                        fulltext = "";
                        
                        sentence.setTokens(tokenList);
                        tokenList = new ArrayList<>();
                        
                        sentenceList.put(sentence.getSentenceNumber(), sentence);
                        sentence = new Sentence();
                        sentenceNumber++;
                        sentence.setLanguage(language);
                        sentence.setSentenceNumber(sentenceNumber);
                        tokenNumber = 1;
                    }
                }
                
                if(tokenList.size() > 0 ){
                    sentence.setFulltext(fulltext);
                    sentence.setTokens(tokenList);
                    sentenceList.put(sentence.getSentenceNumber(), sentence);
                }
            }
            
            return sentenceList;
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GreekSentenizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GreekSentenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
