package org.nlp.match.SentenceMapper;

import org.nlp.match.General.DataHolder;
import org.nlp.match.SentenceMapper.Utility.Allocation;
import java.util.TreeMap;

/**
 *
 * @author Bene
 */
public interface SentenceMapperInterface {
    TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap);
    TreeMap<Integer, Allocation> mapSentences(DataHolder holder, boolean swap, int s1min, int s1max, int s2min, int s2max);
}
