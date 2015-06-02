package org.nlp.match.Sentenizer;

import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.Sentenizer.Utility.Token;
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
public class GreekSentenizer implements SentenizerInterface{
    
    private Transliterator trans;
    
    public GreekSentenizer(){
        String id = "Any-Latin; NFD; [^a-zA-Z\\s] Remove";
        trans = Transliterator.getInstance(id);
    }
    @Override
    public TreeMap<Integer, Sentence> sentenize(String filepath, String language) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"))){
            TreeMap<Integer, Sentence> sentenceList = new TreeMap<>();
            StringBuilder text = new StringBuilder();
            
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append(" ");
            }
            String textString = text.toString();
            
            String[] sstring = textString.split("[\\.\\!\\?\\;]");

            int sentenceNumber=1;
            for(String split:sstring){
                String sentenceString = split.replaceAll("[\\’\\‘]", "").trim();
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
            Logger.getLogger(GreekSentenizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GreekSentenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private ArrayList<Token> tokenize(String sentenceString, int sNumber, String language) {
        ArrayList<Token> tokenList = new ArrayList<>();
        String[] splitStrings = sentenceString.split(" ");
        int wordNumber=1;
        for (String splitString : splitStrings) {
            String latin = trans.transform(splitString);
            if(latin.equals("")) continue;
            Token token = new Token();
            token.setTokenName(latin);
            token.setSentenceNumber(sNumber);
            token.setWordPosition(wordNumber);
            token.setBaseName(splitString.replaceAll("[\\,\\;\\[\\]\\:]", "").trim());
            token.setLanguage(language);
            tokenList.add(token);
            wordNumber++;
        }
        
        return tokenList;
    }
}
