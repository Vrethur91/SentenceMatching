package org.nlp.match.Sentenizer;

import com.ibm.icu.text.Transliterator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.blackbox.nlp.tokenization.Sentenizer;

/**
 *
 * @author Bene
 */
public class LatinSentenizer implements SentenizerInterface{

    private Transliterator trans;
    
    public LatinSentenizer(){
        String id = "Any-Latin; NFD; [^a-zA-Z\\s] Remove";
        trans = Transliterator.getInstance(id);
    }

    public TreeMap<Integer, Sentence> sentenize(String filepath, String language) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"))){
            TreeMap<Integer, Sentence> sentenceList = new TreeMap<>();
            StringBuilder text = new StringBuilder();
            
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            String textString = text.toString();
            
            Map<String, List<Integer[]>> map = Sentenizer.getSentences(textString);
            int sentenceNumber=1;
            for(Map.Entry entry:map.entrySet()){
                String sentenceString = (String)entry.getKey();
                if(sentenceString.equals("")) continue;
                Sentence sentence = new Sentence();
                sentence.setSentenceNumber(sentenceNumber);
                sentence.setFulltext(sentenceString);
                sentence.setLanguage(language);
                sentence.setTokens(tokenize(sentenceString, sentenceNumber, language));
                sentenceList.put(sentenceNumber, sentence);
                sentenceNumber++;
            }
                    
            
            return sentenceList;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LatinSentenizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LatinSentenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private ArrayList<Token> tokenize(String sentenceString, int sNumber, String language) {
        ArrayList<Token> tokenList = new ArrayList<>();
        String[] splitStrings = sentenceString.split("[\\s:\"]");
        int wordNumber=1;
        for (String splitString : splitStrings) {
            String latin = trans.transform(splitString);
            if(latin.equals("")) continue;
            Token token = new Token();
            token.setTokenName(latin);
            token.setSentenceNumber(sNumber);
            token.setWordPosition(wordNumber);
            token.setBaseName(splitString);
            token.setLanguage(language);
            tokenList.add(token);
            wordNumber++;
        }
        
        return tokenList;
    }
    
}
