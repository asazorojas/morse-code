/*
 * Copyright (C) 2016 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Michael <GrubenM@GMail.com>
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MorseCodeDecoder {
    private static int tu;
    private static float thresh13;
    private static float thresh37;
    
     /**
     * Given a string of ones and its following strings of zeros,
     * returns the Morse symbol (e.g. dot, dash, new-letter, new-word)
     * which these ones and zeros signify).
     * This method works even when there is some variation in the length
     * of the time unit used.
     * 
     * @param one
     * @param zero
     * @return 
     */
    private static String nextTeleFuzzy(String one, String zero) {
        String tele = nextTeleFuzzy(one);
        if ((zero.length() >= thresh13) && (zero.length() < thresh37)) tele += " ";
        else if (zero.length() >= thresh37) tele += "   ";
        return tele;
    }
    
    /**
     * Given a string of ones, returns the Morse symbol
     * (i.e. dot or dash) which these ones signify.
     * This method works even when there is some variation in the length
     * of the time unit used.
     * 
     * @param one
     * @return 
     */
    private static String nextTeleFuzzy(String one) {
        String tele = "";
        if (one.length() <= thresh13) tele += ".";
        else tele += "-";
        return tele;
    }
    
    /**
     * Given a string of bits, which may or may not begin or end with '0's,
     * and which may have some variation in the length of the time unit used,
     * returns the Morse Code translation of this message.
     * @param bits
     * @return 
     */
    public static String decodeBitsAdvanced(String bits) {
        String morse = "";
        bits = bits.replaceAll("^[0]+", "");
        bits = bits.replaceAll("[0]+$", "");
        KMeans km = new KMeans(bits, 3);
        km.converge();
        thresh13 = (km.getTimeUnit(0) + km.getTimeUnit(1)) / 2;
        thresh37 = (km.getTimeUnit(1) + km.getTimeUnit(2)) / 2;
        if (bits.length() > 5) {
            thresh13 *= 1.2;
            thresh37 *= 1.1;
        }
        String[] ones = bits.split("0+");
        String[] zeros = bits.split("1+");
        for (int i = 0; i < zeros.length - 1; i++) {
            morse += nextTeleFuzzy(ones[i], zeros[i + 1]);
        }
        if (ones[0].length() > 0) morse += nextTeleFuzzy(ones[ones.length - 1]);
        return morse;
    }
    
    /**
     * Given a string in Morse Code, returns the English translation.
     * 
     * @param morseCode
     * @return 
     */
    public static String decodeMorse(String morseCode) {
        String results = "";
        morseCode = morseCode.trim().replaceAll(" {3}", " SPACE ");
        // Here, we'd like to trim leading and trailing whitespace.
        // We also know that three spaces are used to separate words.
        // Hence, we leave a symbol that can be tokenized for our while
        // loop to recognize as a [space] character.

        Scanner sc = new Scanner(morseCode);
        while (sc.hasNext()) { 
          String nxt = sc.next();
          if (nxt.equals("SPACE")) results += " ";
          else results += MorseCode.get(nxt);
        }
        return results;
    }
    
        /**
     * The Cluster class provides data structures and methods for clusters
     * in the KMeans algorithm.
     */
    private static class Cluster implements Comparable<Cluster> {
        private float location;
        private float centroid;
        private ArrayList<Integer> currentPoints = new ArrayList<>();
        private ArrayList<Integer> previousPoints = new ArrayList<>();
        
        /**
         * Constructors
         */
        private Cluster(float loc) {
            location = loc;
        }
        
        private Cluster() {
            location = -1;
        }
        
        /**
         * Methods for claiming currentPoints and calculating centroid.
         */
        private void addPoint(int i) {
            currentPoints.add(i);
        }
        
        private boolean didChange() {
            if (previousPoints.size() != currentPoints.size()) return true;
            else return !currentPoints.equals(previousPoints);
        }
        
        private void clearPoints() {
            previousPoints = (ArrayList<Integer>) currentPoints.clone();
            currentPoints.clear();
        }
        
        /**
         * After new points have been assigned to this cluster, this method
         * calculates the new centroid of the cluster and moves the cluster
         * to that location.
         */
        private void update() {
            float sum = 0;
            for (Integer p: currentPoints) {
                sum += p;
            }
            centroid = sum / currentPoints.size();
            setLocation(centroid);
        }
                
        /**
         * Getters and Setters.
         */
        private float getLocation() { return location; }
        private float getDistance(int point) {
            return Math.abs(location - point);
        }
        private void setLocation(float loc) { location = loc; }
        
        @Override
        public int compareTo(Cluster t) {
            if (this.getLocation() > t.getLocation()) return 1;
            else if (this.getLocation() < t.getLocation()) return -1;
            else return 0;
        }
    }
    
    private static class KMeans {    
        /**
         * KMeans attributes.
         */
        private boolean converged = false;
        private final Cluster[] clusters;
        private final String[] bitCollection; // for generating frequency dist.
        private float[] timeUnits = {0, 0, 0};
        private final HashMap<Integer, Integer> dist = new HashMap<>();
        List<Integer> keys;

        public KMeans(String stream, int numClusters) {
            this.clusters = new Cluster[numClusters];
            stream = stream.replaceAll("^[0]+", ""); // remove leading 0s
            stream = stream.replaceAll("[0]+$", ""); // remove trailing 0s

            /**
             * The following if/else block populates this.bitCollection.
             */
            if (stream.length() == 0) {
                bitCollection = new String[1];
                bitCollection[0] = "";
            }
            else {
                String[] ones = stream.split("0+");
                String[] zeros = stream.split("1+");

                if (zeros.length == 0) {
                    bitCollection = new String[1];
                    bitCollection[0] = ones[0];
                }
                else {
                    bitCollection = new String[ones.length + zeros.length - 1];
                    for (int i = 0; i < ones.length - 1; i++) {
                        bitCollection[2*i] = ones[i];
                        bitCollection[2*i+1] = zeros[i+1];
                    }
                    bitCollection[bitCollection.length - 1] = ones[ones.length - 1];
                }
            }

            /**
             * The following for loop populates the this.dist HashMap.
             */
            for (int i = 0; i < bitCollection.length; i++) {
                int l = bitCollection[i].length();
                if (!dist.containsKey(l)) {
                    dist.put(l, 1);
                }
                else dist.put(l, dist.get(l) + 1);
            }
            this.keys = new ArrayList<>(dist.keySet());
                        
            if (keys.size() == 1 || keys.size() == 2) {
                timeUnits[0] = keys.get(0);
                timeUnits[1] = keys.get(0) * 3;
                timeUnits[2] = keys.get(0) * 7;
                converged = true;
            }
            else {
                Collections.sort(keys);
                initializeClusters();
            }
        }

        /**
         * Populates this.clusters with this.numClusters Cluster objects,
         * whose initial locations are from this.keys (the minimum, the
         * maximum, and the middle between the two).
         */
        private void initializeClusters() {
            clusters[0] = new Cluster(keys.get(0));
            clusters[2] = new Cluster(keys.get(keys.size() - 1));
            clusters[1] = new Cluster(
                (keys.get(keys.size() - 1) + keys.get(0)) / 2 + 1);
        }

        /**
         * Assigns cluster-labels to each length-point from the fuzzy input,
         * which is subsequently used by the clusters to re-calculate their
         * centroids and move accordingly.
         */
        public void assignToClosestCluster() {
            clear();
            for (Integer i: keys) {
                Cluster bestCluster = new Cluster();
                float closest = Float.MAX_VALUE;
                for (Cluster c: clusters) {
                    float d = c.getDistance(i);
                    if (d < closest) {
                        closest = d;
                        bestCluster = c;
                    }
                }
                for(int j = 0; j < dist.get(i); j++) {
                    bestCluster.addPoint(i);
                }
            }
        }

        /**
         * Populates this.timeUnits[] with the first, second, and third cluster
         * means, representing the average length of 1 time unit,
         * 3 time units, and 7 time units respectively.
         */
        public void calculateTimeUnits() {
            Cluster[] sortedClusters = clusters.clone();
            Arrays.sort(sortedClusters);
            timeUnits[0] = sortedClusters[0].getLocation();
            timeUnits[1] = sortedClusters[1].getLocation();
            timeUnits[2] = sortedClusters[2].getLocation();
        }

        public void clear() {
            for (Cluster c: clusters) c.clearPoints();
        }

        /**
         * Assigns the closest Cluster to each point, calculates the centroid
         * for those Clusters based off of those points, moves the Clusters
         * to their respective centroids, and repeats until assignment on the next
         * iteration is the same.
         */
        public void converge() {
            if (!converged) {
                assignToClosestCluster();
                while (!converged) {
                    update();
                    assignToClosestCluster();
                    if (!didChange()) converged = true;
                }
                calculateTimeUnits();
            }
        }

        public boolean didChange() {
            for (Cluster c: clusters) if (c.didChange()) return true;
            return false;
        }

        public void update() {
            for (Cluster c: clusters) c.update();
        }

        /**
         * Getters and Setters.
         *
         */
        public float getTimeUnit(int index) { return this.timeUnits[index]; }
    }
    
    public static void main(String[] args) {
        String fuzzyBits = "00000000000000011111111000000011111111111100000000000111111111000001111111110100000000111111111111011000011111111011111111111000000000000000000011111111110000110001111111111111000111000000000001111111111110000111111111100001100111111111110000000000111111111111011100001110000000000000000001111111111010111111110110000000000000001111111111100001111111111110000100001111111111111100000000000111111111000000011000000111000000000000000000000000000011110001111100000111100000000111111111100111111111100111111111111100000000011110011111011111110000000000000000000000111111111110000000011111000000011111000000001111111111110000000001111100011111111000000000111111111110000011000000000111110000000111000000000011111111111111000111001111111111001111110000000000000000000001111000111111111100001111111111111100100000000001111111100111111110111111110000000011101111111000111000000001001111111000000001111111111000000000111100001111111000000000000011111111100111111110111111111100000000000111111110000001100000000000000000000111111101010000010000001111111100000000011111000111111111000000111111111110011111111001111111110000000011000111111110000111011111111111100001111100001111111100000000000011110011101110001000111111110000000001111000011111110010110001111111111000000000000000000111111111110000000100000000000000000011110111110000001000011101110000000000011111111100000011111111111100111111111111000111111111000001111111100000000000001110111111111111000000110011111111111101110001111111111100000000111100000111100000111111111100000111111111111000000011111111000000000001000000111100000001000001111100111111111110000000000000000000010001111111100000011111111100000000000000100001111111111110111001111111111100000111111100001111111111000000000000000000000000011100000111111111111011110000000010000000011111111100011111111111100001110000111111111111100000000000000111110000011111001111111100000000000011100011100000000000011111000001111111111101000000001110000000000000000000000000000111110010000000000111111111000011111111110000000000111111111111101111111111100000000010000000000000011111111100100001100000000000000111100111100000000001100000001111111111110000000011111111111000000000111100000000000000000000111101111111111111000000000001111000011111000011110000000001100111111100111000000000100111000000000000111110000010000011111000000000000001111111111100000000110111111111100000000000000111111111111100000111000000000111111110001111000000111111110111111000000001111000000000010000111111111000011110001111111110111110000111111111111000000000000000000000000111111111110000000111011111111100011111110000000001111111110000011111111100111111110000000001111111111100111111111110000000000110000000000000000001000011111111110000000001111111110000000000000000000000011111111111111000000111111111000001111111110000000000111111110000010000000011111111000011111001111111100000001110000000011110000000001011111111000011111011111111110011011111111111000000000000000000100011111111111101111111100000000000000001100000000000000000011110010111110000000011111111100000000001111100011111111111101100000000111110000011110000111111111111000000001111111111100001110111111111110111000000000011111111101111100011111111110000000000000000000000000010000111111111100000000001111111110111110000000000000000000000110000011110000000000001111111111100110001111111100000011100000000000111110000000011111111110000011111000001111000110000000011100000000000000111100001111111111100000111000000001111111111000000111111111100110000000001111000001111111100011100001111111110000010011111111110000000000000000000111100000011111000001111000000000111111001110000000011111111000100000000000011111111000011001111111100000000000110111000000000000111111111111000100000000111111111110000001111111111011100000000000000000000000000";
//        MorseCodeDecoder.getTimeUnit(bits);
        String morse = MorseCodeDecoder.decodeBitsAdvanced(fuzzyBits);
        System.out.println(morse);
        String msg = MorseCodeDecoder.decodeMorse(morse);
        System.out.println(msg);
    }
}
