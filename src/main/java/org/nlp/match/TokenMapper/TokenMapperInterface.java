package org.nlp.match.TokenMapper;

import java.util.ArrayList;
import org.nlp.match.Sentenizer.Token;

/**
 *
 * @author Bene
 */
public interface TokenMapperInterface {
    
    public ArrayList<TokenPair> measureTokenSimilarity(ArrayList<Token> list1, ArrayList<Token> list2, boolean inLanguage);
    
    public void setThreshold(double threshold);
}
