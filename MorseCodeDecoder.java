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
    private static void dotLength(String bits) {
        String[] b = bits.split("0+");
        int l = b[0].length();
        for (int i = 1; i < b.length; i++) {
            int t = b[i].length();
            if (t != l) {
                l = Math.min(l,t);
                break;
            }
        }
        tu = l;
    }
    
    private String nextTele(String one, String zero) {
        String tele = "";
        if (one.length() == tu) tele += ".";
        else if (one.length() == 3 * tu) tele += "-";
        if (zero.length() == 3 * tu) tele += " ";
        else if (zero.length() == 7 * tu) tele += "   ";
        return tele;
    }
    
    public static String decodeBits(String bits) {
        String morse = "";
        bits = bits.replaceAll("^[0]+", "");
        bits = bits.replaceAll("[0]+$", "");
        dotLength(bits);
        String[] ones = bits.split("0+");
        String[] zeros = bits.split("1+");
        for (int i = 0; i < zeros.length - 1; i++) {
            System.out.print(ones[i]);
            System.out.println(zeros[i + 1]);
            if (ones[i].length() == tu) morse += ".";
            else if (ones[i].length() == 3 * tu) morse += "-";
            if (zeros[i + 1].length() == 3 * tu) morse += " ";
            else if (zeros[i + 1].length() == 7 * tu) morse += "   ";
        }
        System.out.println(ones[ones.length - 1]);
        return morse;
    }
    
    public static String decodeMorse(String morseCode) {
        return MorseCode.get(morseCode);
    }
    
    public static String decode(String morseCode) {
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
        String morse = MorseCodeDecoder.decodeBits(bits);
        System.out.println(morse);
//        System.out.println(
//                MorseCodeDecoder.decode(msg));
    }
    
}
