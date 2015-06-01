package org.nlp.match.SentenceMapper;

import java.util.TreeMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author Bene
 */
public class DataWindow {
    private int windowID;
    private int depth;
    private int minIndexSentenceList1;
    private int maxIndexSentenceList1;
    private int minIndexSentenceList2;
    private int maxIndexSentenceList2;
    private SimpleRegression regression;
    private double mSE;
    private TreeMap<Integer, Allocation> windowSentenceMap;

    /**
     * @return the windowID
     */
    public int getWindowID() {
        return windowID;
    }

    /**
     * @param windowID the windowID to set
     */
    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @param depth the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @return the minIndexSentenceList1
     */
    public int getMinIndexSentenceList1() {
        return minIndexSentenceList1;
    }

    /**
     * @param minIndexSentenceList1 the minIndexSentenceList1 to set
     */
    public void setMinIndexSentenceList1(int minIndexSentenceList1) {
        this.minIndexSentenceList1 = minIndexSentenceList1;
    }

    /**
     * @return the maxIndexSentenceList1
     */
    public int getMaxIndexSentenceList1() {
        return maxIndexSentenceList1;
    }

    /**
     * @param maxIndexSentenceList1 the maxIndexSentenceList1 to set
     */
    public void setMaxIndexSentenceList1(int maxIndexSentenceList1) {
        this.maxIndexSentenceList1 = maxIndexSentenceList1;
    }

    /**
     * @return the minIndexSentenceList2
     */
    public int getMinIndexSentenceList2() {
        return minIndexSentenceList2;
    }

    /**
     * @param minIndexSentenceList2 the minIndexSentenceList2 to set
     */
    public void setMinIndexSentenceList2(int minIndexSentenceList2) {
        this.minIndexSentenceList2 = minIndexSentenceList2;
    }

    /**
     * @return the maxIndexSentenceList2
     */
    public int getMaxIndexSentenceList2() {
        return maxIndexSentenceList2;
    }

    /**
     * @param maxIndexSentenceList2 the maxIndexSentenceList2 to set
     */
    public void setMaxIndexSentenceList2(int maxIndexSentenceList2) {
        this.maxIndexSentenceList2 = maxIndexSentenceList2;
    }

    /**
     * @return the MSE
     */
    public double getMSE() {
        return mSE;
    }

    /**
     * @param mSE the MSE to set
     */
    public void setMSE(double mSE) {
        this.mSE = mSE;
    }

    /**
     * @return the windowSentenceMap
     */
    public TreeMap<Integer, Allocation> getWindowSentenceMap() {
        return windowSentenceMap;
    }

    /**
     * @param windowSentenceMap the windowSentenceMap to set
     */
    public void setWindowSentenceMap(TreeMap<Integer, Allocation> windowSentenceMap) {
        this.windowSentenceMap = windowSentenceMap;
    }

    /**
     * @return the regression
     */
    public SimpleRegression getRegression() {
        return regression;
    }

    /**
     * @param regression the regression to set
     */
    public void setRegression(SimpleRegression regression) {
        this.regression = regression;
    }
}
