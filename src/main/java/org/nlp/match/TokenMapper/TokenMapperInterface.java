package org.nlp.match.TokenMapper;

import org.nlp.match.TokenMapper.Utility.TokenPair;
import java.util.ArrayList;
import org.nlp.match.Sentenizer.Utility.Token;

/**
 *
 * @author Bene
 */
public interface TokenMapperInterface {
    
    public ArrayList<TokenPair> measureTokenSimilarity(ArrayList<Token> list1, ArrayList<Token> list2, boolean inLanguage);
    
    public void setThreshold(double threshold);
}
