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
import java.util.Scanner;

public class MorseCodeDecoder {
    private static int tu;
    private static float thresh13;
    private static float thresh37;
    
    /**
     * Given a string of bits beginning and ending with '1's,
     * returns the standard timing unit used in the bit transmission
     * 
     * @param bits 
     */
    private static int getTimeUnit(String bits) {
        String[] o = bits.split("0+");
        if (o.length == 1) return bits.length(); //bits is all 1's, so E
        int os = o[0].length();
        for (int i = 1; i < o.length - 1; i++) {
            int t = o[i].length();
            if (t != os) {
                os = Math.min(os,t);
                break;
            }
        }
        String[] z = bits.split("1+");
        int zs = z[1].length();
        for (int i = 2; i < z.length - 1; i++) {
            int t = z[i].length();
            if (t != zs) {
                zs = Math.min(zs,t);
                break;
            }
        }
        return Math.min(os,zs);
    }
    
    /**
     * Given a string of ones and its following strings of zeros,
     * returns the Morse symbol (e.g. dot, dash, new-letter, new-word)
     * which these ones and zeros signify).
     * 
     * @param one
     * @param zero
     * @return 
     */
    private static String nextTele(String one, String zero) {
        String tele = "";
        if (one.length() == tu) tele += ".";
        else if (one.length() == 3 * tu) tele += "-";
        if (zero.length() == 3 * tu) tele += " ";
        else if (zero.length() == 7 * tu) tele += "   ";
        return tele;
    }
    
    /**
     * Given a string of ones, returns the Morse symbol
     * (i.e. dot or dash) which these ones signify.
     * 
     * @param one
     * @return 
     */
    private static String nextTele(String one) {
        String tele = "";
        if (one.length() == tu) tele += ".";
        else if (one.length() == 3 * tu) tele += "-";
        return tele;
    }
    
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
     * returns the Morse Code translation of this message.
     * 
     * @param bits
     * @return 
     */
    public static String decodeBits(String bits) {
        String morse = "";
        bits = bits.replaceAll("^[0]+", "");
        bits = bits.replaceAll("[0]+$", "");
        tu = getTimeUnit(bits);
        String[] ones = bits.split("0+");
        String[] zeros = bits.split("1+");
        for (int i = 0; i < zeros.length - 1; i++) {
            morse += nextTele(ones[i], zeros[i + 1]);
        }
        morse += nextTele(ones[ones.length - 1]);
        return morse;
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fuzzyBits = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
//        MorseCodeDecoder.getTimeUnit(bits);
        String morse = MorseCodeDecoder.decodeBitsAdvanced(fuzzyBits);
        System.out.println(morse);
        String msg = MorseCodeDecoder.decodeMorse(morse);
        System.out.println(msg);
    }
    
}
