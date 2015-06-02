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
public class SlidingWindows implements SentenizerInterface {

    @Override
    public TreeMap<Integer, Sentence> sentenize(String filepath, String language) {

        int windowSize = 10;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"))) {
            TreeMap<Integer, Sentence> sentenceList = new TreeMap<>();
            ArrayList<Sentence> windowList = new ArrayList<>();
            String id = "Any-Latin; NFD; [^a-zA-Z\\s] Remove"; //^a-zA-Z\s //^\\p{Word}
            Transliterator trans = Transliterator.getInstance(id);

            int sentenceNumber = 1;
            int i = 0;

            String line;
            while ((line = br.readLine()) != null) {
                String[] sstring = line.split("\\s+");
                for (String substring : sstring) {

                    String latin = trans.transform(substring);
                    if (latin.equals("")) {
                        continue;
                    }
                    
                    if (windowList.size() < i + 1) {
                        Sentence newSentence = new Sentence();
                        newSentence.setLanguage(language);
                        newSentence.setSentenceNumber(sentenceNumber);
                        newSentence.setFulltext(substring);

                        ArrayList<Token> tokenList = new ArrayList<>();
                        Token token = new Token();
                        token.setBaseName(substring);
                        token.setSentenceNumber(sentenceNumber);
                        token.setWordPosition(1);
                        token.setTokenName(latin);
                        token.setLanguage(language);
                        tokenList.add(token);
                        newSentence.setTokens(tokenList);
                        sentenceNumber++;
                        windowList.add(newSentence);
                    } else {
                        for (int j = 0; j < windowList.size(); j++) {
                            if(j == i){
                                sentenceList.put(windowList.get(j).getSentenceNumber(), windowList.get(j));
                                
                                Sentence newSentence = new Sentence();
                                newSentence.setLanguage(language);
                                newSentence.setSentenceNumber(sentenceNumber);
                                newSentence.setFulltext(substring);
                                
                                ArrayList<Token> tokenList = new ArrayList<>();
                                Token token = new Token();
                                token.setBaseName(substring);
                                token.setTokenName(latin);
                                token.setLanguage(language);
                                token.setSentenceNumber(sentenceNumber);
                                token.setWordPosition(1);
                                tokenList.add(token);
                                
                                newSentence.setTokens(tokenList);
                                
                                sentenceNumber++;
                                windowList.set(j, newSentence);
                            } else {
                                Sentence sentence = windowList.get(j);
                                Token token = new Token();
                                token.setBaseName(substring);
                                token.setTokenName(latin);
                                token.setLanguage(language);
                                token.setSentenceNumber(sentence.getSentenceNumber());
                                token.setWordPosition(sentence.getTokens().size()+1);
                                sentence.getTokens().add(token);
                                sentence.setFulltext(sentence.getFulltext()+" "+substring);
                            }
                        }
                    }

                    i = (i + 1) % windowSize;
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
