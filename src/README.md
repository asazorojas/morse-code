# morse-code 
Maps Morse Code to English, whether dots and dashes, bits, or fuzzy bits.

This project was completed for the CodeWars [Decode the Morse code, for real](https://www.codewars.com/kata/decode-the-morse-code-for-real/) series.

Dots and dashes are handled by a conditional chain.

Bits are handled by establishing the minimum time unit, then testing for even multiples of that time unit.

Fuzzy bits are handled by a KMeans algorithm that establishes the means of the tri-phasic input stream (usually, though not necessarily, a 1 - 3 - 7 distribution).  These means are then used to establish boundaries for use in signal processing.

I recommend that this code should only be viewed _after_ you've completed your own implementation.  
If you're super stuck, take a break, take a walk, and it will come to you; good luck.
