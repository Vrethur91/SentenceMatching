package org.nlp.match.TokenFilter;

import org.nlp.match.Sentenizer.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nlp.match.Utility.ConfigLoader;

/**
 *
 * @author Bene
 */
public class TokenFilter {

    private Set<String> baseSet;

    public TokenFilter() {
        baseSet = new HashSet<>();
    }
    
    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList){
        ArrayList<Token> tokenList = new ArrayList<>();
        try{
            String serializedClassifier = ConfigLoader.getProperty("standford.ner.model.path");
            AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
            for(int i = 1; i <=sentenceList.size();i++) {
                Sentence sentence = sentenceList.get(i);
                //System.out.println(sentence.getFulltext());
                int tokenIndex = 0;
                for(List<CoreLabel> lcl : classifier.classify(sentence.getFulltext())){
                    
                    for (CoreLabel cl : lcl) {
                        
                        if ((!cl.get(CoreAnnotations.AnswerAnnotation.class).equals("O"))&&(!Character.isLowerCase(cl.originalText().charAt(0)))) {
                            if(cl.originalText().length()>1){

                                tokenList.add(sentence.getTokens().get(tokenIndex));
                            }
                        }
                        if(Character.isLetter(cl.originalText().charAt(0))){
                            tokenIndex++;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TokenFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassCastException ex) {
            Logger.getLogger(TokenFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TokenFilter.class.getName()).log(Level.SEVERE, null, ex);
        
        }
        
        return tokenList;
    }

    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList, String filterMode) {
        ArrayList<Token> tokenList = new ArrayList<>();

        
        for (int i = 1; i<= sentenceList.size();i++) {
            Sentence sentence = sentenceList.get(i);
            for (Token token : sentence.getTokens()) {
                String tokenName = token.getTokenName();
                switch (filterMode) {
                    case "All":
                        if (Character.isUpperCase(tokenName.charAt(0))) {
                            tokenList.add(token);
                        }
                        break;
                    case "NameSet":
                        if (baseSet.contains(tokenName)) {
                            tokenList.add(token);
                        }
                        break;
                    default:
                        System.out.println("Unsupported return mode: " + filterMode);
                        return null;
                }
            }
        }
        
        return tokenList;
    }

    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList, int minWordLength){
        ArrayList<Token> tokenList = new ArrayList<>();
        
        for(int i = 1;i <= sentenceList.size(); i++){
            Sentence sentence = sentenceList.get(i);
            for(Token token:sentence.getTokens()){
                if(token.getTokenName().length()>=minWordLength){
                    tokenList.add(token);
                }
            }
        }
        
        return tokenList;
    }
    
    public void loadBaseSet(String pathToNameFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToNameFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!baseSet.contains(line)) {
                    baseSet.add(line);
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TokenFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TokenFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
