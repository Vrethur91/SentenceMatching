package org.nlp.match.TokenFilter;

import java.util.ArrayList;
import java.util.TreeMap;
import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.Sentenizer.Utility.Token;

/**
 * Implements TokenFilterInterface.
 * Returns all words beginning with upper case (includes first word in sentence)
 * @author Benedict Preßler
 */
public class UpperCaseFilter implements TokenFilterInterface {

    @Override
    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList) {
        ArrayList<Token> tokenList = new ArrayList<>();
        for (int i = 1; i <= sentenceList.size(); i++) {
            Sentence sentence = sentenceList.get(i);
            for (Token token : sentence.getTokens()) {
                String tokenName = token.getTokenName();
                if (Character.isUpperCase(tokenName.charAt(0))) {
                    tokenList.add(token);
                }
            }
        }

        return tokenList;
    }

}
