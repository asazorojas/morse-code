
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

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
     * This class provides data structures and methods for numClusters
 in the KMeans algorithm.
     */
    private static class Cluster {
        int currentLocation;
        int previousLocation;
        private Cluster(int loc) {
            currentLocation = loc;
        }
        private void printLocation() { System.out.println(currentLocation); }
        private int getLocation() { return currentLocation; }
        private void setLocation(int loc) {
            previousLocation = currentLocation;
            currentLocation = loc;
        }
        
        private int getDistance(int point) {
            return Math.abs(currentLocation - point);
        }
    }
    
    private final String stream;
    private final Cluster[] clusters;
    private final Random rand;
    private final String[] bitCollection;
    private final int tu = -1;
    
    public KMeans(String stream, int numClusters) {
        this.stream = stream;
        this.clusters = new Cluster[numClusters];
        this.rand = new Random();
        
        stream = stream.replaceAll("^[0]+", ""); // remove leading 0s
        stream = stream.replaceAll("[0]+$", ""); // remove trailing 0s
        
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
    }
    
    public void setDistribution() {
        HashMap<Integer, Integer> h = new HashMap<>();
        for (int i = 0; i < bitCollection.length; i++) {
            int l = bitCollection[i].length();
            if (!h.containsKey(l)) {
                h.put(l, 1);
            }
            else h.put(l, h.get(l) + 1);
        }
        for (Entry<Integer, Integer> e: h.entrySet()) {
            System.out.println("Length: " + e.getKey() + 
                    " occured " + e.getValue() + " times");
        }
    }
    
    public void printBitCollection() {
        for (String s: bitCollection) System.out.println(s);
    }
        
    public static void main(String[] args) {
        KMeans km = new KMeans("0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000", 2);
        Cluster c = new Cluster(2);
        c.printLocation();
        System.out.println(c.getDistance(3));
    }
}
