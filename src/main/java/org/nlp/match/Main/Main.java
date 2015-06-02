package org.nlp.match.Main;

import org.nlp.match.General.MatchOption;
import org.nlp.match.Levenshtein.*;
import org.nlp.match.SentenceMapper.*;
import org.nlp.match.Sentenizer.*;
import org.nlp.match.TokenMapper.LevenshteinTokenMapper;
import org.nlp.match.TokenFilter.*;
import org.nlp.match.General.DataHolder;
import org.nlp.match.Tools.OutputWriter;

public class Main {

    public static void main(String[] args) throws Exception {
        //option();
        //runStandardMatch();
        //runBibleMatch();
        //test();
        
    }
    

    public static void option() {
        MatchOption option = new MatchOption();
        DataHolder holder = new DataHolder();

        option.sentenize(holder,
                ".//docs//apollo_gr.txt", "gr",
                ".//docs//apollo_eng.txt", "eng",
                new GreekSentenizer(), new LatinSentenizer());

        option.defineToken(holder, new UpperCaseFilter(), new StanfordNERFilter(), 0.0, 0.0);

        option.mapToken(holder,
                new LevenshteinTokenMapper(
                        new LevenshteinDistanceEnglishGreek(5, 3, 1)),
                0.6, false);

        option.mapSentences(holder, new SimpleSentenceMapper(), false);

        //option.mapSentencesDC(holder, 3, true);
        /**
         * System.out.println("> Tweak Results ... ");
         * Tweaker.filterResults(holder, 0.2);
         */
        option.runAnalysis(holder);

        System.out.println("> Write File ... ");
        OutputWriter.writeInPlotTemplateXLS(holder.getSentenceMap(),
                "plot_apollo_gr_eng");

    }

    public static void runBibleMatch() {
        System.out.println("Run BibleTextMatcher:");
        DataHolder holder = new DataHolder();
        MatchOption option = new MatchOption();

        option.sentenize(holder,
                "C:\\Users\\Bene\\Documents\\NetBeansProjects\\EntityTagger\\docs\\bibeln\\deu-neue-a.pbt", "deu",
                "C:\\Users\\Bene\\Documents\\NetBeansProjects\\EntityTagger\\docs\\bibeln\\eng-bbe.pbt", "eng",
                new BibleSentenizer(), null);

        option.defineToken(holder, new UpperCaseFilter(), new StanfordNERFilter(), 0.1, 0.1);

        option.mapToken(holder,
                new LevenshteinTokenMapper(
                        new LevenshteinDistance(5, 3, 1)),
                0.8, false);

        option.mapSentences(holder, new SimpleSentenceMapper(), true);
        //option.mapSentencesDC(holder, 3, true);

        //System.out.println("> Tweak Results ");
        //sentenceMap = Tweaker.filterResults(sentenceMap, holder, 0.2);
        option.runAnalysis(holder);

        OutputWriter.writeInPlotTemplateXLS(holder.getSentenceMap(), "bible_deu-neue_eng-bbe_full");
    }

    public static void runStandardMatch() {
        MatchOption option = new MatchOption();
        DataHolder holder = new DataHolder();

        option.sentenize(holder,
                "./docs/apollo_gr.txt", "gr",
                "./docs/apollo_eng.txt", "eng",
                new GreekSentenizer(), new LatinSentenizer());

        option.defineToken(holder, new UpperCaseFilter(), new StanfordNERFilter(), 0.0, 0.0);

        option.mapToken(holder,
                new LevenshteinTokenMapper(
                        new LevenshteinDistanceEnglishGreek(5, 3, 1)),
                0.6, false);

        option.mapSentences(holder, new SimpleSentenceMapper(), true);
        //option.mapSentencesDC(holder, 3, true);

        /**
         * System.out.println("> Tweak Results ... ");
         * Tweaker.filterResults(holder, 0.2);
         */
        option.runAnalysis(holder);

        System.out.println("> Write File");
        OutputWriter.writeInPlotTemplateXLS(holder.getSentenceMap(),
                "plot_apollo_gr_eng_dc");
    }

    public static void runSlidingWindows() {
        System.out.println("Run...\nSentenize");
        MatchOption option = new MatchOption();
        DataHolder holder = new DataHolder();

        option.sentenize(holder,
                ".//docs//apollo_gr.txt", "gr",
                ".//docs//apollo_eng.txt", "eng",
                new SlidingWindows(), null);

        option.defineToken(holder, new UpperCaseFilter(), new StanfordNERFilter(), 0.0, 0.0);

        option.mapToken(holder,
                new LevenshteinTokenMapper(
                        new LevenshteinDistanceEnglishGreek(5, 3, 1)),
                0.6, false);

        option.mapSentences(holder, new LinearSentenceMapper(), false);

        option.runAnalysis(holder);

        OutputWriter.writeInPlotTemplateXLS(holder.getSentenceMap(), "slidingWindows__apollo");
    }
}
