
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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
public class KMeans {
    
    /**
     * This class provides data structures and methods for clusters
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
        
        /**
         * Printers.
         */
        private void printCentroid() { System.out.println(centroid); }
        private void printLocation() { System.out.println(location); }
        private void printPoints() {
            for (Integer p: currentPoints)
                System.out.print(p + " ");
            System.out.println();
        }

        @Override
        public int compareTo(Cluster t) {
            if (this.getLocation() > t.getLocation()) return 1;
            else if (this.getLocation() < t.getLocation()) return -1;
            else return 0;
        }
    }
    
    /**
     * KMeans attributes.
     */
    private final Cluster[] clusters;
    private final String[] bitCollection; // for generating frequency dist.
    private float[] timeUnits = {0, 0, 0};
    private final HashMap<Integer, Integer> dist = new HashMap<>();
    Random rand = new Random();
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
        initializeClusters();
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
     * Populates this.clusters with this.numClusters Cluster objects,
     * whose initial locations are randomly chosen from this.keys
     * without replacement.
     */
    private void initializeClustersRandomly() {
        Set<Integer> picked = new HashSet<>();
        int j = 0;
        while (picked.size() < clusters.length) {
            int t = keys.get(rand.nextInt(keys.size()));
            if (!picked.contains(t)) {
                picked.add(t);
                clusters[j] = new Cluster(t);
                j++;
            }
        }
    }

    /**
     * Sets the location of each Cluster in this.clusters to
     * a location randomly chosen from this.keys without replacement.
     */
    private void randomizeClusters() {
        Set<Integer> picked = new HashSet<>();
        int j = 0;
        while (picked.size() < clusters.length) {
            int t = keys.get(rand.nextInt(keys.size()));
            if (!picked.contains(t)) {
                picked.add(t);
                clusters[j].setLocation(t);
                j++;
            }
        }
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
        assignToClosestCluster();
        do {
            update();
            assignToClosestCluster();
        } while (didChange());
        calculateTimeUnits();
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
    public Cluster[] getClusters() { return this.clusters; }
    public float getTimeUnit(int index) { return this.timeUnits[index]; }
    
    /**
     * Printers.
     */
    public void printBitCollection() {
        for (String s: bitCollection) System.out.println(s);
    }
    public void printClusterPoints() {
        for (Cluster c: clusters) {
            System.out.println("Points for cluster at " + c.getLocation());
            c.printPoints();
        }
    }
    public void printClusters() {
        for (Cluster c: clusters) {
            System.out.println(c.getLocation());
        }
    }
    public void printDidChange() {
        System.out.println(didChange());
    }
    public void printDistances() {
        for (Integer i: keys) {
            float best = -1;
            float closest = Float.MAX_VALUE;
            for (Cluster c: clusters) {
                System.out.print("From cluster at " + c.getLocation());
                System.out.print(" to point at " + i + " is: ");
                float d = c.getDistance(i);
                System.out.println(d);
                if (d < closest) {
                    closest = d;
                    best = c.getLocation();
                }
            }
            System.out.println("Closest to: " + best);
        }
    }
    public void printDistribution() {
        for (Entry<Integer, Integer> e: dist.entrySet())
            System.out.println("Length: " + e.getKey() + 
                    " occurred " + e.getValue() + " times");
    }
    public void printKeys() {
        for (Integer i: keys) System.out.println(i);
    }
    
    public static void main(String[] args) {
        KMeans km = new KMeans("0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000", 3);
        km.converge();
        System.out.println(km.getTimeUnit(0));
        System.out.println(km.getTimeUnit(1));
        System.out.println(km.getTimeUnit(2));
    }
}
