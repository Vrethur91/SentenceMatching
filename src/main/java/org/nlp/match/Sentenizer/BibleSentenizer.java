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
public class BibleSentenizer implements SentenizerInterface{
    
    private Transliterator trans;

    public BibleSentenizer(){
        String id = "Any-Latin; NFD; [^a-zA-Z\\s] Remove";
        trans = Transliterator.getInstance(id);
    }
    
    @Override
    public TreeMap<Integer, Sentence> sentenize(String filepath, String language) {
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"))){
            TreeMap<Integer, Sentence> sentenceList = new TreeMap<>();

            //System.out.println(">> Load Data for \""+language+"\" from \""+filepath+"\"");
            String line;
            int sentenceNumber = 1;
            while ((line = br.readLine()) != null) {
                if(line.equals("")) continue;
                String[] split = line.split("\t");
                Sentence sentence = new Sentence();
                sentence.setSentenceNumber(sentenceNumber);
                sentence.setAdditionalNumber(split[0]);
                sentence.setFulltext(split[1]);
                sentence.setLanguage(language);
                sentence.setTokens(tokenize(split[1], sentence.getSentenceNumber(), language));
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
        String[] splitStrings = sentenceString.split("[\\s:\"]|(--)");
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
