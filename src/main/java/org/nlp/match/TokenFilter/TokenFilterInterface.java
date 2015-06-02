package org.nlp.match.TokenFilter;

import java.util.ArrayList;
import java.util.TreeMap;
import org.nlp.match.Sentenizer.Sentence;
import org.nlp.match.Sentenizer.Token;

/**
 * Interface for token filter classes
 * @author Benedict Preßler
 */
public interface TokenFilterInterface {
    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList);
}
