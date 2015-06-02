package org.nlp.match.TokenFilter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nlp.match.Sentenizer.Utility.Sentence;
import org.nlp.match.Sentenizer.Utility.Token;
import org.nlp.match.Tools.ConfigLoader;

/**
 * Implements TokenFilterInterface
 * Returns all words that are present in the label set.
 * Default property for location is 'labellist.location'.
 * For custom property (for separated lists), use LabelListFilter(String property) constructor.
 * @author Benedict Preßler
 */
public class LabelListFilter implements TokenFilterInterface {

    private Set<String> labelSet;

    public LabelListFilter() {
        String pathToList = ConfigLoader.getProperty("labellist.location");
        loadLabelSet(pathToList);
    }
    
    public LabelListFilter(String property){
        String pathToList = ConfigLoader.getProperty(property);
        loadLabelSet(pathToList);
    }

    @Override
    public ArrayList<Token> filterToken(TreeMap<Integer, Sentence> sentenceList) {
        ArrayList<Token> tokenList = new ArrayList<>();

        for (int i = 1; i <= sentenceList.size(); i++) {
            Sentence sentence = sentenceList.get(i);
            for (Token token : sentence.getTokens()) {
                String tokenName = token.getTokenName();
                if (labelSet.contains(tokenName)) {
                    tokenList.add(token);
                }
            }
        }
        return tokenList;
    }

    private void loadLabelSet(String pathToList) {
        try {
            String[] pathList = pathToList.split(";");
            for (String path : pathList) {
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line;
                while ((line = br.readLine()) != null) {
                    if (!labelSet.contains(line)) {
                        labelSet.add(line);
                    }
                }
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error for LabelListFilter. Please check if the path/paths to list location is set right in config.property (labellist.location).");
            Logger.getLogger(LabelListFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
