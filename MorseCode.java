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
public class MorseCode {
    public static String get(String m) {
        if (m.equals(".-")) return "A";
        if (m.equals("-...")) return "B";
        if (m.equals("-.-.")) return "C";
        if (m.equals("-..")) return "D";
        if (m.equals(".")) return "E";
        if (m.equals("..-.")) return "F";
        if (m.equals("--.")) return "G";
        if (m.equals("....")) return "H";
        if (m.equals("..")) return "I";
        if (m.equals(".---")) return "J";
        if (m.equals("-.-")) return "K";
        if (m.equals(".-..")) return "L";
        if (m.equals("--")) return "M";
        if (m.equals("-.")) return "N";
        if (m.equals("---")) return "O";
        if (m.equals(".--.")) return "P";
        if (m.equals("--.-")) return "Q";
        if (m.equals(".-.")) return "R";
        if (m.equals("...")) return "S";
        if (m.equals("-")) return "T";
        if (m.equals("..-")) return "U";
        if (m.equals("...-")) return "V";
        if (m.equals(".--")) return "W";
        if (m.equals("-..-")) return "X";
        if (m.equals("-.--")) return "Y";
        if (m.equals("--..")) return "Z";
        if (m.equals("-----")) return "0";
        if (m.equals(".----")) return "1";
        if (m.equals("..---")) return "2";
        if (m.equals("...--")) return "3";
        if (m.equals("....-")) return "4";
        if (m.equals(".....")) return "5";
        if (m.equals("-....")) return "6";
        if (m.equals("--...")) return "7";
        if (m.equals("---..")) return "8";
        if (m.equals("----.")) return "9";
        if (m.equals("...---...")) return "SOS";
        else return "We don't know";
    }
}
