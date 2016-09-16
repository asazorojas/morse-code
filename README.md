# morse-code 
Maps Morse Code to English, whether dots and dashes, bits, or fuzzy bits.

This project was completed for the CodeWars [Decode the Morse code, for real](https://www.codewars.com/kata/decode-the-morse-code-for-real/) series.


### Branches
- `master` contains everything in separate class files.  Use this for parts [one](https://www.codewars.com/kata/decode-the-morse-code) and [two](https://www.codewars.com/kata/decode-the-morse-code-advanced) of the Morse Code series.
- `kata3` contains the KMeans, Cluster, and helper methods within a single class file.  Use this for part [three](https://www.codewars.com/kata/decode-the-morse-code-for-real/) of the Morse Code series.

### About
Dots and dashes are handled by a `HashMap` in Java, and by a `dictionary` in Python.

Bits are handled by establishing the minimum time unit, then testing for even multiples of that time unit.

Fuzzy bits are handled by a `KMeans` algorithm.  

- This algorithm establishes the means of the tri-phasic input stream (usually, though not necessarily, a 1 - 3 - 7 distribution).  
- These means are then used to establish boundaries for use in signal processing.  
- Presently, there is a bias in the tests to favor 1 over 3, and 3 over 7 (see below).
<img src="http://i.imgur.com/p38bE4i.png" height="400"/>

### Viewing
I recommend that this code should only be viewed _after_ you've completed your own implementation.  
If you're super stuck, take a break, take a walk, and it will come to you; good luck.
