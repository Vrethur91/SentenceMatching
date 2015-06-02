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
public class BibleSentenizerSplit implements SentenizerInterface{
    private Transliterator trans;

    public BibleSentenizerSplit(){
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
                ArrayList<ArrayList<Token>> multiList = tokenize(split[1], sentenceNumber, language);
                
                Sentence sentence1 = new Sentence();
                sentence1.setSentenceNumber(sentenceNumber);
                sentence1.setAdditionalNumber(split[0]);
                sentence1.setFulltext(split[1]);
                sentence1.setLanguage(language);
                sentence1.setTokens(multiList.get(0));
                sentenceList.put(sentenceNumber, sentence1);
                sentenceNumber++;
                
                Sentence sentence2 = new Sentence();
                sentence2.setSentenceNumber(sentenceNumber);
                sentence2.setAdditionalNumber(split[0]);
                sentence2.setFulltext(split[1]);
                sentence2.setLanguage(language);
                sentence2.setTokens(multiList.get(1));
                sentenceList.put(sentenceNumber, sentence2);
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
    
    private ArrayList<ArrayList<Token>> tokenize(String sentenceString, int sNumber, String language) {
        ArrayList<ArrayList<Token>> multiList = new ArrayList<>();
        ArrayList<Token> tempList = new ArrayList<>();
        ArrayList<Token> tokenList1 = new ArrayList<>();
        ArrayList<Token> tokenList2 = new ArrayList<>();
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
            tempList.add(token);
            wordNumber++;
        }
        
        int mid = tempList.size() / 2;
        int newWordNumber = 1;
        for(Token token: tempList){
            if(token.getWordPosition()<=mid){
                tokenList1.add(token);
            } else{
                token.setSentenceNumber(sNumber+1);
                token.setWordPosition(newWordNumber);
                tokenList2.add(token);
                newWordNumber++;
            }
        }
        
        multiList.add(tokenList1);
        multiList.add(tokenList2);
        return multiList;
    }
}
