package org.nlp.match.SentenceMapper.DivideAndConquer;

import org.nlp.match.Tools.OutputWriter;
import org.nlp.match.General.DataHolder;
import org.nlp.match.SentenceMapper.Utility.Allocation;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.nlp.match.SentenceMapper.SentenceMapperInterface;
import org.nlp.match.SentenceMapper.SimpleSentenceMapper;
import org.structures.Tree.*;

/**
 *
 * @author Bene
 */
public class DivideAndConquerSentenceMapper {

    private final int maxDepth = 4;
    private int split;
    private DataHolder holder;
    private Tree tree;
    private ArrayList<TreeMap<Integer, Allocation>> output;
    private boolean swap;

    public TreeMap<Integer, Allocation> divideAndConquer(DataHolder holder, int split, boolean swap) {
        output = new ArrayList<>();
        this.split = split;
        this.holder = holder;
        this.swap = swap;

        DataWindow basicWindow = new DataWindow();
        basicWindow.setDepth(0);
        basicWindow.setMinIndexSentenceList1(1);
        basicWindow.setMinIndexSentenceList2(1);
        basicWindow.setWindowID(0);
        if(swap){
            basicWindow.setMaxIndexSentenceList1(holder.getSentenceList2().size());
            basicWindow.setMaxIndexSentenceList2(holder.getSentenceList1().size());
        }else{
            
            basicWindow.setMaxIndexSentenceList1(holder.getSentenceList1().size());
            basicWindow.setMaxIndexSentenceList2(holder.getSentenceList2().size());
        }
        

        tree = new Tree(basicWindow);

        conquer(tree.getRoot());
        divide(tree.getRoot());

        //return sentenceMap;
        return merge(tree.getRoot());
    }

    private void conquer(TreeNode node) {

        DataWindow currentWindow = (DataWindow) node.getNodeObject();

        //System.out.println("Window Data for " + node.getIdentifier());
        SentenceMapperInterface sentenceMapper = new SimpleSentenceMapper();
        //SentenceMapperInterface sentenceMapper = new LinearSentenceMapper();
        TreeMap<Integer, Allocation> windowSentenceMap = sentenceMapper.mapSentences(holder, swap,
                currentWindow.getMinIndexSentenceList1(), currentWindow.getMaxIndexSentenceList1(),
                currentWindow.getMinIndexSentenceList2(), currentWindow.getMaxIndexSentenceList2());

        SimpleRegression regression = computeRegression(windowSentenceMap);
        currentWindow.setRegression(regression);
        currentWindow.setMSE(regression.getMeanSquareError());
        currentWindow.setWindowSentenceMap(windowSentenceMap);

    }

    private void divide(TreeNode parent) {

        DataWindow parentWindow = (DataWindow) parent.getNodeObject();
        int windowSideLength1 = (parentWindow.getMaxIndexSentenceList1() - parentWindow.getMinIndexSentenceList1() + 1) / split - 1;//holder.getSentenceList1().size() / split -1; 
        int windowSideLength2 = (parentWindow.getMaxIndexSentenceList2() - parentWindow.getMinIndexSentenceList2() + 1) / split - 1;//holder.getSentenceList1().size() / split -1;

        int rest1 = (parentWindow.getMaxIndexSentenceList1() - parentWindow.getMinIndexSentenceList1() + 1) % split;
        int rest2;

        int xmin = parentWindow.getMinIndexSentenceList1();
        int ymin, xmax, ymax;
        int subWindowIndex = 0;

        for (int i = 0; i < split; i++) {
            ymin = parentWindow.getMinIndexSentenceList2();
            xmax = xmin + windowSideLength1;
            if (rest1 > 0) {
                xmax++;
                rest1--;
            }
            rest2 = (parentWindow.getMaxIndexSentenceList2() - parentWindow.getMinIndexSentenceList2() + 1) % split;
            for (int j = 0; j < split; j++) {
                ymax = ymin + windowSideLength2;
                if (rest2 > 0) {
                    ymax++;
                    rest2--;
                }

                DataWindow subWindow = new DataWindow();
                subWindow.setDepth(parentWindow.getDepth() + 1);

                subWindow.setMinIndexSentenceList1(xmin);
                subWindow.setMaxIndexSentenceList1(xmax);
                subWindow.setMinIndexSentenceList2(ymin);
                subWindow.setMaxIndexSentenceList2(ymax);
                subWindow.setWindowID(subWindowIndex);
                TreeNode node = new TreeNode(subWindow, parent, parent.getIdentifier() + "." + subWindowIndex);
                parent.addChildNode(node);
                conquer(node);
                if (subWindow.getWindowSentenceMap().size() > split * split * 3) {
                    divide(node);
                }
                subWindowIndex++;
                //System.out.println("Fenster " + i + "." + j + " : x=" + xmin + "-" + xmax + " y=" + ymin + "-" + ymax);
                ymin = ymax + 1;
            }
            xmin = xmax + 1;
        }
    }

