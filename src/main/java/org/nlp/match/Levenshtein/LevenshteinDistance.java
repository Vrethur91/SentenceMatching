package org.nlp.match.Levenshtein;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;



/**
 * Frei Verfügbar auf:
 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
 * Modifiziert von Benedict Preßler
 */
public class LevenshteinDistance {

    int high = 1;
    int mid = 1;
    int low = 1;
    String s0;
    String s1;
    int[][] caseMatrix;
    
    public LevenshteinDistance(){
        
    }
    
    public LevenshteinDistance(int costHigh, int costMid, int costLow){
        this.high = costHigh;
        this.mid = costMid;
        this.low = costLow;
    }
    
    public double levenshteinDistance(String s0, String s1) {
        this.s0 = s0;
        this.s1 = s1;
        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;
        caseMatrix = new int[len0][len1];

        // the array of distances                                                       
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        int maxCost = 0;
        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0; i++) {
            //cost[i] = i;
            if (i == 0) {
                cost[i] = 0;
            } else {
                int n = (int) Math.ceil(len0 / 3.0);
                int m = i / n;
                switch (m) {
                    case 0:
                        cost[i] = cost[i - 1] + high;
                        break;
                    case 1:
                        cost[i] = cost[i - 1] + mid;
                        break;
                    case 2:
                        cost[i] = cost[i - 1] + low;
                        break;
                    default:
                        cost[i] = cost[i - 1] + low;
                }
            }
            if(len0>=len1) {
                //System.out.println("len0 >= len1: "+ maxCost+ " + " + cost[i]);
                maxCost = cost[i];
            }
            //System.out.println("i="+i+" : "+cost[i]);

        }

        // dynamicaly computing the array of distances                                  
        // transformation cost for each letter in s1  
        int costtemp = 0;
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1                             
            //newcost[0] = j;
            if (j == 1) {
                newcost[0] = 5;
            } else {
                int n = (int) Math.ceil(len1 / 3.0);
                int m = j / n;
                switch (m) {
                    case 0:
                        newcost[0] = costtemp + high;
                        break;
                    case 1:
                        newcost[0] = costtemp + mid;
                        break;
                    case 2:
                        newcost[0] = costtemp + low;
                        break;
                    default:
                        newcost[0] = costtemp + low;
                }
                costtemp = newcost[0];
            }
            costtemp = newcost[0];
            if(len0 < len1){
                //System.out.println("len0 < len1: "+ maxCost+ " + " + newcost[0]);
                maxCost = newcost[0];
                
            }
            //System.out.println("j="+j+" i="+0+" : "+newcost[0]);
            // transformation cost for each letter in s0                                
            for (int i = 1; i < len0; i++) {
                int n = 0;
                int m = 0;
                int var = 0;
                if (i == j) {
                    n = (int) Math.ceil(Math.max(len0, len1) / 3.0);
                    m = i / n;
                } else {
                    if (j > i) {
                        n = (int) Math.ceil(len1 / 3.0);
                        m = j / n;
                    } else {
                        n = (int) Math.ceil(len0 / 3.0);
                        m = i / n;
                    }
                }
                switch (m) {
                    case 0:
                        var = high;
                        break;
                    case 1:
                        var = mid;
                        break;
                    case 2:
                        var = low;
                        break;
                    default:
                        var = low;
                }

                // matching current letters in both strings                             
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : var;
                
                //special cases
                int sc = this.getSpecialCase(i-1,j-1);
                if(sc != -1){
                    //System.out.println("DA?");
                    match = sc;
                }
                if(caseMatrix[i-1][j-1] != 0)
                {
                    match = 0;
                    var = 0;
                }
                //System.out.println("Match: "+match);
                // computing cost for each transformation                               
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + var;
                int cost_delete = newcost[i - 1] + var;

                // keep minimum cost                                                    
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
                //System.out.println("j="+j+" i="+i+" : "+newcost[i]);
            }

            // swap cost/newcost arrays                                                 
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings 
        double rating = (maxCost-cost[len0-1]) / (double)maxCost;
        DecimalFormat df = new DecimalFormat("0.00");
        rating = Double.parseDouble(df.format(rating).replace(',', '.'));
        return rating;
    }
    
    public int getSpecialCase(int i, int j) {
        return -1;
    }
}
