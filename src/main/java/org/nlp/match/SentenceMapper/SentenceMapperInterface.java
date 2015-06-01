package org.nlp.match.SentenceMapper;

import java.util.TreeMap;
import org.nlp.match.Utility.*;

/**
 *
 * @author Bene
 */
public interface SentenceMapperInterface {
    TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap);
    TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap, int s1min, int s1max, int s2min, int s2max);
}
