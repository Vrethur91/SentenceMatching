package org.nlp.match.SentenceMapper;

import java.util.ArrayList;

/**
 *
 * @author Bene
 */
public class TopAllocationList {

    private ArrayList<Allocation> topAllocations;
    private ArrayList<Double> topRatings;

    public TopAllocationList() {
        topAllocations = new ArrayList<>();
        topRatings = new ArrayList<>();
    }

    public ArrayList<Allocation> getTopMatches() {
        return topAllocations;
    }
    
    public ArrayList<Integer> getTopIndizes(){
        ArrayList<Integer> returnList = new ArrayList<>();
        for(int i = 0;i<topAllocations.size();i++){
            returnList.add(topAllocations.get(i).getSentenceID2());
        }
        return returnList;
    }

    public void compareAndAddMatch(Allocation allocation, double rating) {
        if (topRatings.size() == 0) {
            topRatings.add(rating);
            topAllocations.add(allocation);
        } else if (topRatings.size() == 1) {
            if (rating > topRatings.get(0)) {
                topRatings.add(topRatings.get(0));
                topAllocations.add(topAllocations.get(0));
                
                topRatings.set(0, rating);
                topAllocations.set(0, allocation);
            } else if ((rating == topRatings.get(0)) && (allocation.getAvgTokenSimilarityRating() > topAllocations.get(0).getAvgTokenSimilarityRating())) {
                topRatings.add(topRatings.get(0));
                topAllocations.add(topAllocations.get(0));
                
                topRatings.set(0, rating);
                topAllocations.set(0, allocation);
            } else {
                topRatings.add(rating);
                topAllocations.add(allocation);
            }
        } else if (topRatings.size() == 2) {
            if (rating > topRatings.get(0)) {
                topRatings.add(topRatings.get(1));
                topAllocations.add(topAllocations.get(1));
                
                topRatings.set(1, topRatings.get(0));
                topAllocations.set(1, topAllocations.get(0));
                
                topRatings.set(0, rating);
                topAllocations.set(0, allocation);
            } else if ((rating == topRatings.get(0)) && (allocation.getAvgTokenSimilarityRating() > topAllocations.get(0).getAvgTokenSimilarityRating())) {
                topRatings.add(topRatings.get(1));
                topAllocations.add(topAllocations.get(1));
                
                topRatings.set(1, topRatings.get(0));
                topAllocations.set(1, topAllocations.get(0));
                
                topRatings.set(0, rating);
                topAllocations.set(0, allocation);
            } else if (rating > topRatings.get(1)) {
                topRatings.add(topRatings.get(1));
                topAllocations.add(topAllocations.get(1));
                
                topRatings.set(1, rating);
                topAllocations.set(1, allocation);
            } else if ((rating == topRatings.get(1)) && (allocation.getAvgTokenSimilarityRating() > topAllocations.get(1).getAvgTokenSimilarityRating())) {
                topRatings.add(topRatings.get(1));
                topAllocations.add(topAllocations.get(1));
                
                topRatings.set(1, rating);
                topAllocations.set(1, allocation);
            } else{
                topRatings.add(rating);
                topAllocations.add(allocation);
            }
        } else if (topRatings.size() >= 3) {
            if (rating > topRatings.get(0)) {
                addToLists(allocation, rating, 0);
            } else if ((rating == topRatings.get(0)) && (allocation.getAvgTokenSimilarityRating() > topAllocations.get(0).getAvgTokenSimilarityRating())) {
                addToLists(allocation, rating, 0);
            } else if (rating > topRatings.get(1)) {
                addToLists(allocation, rating, 1);
            } else if ((rating == topRatings.get(1)) && (allocation.getAvgTokenSimilarityRating() > topAllocations.get(1).getAvgTokenSimilarityRating())) {
                addToLists(allocation, rating, 1);
            } else if (rating > topRatings.get(2)) {
                addToLists(allocation, rating, 2);
            } else if ((rating == topRatings.get(2)) && (allocation.getAvgTokenSimilarityRating() > topAllocations.get(2).getAvgTokenSimilarityRating())) {
                addToLists(allocation, rating, 2);
            }
        }
    }

    private void addToLists(Allocation allocation, double rating, int pos) {
        switch (pos) {
            case 0:
                if (topRatings.size() >= 2) {
                    topRatings.set(2, topRatings.get(1));
                    topAllocations.set(2, topAllocations.get(1));
                }
                if (topRatings.size() >= 1) {
                    topRatings.set(1, topRatings.get(0));
                    topAllocations.set(1, topAllocations.get(0));
                }

                topRatings.set(0, rating);
                topAllocations.set(0, allocation);
                break;
            case 1:
                if (topRatings.size() >= 2) {
                    topRatings.set(2, topRatings.get(1));
                    topAllocations.set(2, topAllocations.get(1));
                }

                topRatings.set(1, rating);
                topAllocations.set(1, allocation);
                break;
            case 2:
                topRatings.set(2, rating);
                topAllocations.set(2, allocation);
                break;
        }
    }
}
