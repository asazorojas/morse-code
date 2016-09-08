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
    
    /**
     * Given a string of bits beginning and ending with '1's,
     * stores in this.tu the "dot" length of the string, which forms
     * the basis for the standard timing unit in a Morse Code
     * transmission.
     * 
     * @param bits 
     */
    private static void dotLength(String bits) {
        String[] b = bits.split("1+");
        int l = b[1].length();
        for (int i = 2; i < b.length - 1; i++) {
            int t = b[i].length();
            if (t != l) {
                l = Math.min(l,t);
                break;
            }
        }
        tu = l;
    }
    
    /**
     * Given a string of ones and its following strings of zeros,
     * returns the Morse symbol (e.g. dot, dash, new-letter, new-word)
     * these ones and zeros signify).
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
     * (i.e. dot or dash) these ones signify.
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
        dotLength(bits);
        String[] ones = bits.split("0+");
        String[] zeros = bits.split("1+");
        for (int i = 0; i < zeros.length - 1; i++) {
            morse += nextTele(ones[i], zeros[i + 1]);
        }
        morse += nextTele(ones[ones.length - 1]);
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
        String bits = "1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011";
//        MorseCodeDecoder.dotLength(bits);
        String morse = MorseCodeDecoder.decodeBits(bits);
        String msg = MorseCodeDecoder.decodeMorse(morse);
        System.out.println(msg);
    }
    
}
