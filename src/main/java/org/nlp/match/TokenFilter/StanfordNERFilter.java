package org.nlp.match.TokenFilter;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.Sentenizer.Utility.Token;
import org.nlp.match.Tools.ConfigLoader;

/**
 * Implements TokenFilterInterface.
 * Returns all words marked by the Stanford NER.
 * Path to classifier must be set in the config.property
 * @author Benedict Preßler
 */
public class StanfordNERFilter implements TokenFilterInterface{
    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList){
        ArrayList<Token> tokenList = new ArrayList<>();
        try{
            String serializedClassifier = ConfigLoader.getProperty("standford.ner.model.path");
            AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
            for(int i = 1; i <=sentenceList.size();i++) {
                Sentence sentence = sentenceList.get(i);
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
            Logger.getLogger(StanfordNERFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassCastException ex) {
            Logger.getLogger(StanfordNERFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StanfordNERFilter.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return tokenList;
    }
}
