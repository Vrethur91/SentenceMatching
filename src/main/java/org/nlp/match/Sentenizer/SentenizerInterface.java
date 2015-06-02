package org.nlp.match.Sentenizer;

import org.nlp.match.Sentenizer.Utility.Sentence;
import java.util.TreeMap;

/**
 *
 * @author Bene
 */
public interface SentenizerInterface {

    TreeMap<Integer, Sentence> sentenize(String filepath, String language);
}
