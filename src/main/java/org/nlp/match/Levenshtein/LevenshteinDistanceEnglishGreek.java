package org.nlp.match.Levenshtein;

/**
 *
 * @author Bene
 */
public class LevenshteinDistanceEnglishGreek extends LevenshteinDistance {
    
    public LevenshteinDistanceEnglishGreek(int costHigh, int costMid, int costLow){
        super.high = costHigh;
        super.mid = costMid;
        super.low = costLow;
    }
    
    public LevenshteinDistanceEnglishGreek() {
        
    }
    
    public double levenshteinDistance(String s0, String s1){
        return super.levenshteinDistance(s0, s1);
    }
    
    public int getSpecialCase(int i0, int i1) {
        int match;
        switch (s0.charAt(i0)) {
            case 'a':
                match = lowerCaseA(i0, i1);
                break;
            case 'c': 
                match = lowerCaseC(i0, i1);
                break;
            case 'e': 
                match = lowerCaseE(i0, i1);
                break;
            case 'g': 
                match = lowerCaseG(i0, i1);
                break;
            case 'i': 
                match = lowerCaseI(i0, i1);
                break;
            case 'k': 
                match = lowerCaseK(i0, i1);
                break;
            case 'm':
                match = lowerCaseM(i0, i1);
                break;
            case 'n': 
                match = lowerCaseN(i0, i1);
                break;
            case 'o':
                match = lowerCaseO(i0, i1);
                break;
            case 'u':
                match = lowerCaseU(i0, i1);
                break;
            default:
                match = -1;
        }
        return match; 
    }
    
    private int lowerCaseA(int i0, int i1) {
        if ((s1.charAt(i1) == 'e') && (i0 != s0.length() - 1)) {
            if (s0.charAt(i0 + 1) == 'i') {     //ai - e
                caseMatrix[i0 + 1][i1] = 1;
                return 1;
            }
        }

        return -1;
    }
    
    private int lowerCaseC(int i0, int i1) {
        if (s1.charAt(i1) == 'k') {     //c - k
            return 0;
        }

        return -1;
    }
    
    private int lowerCaseE(int i0, int i1) {
        if ((s1.charAt(i1) == 'i') && (i0 != s0.length() - 1)) {    
            if (s0.charAt(i0 + 1) == 'i') {     //ei - i
                caseMatrix[i0 + 1][i1] = 1;
                return 1;
            }
        } else if ((s1.charAt(i1) == 'a') && (i1 != s1.length() - 1)) {  
            if (s1.charAt(i1 + 1) == 'i') {     //e - ai
                caseMatrix[i0][i1 + 1] = 1;
                return 1;
            }
        } else if ((s1.charAt(i1) == 'e') && (i0 != s0.length() - 1) && (i1 != s1.length() - 1)) {
            if ((s0.charAt(i0 + 1) == 'y') && ((s1.charAt(i1 + 1) == 'f') || (s1.charAt(i1 + 1) == 'v'))) { //ey - ef, ev
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            } else if ((s0.charAt(i0 + 1) == 'f') && ((s1.charAt(i1 + 1) == 'y') || (s1.charAt(i1 + 1) == 'v'))) { //ef - ey, ev
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            } else if ((s0.charAt(i0 + 1) == 'v') && ((s1.charAt(i1 + 1) == 'y') || (s1.charAt(i1 + 1) == 'f'))) { //ev - ey, ef
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            }
        }

        return -1;
    }
    
    private int lowerCaseG(int i0, int i1) {
        if ((s1.charAt(i1) == 'n') && (i0 != s0.length() - 1) && (i1 != s1.length() - 1)) {
            if ((s1.charAt(i1 + 1) == 'g') && (s0.charAt(i0) == 'g')) {     //gg - ng
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            } 
        } 
        
        return -1;
    }
    
    private int lowerCaseI(int i0, int i1) {
        if ((s1.charAt(i1) == 'e') && (i1 != s1.length() - 1)) {
            if (s1.charAt(i1 + 1) == 'i') {     //i - ei
                caseMatrix[i0][i1 + 1] = 1;
                return 1;
            }
        } else if ((s1.charAt(i1) == 'o') && (i1 != s1.length() - 1)) {
            if (s1.charAt(i1 + 1) == 'i') {     //i - oi
                caseMatrix[i0][i1 + 1] = 1;
                return 1;
            }
        }
        
        return -1;
    }
    
    private int lowerCaseK(int i0, int i1) {
        if (s1.charAt(i1) == 'c') {     //k - c
            return 0;
        } 
        
        return -1;
    }
    
    private int lowerCaseM(int i0, int i1) {
        if ((s1.charAt(i1) == 'm') && (i0 != s0.length() - 1) && (i1 != s1.length() - 1)) {
            if ((s0.charAt(i0 + 1) == 'p') && (s1.charAt(i1 + 1) == 'b')) {     //mp - mb
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            } else if ((s0.charAt(i0 + 1) == 'b') && (s1.charAt(i1 + 1) == 'p')) {      //mb - mp
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            }
        }
        
        return -1;
    }
    
    private int lowerCaseN(int i0, int i1) {
        if ((s1.charAt(i1) == 'g') && (i0 != s0.length() - 1) && (i1 != s1.length() - 1)) {
            if ((s0.charAt(i0 + 1) == 'g') && (s1.charAt(i1 + 1) == 'g')) {     //ng - gg
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            }
        } else if ((s1.charAt(i1) == 'n') && (i0 != s0.length() - 1) && (i1 != s1.length() - 1)) {
            if ((s0.charAt(i0 + 1) == 't') && (s1.charAt(i1 + 1) == 'd')) {     //nt - nd
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            } else if ((s0.charAt(i0 + 1) == 'd') && (s1.charAt(i1 + 1) == 't')) {      //nd - nt
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            }
        }
        
        return -1;
    }
    
    private int lowerCaseO(int i0, int i1) {
        if ((s1.charAt(i1) == 'i') && (i0 != s0.length() - 1)) {
            if (s0.charAt(i0 + 1) == 'i') {     //oi - i
                caseMatrix[i0 + 1][i1] = 1;
                return 1;
            }
        } else if ((s1.charAt(i1) == 'o') && (i0 != s0.length() - 1) && (i1 != s1.length() - 1)) {
            if ((s0.charAt(i0 + 1) == 'y') && (s1.charAt(i1 + 1) == 'u')) {     //oy - ou
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            } else if ((s0.charAt(i0 + 1) == 'u') && (s1.charAt(i1 + 1) == 'y')) {      //ou - oy
                caseMatrix[i0 + 1][i1 + 1] = 1;
                return 1;
            }
        } else if ((s1.charAt(i1) == 'u') && (i0 != s0.length() - 1)) {
            if ((s0.charAt(i0 + 1) == 'y') || (s0.charAt(i0 + 1) == 'u')) {     //oy, ou - u
                caseMatrix[i0 + 1][i1] = 1;
                return 1;
            }
        }
        
        return -1;
    }

    private int lowerCaseU(int i0, int i1) {
        if ((s1.charAt(i1) == 'o') && (i1 != s1.length() - 1)) {
            if ((s1.charAt(i1 + 1) == 'y') || (s1.charAt(i1 + 1) == 'u')) {
                caseMatrix[i0][i1+1] = 1;
                return 1;
            }
        }
        return -1;
    }
}
