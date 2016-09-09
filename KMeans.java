
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
    private final String stream;
    private final int clusters;
    private final Random rand;
    private final String[] bitCollection;
    
    public KMeans(String stream, int clusters) {
        this.stream = stream;
        this.clusters = clusters;
        this.rand = new Random();
        
        stream = stream.replaceAll("^[0]+", "");
        stream = stream.replaceAll("[0]+$", "");
        String[] ones = stream.split("0+");
        String[] zeros = stream.split("1+");
        bitCollection = new String[ones.length + zeros.length - 1];
        for (int i = 0; i < ones.length - 1; i++) {
            bitCollection[2*i] = ones[i];
            bitCollection[2*i+1] = zeros[i+1];
        }
        bitCollection[bitCollection.length - 1] = ones[ones.length - 1];
    }
        
    public static void main(String[] args) {
        KMeans km = new KMeans("00011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000000", 2);
    }
}
