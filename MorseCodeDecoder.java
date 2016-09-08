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
    
    public static String decodeBits(String bits) {
        bits = bits.replaceAll("^[0]+", "");
        bits = bits.replaceAll("[0]+$", "");
        return bits;
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
        String bits = "000000110011001100110000001100000011111100110011111100111111000000000000001100111111001111110011111100000011001100111111000000111111001100110000001100000";
        String msg = MorseCodeDecoder.decodeBits(bits);
        System.out.println(msg);
//        System.out.println(
//                MorseCodeDecoder.decode(msg));
    }
    
}