    private SimpleRegression computeRegression(TreeMap<Integer, Allocation> windowSentenceMap) {
        SimpleRegression regression = new SimpleRegression();

        for (Entry entry : windowSentenceMap.entrySet()) {
            regression.addData((int) entry.getKey(), ((Allocation) entry.getValue()).getSentenceID2());
        }

        return regression;
    }

    private SimpleRegression computeRegression(ArrayList<TreeMap<Integer, Allocation>> subSentenceMapList) {
        SimpleRegression regression = new SimpleRegression();
        for (TreeMap<Integer, Allocation> subSentenceMap : subSentenceMapList) {
            for (Entry entry : subSentenceMap.entrySet()) {
                regression.addData((int) entry.getKey(), ((Allocation) entry.getValue()).getSentenceID2());
            }
        }

        return regression;
    }

    private TreeMap<Integer, Allocation> merge(TreeNode node) {
        TreeMap<Integer, Allocation> sentenceMap = new TreeMap<>();
        ArrayList<TreeMap<Integer, Allocation>> subSentenceMapList = new ArrayList<>();
        if (node.getChildren().size() == 0) {
            return ((DataWindow) node.getNodeObject()).getWindowSentenceMap();
        }

        for (TreeNode subNode : node.getChildren()) {
            TreeMap<Integer, Allocation> subSentenceMap;
            if (subNode.getChildren().size() > 0) {
                subSentenceMap = merge(subNode);
            } else {
                subSentenceMap = ((DataWindow) subNode.getNodeObject()).getWindowSentenceMap();
                ((DataWindow) subNode.getNodeObject()).setWindowSentenceMap(subSentenceMap);
            }
            OutputWriter.writeInPlotTemplateXLS(subSentenceMap, "dcmapper/" + subNode.getIdentifier());
            subSentenceMapList.add(subSentenceMap);

            if (((DataWindow) subNode.getNodeObject()).getDepth() == 1) {
                //System.out.println(subNode.getIdentifier());
                output.add(subSentenceMap);
            }
        }

        SimpleRegression regression = ((DataWindow)node.getNodeObject()).getRegression();//computeRegression(subSentenceMapList);

        for (TreeMap<Integer, Allocation> subSentenceMap : subSentenceMapList) {
            for (Entry<Integer, Allocation> entry : subSentenceMap.entrySet()) {
                int key1 = entry.getKey();
                if (sentenceMap.containsKey(key1)) {
                    Allocation oldAllocation = sentenceMap.get((int) entry.getKey());
                    Allocation allocation = entry.getValue();
                    double predictedValue = regression.predict(key1);
                    double diffCurrent = Math.abs(predictedValue - allocation.getSentenceID2());
                    double diffOld = Math.abs(predictedValue - oldAllocation.getSentenceID2());
                    if (diffCurrent > diffOld) {
                        continue;
                    } else if (diffCurrent < diffOld) {
                        sentenceMap.put((int) entry.getKey(), allocation);
                        //System.out.println("Replace for: " + (int) entry.getKey() + " new: " + allocation.getSentenceID2() + ", old: " + oldAllocation.getSentenceID2() + " bc of Regression: " + diffCurrent + " vs " + diffOld);
                    } else {
                        if (allocation.getAllocationRating() > oldAllocation.getAllocationRating()) {
                            sentenceMap.put((int) entry.getKey(), allocation);
                            //System.out.println("Replace for: " + (int) entry.getKey() + " " + allocation.getSentenceID2() + "-" + oldAllocation.getSentenceID2() + " bc of Rating: " + allocation.getAllocationRating() + " vs " + oldAllocation.getAllocationRating());
                        } else {
                            continue;
                        }
                    }
                } else {
                    sentenceMap.put((int) entry.getKey(), (Allocation) entry.getValue());
                }
            }
        }
        OutputWriter.writeMultiPlot(output, "test");
        return sentenceMap;
    }

}
