package org.blackbox.nlp.tokenization;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author jnphilipp
 * @version 0.2.0
 */
public class Sentenizer {

    /**
     * Sentenizes the given text.
     *
     * @param text text
     * @throws SQLException
     */
    /*
     public static void getSentences(Text text) throws SQLException {
     Map<String, List<Integer[]>> sentences = getSentences(text.getText());

     for ( Entry<String, List<Integer[]>> entry : sentences.entrySet() ) {
     Sentence sentence = new Sentence(entry.getKey());
     text.getSentences().put(sentence, new LinkedHashSet<Position>());
     for ( Integer[] value : entry.getValue() )
     text.getSentences().get(sentence).add(new Position(value[0], value[1]));
     }
     }*/

    /**
     * Sentenizes the given text.
     *
     * @param text text
     * @return list of sentences
     */
    public static Map<String, List<Integer[]>> getSentences(String text) {
        Map<String, List<Integer[]>> sentences = new LinkedHashMap<>();

        boolean hasWhitespace = false;
        int start = -1;
        Stack<String> needClosing = new Stack<>();
        for (int i = 0; i < text.length(); i++) {
            if (start == -1) {
                if (text.substring(i, i + 1).matches("[A-ZÄÖÜ0-9'\"„“”]")) {
                    start = i;
                    if (i > 0 && text.substring(i - 1, i).matches("[\\(\\[\\{]")) {
                        needClosing.add(text.substring(i - 1, i));
                    }
                }
            } else {
                if (text.substring(i, i + 1).matches("[\\(\\[\\{]")) {
                    needClosing.add(text.substring(i, i + 1));
                    continue;
                } else if (text.substring(i, i + 1).matches("[\\)\\]\\}]")) {
                    if (text.substring(i, i + 1).equals(")") && needClosing.peek().equals("(")) {
                        needClosing.pop();
                    } else if (text.substring(i, i + 1).equals("]") && needClosing.peek().equals("[")) {
                        needClosing.pop();
                    } else if (text.substring(i, i + 1).equals("}") && needClosing.peek().equals("{")) {
                        needClosing.pop();
                    }

                    if (start != 0 && ((text.substring(start - 1, start).equals("(") && text.substring(i, i + 1).equals(")")) || (text.substring(start - 1, start).equals("[") && text.substring(i, i + 1).equals("]")) || (text.substring(start - 1, start).equals("{") && text.substring(i, i + 1).equals("}")))) {
                        //System.out.println(text.substring(start, i));
                        start = -1;
                        hasWhitespace = false;
                        needClosing.clear();
                    }

                    continue;
                }

                if (text.substring(i, i + 1).matches("[\\.\\?!]")) {
                    if (text.substring(i, i + 1).matches("\\.") && i <= (text.length() - 3)) {
                        if (!hasWhitespace || text.substring(i, i + 3).matches("\\.\\d.|\\.\\s[a-zäöü]")) {
                            continue;
                        }

                        if (text.substring(start, i + 1).matches("([A-ZÄÖÜ][A-Za-zÄäÖöÜüß]*\\s?)+\\.")) {
                            continue;
                        }
                    }

                    if (!needClosing.isEmpty()) {
                        continue;
                    }

                    if (i <= (text.length() - 2) && text.substring(i + 1, i + 2).matches("['\"„“”]")) {
                        i++;
                    }

                    if (i <= (text.length() - 2) && text.substring(start, start + 1).matches("['\"„“”]") && text.substring(i + 1, i + 2).equals(",")) {
                        continue;
                    }

                    String sentence = text.substring(start, i + 1);
                    if (!sentences.containsKey(sentence)) {
                        sentences.put(sentence, new LinkedList<Integer[]>());
                    }

                    sentences.get(sentence).add(new Integer[]{start, i + 1});
                    start = -1;
                    hasWhitespace = false;
                    needClosing.clear();
                } else if (text.substring(i, i + 1).matches("\\s")) {
                    hasWhitespace = true;
                }
            }
        }

        return sentences;
    }
}
