package org.nlp.match.Sentenizer;

import java.util.TreeMap;

/**
 *
 * @author Bene
 */
public interface SentenizerInterface {

    TreeMap<Integer, Sentence> sentenize(String filepath, String language);
}
