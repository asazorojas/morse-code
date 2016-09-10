import re

MORSE_CODE = {
    ".-": "A",
    "-...": "B",
    "-.-.": "C", 
    "-..": "D",
    ".": "E",
    "..-.": "F",
    "--.": "G",
    "....": "H",
    "..": "I",
    ".---": "J",
    "-.-": "K",
    ".-..": "L",
    "--": "M",
    "-.": "N",
    "---": "O",
    ".--.": "P",
    "--.-": "Q",
    ".-.": "R",
    "...": "S",
    "-": "T",
    "..-": "U",
    "...-": "V",
    ".--": "W",
    "-..-": "X",
    "-.--": "Y",
    "--..": "Z",
    "-----": "0",
    ".----": "1",
    "..---": "2",
    "...--": "3",
    "....-": "4",
    ".....": "5",
    "-....": "6",
    "--...": "7",
    "---..": "8",
    "----.": "9",
    "...---...": "SOS"
}

heyJude = ".... . -.--   .--- ..- -.. ."
JudeBits = "00011001100110011000000110000001111110011001111110011111100000000000000110011111100111111001111110000001100110011111100000011111100110011000000110000"

class Cluster(object):
    location = None
    centroid = None
    currentPoints = []
    previousPoints = []
    
    def __init__(self, loc):
        self.location = loc
    
    def addPoint(self, point):
        self.currentPoints.append(point)
        
    def didChange(self):
        if len(self.currentPoints) != len(self.previousPoints):
            return True
        else:
            return !self.currentPoints == self.previousPoints
    
    def clearPoints(self):
        self.previousPoints = self.currentPoints[:]
        self.currentPoints.clear()
        
    def update(self):
        sum = 0.0
        for p in self.currentPoints:
            sum += p
        self.centroid = sum / len(self.currentPoints)
        self.location = self.centroid
    
    def getLocation(self):
        return self.location
        
    def getDistance(self, point):
        return abs(self.location - point)


def decodeBitsAdvanced(bits):
    '''
    input bits, a string of 0s and 1s with variable timing
    returns string, a morse code message
    '''
    # ToDo: Accept 0's and 1's, return dots, dashes and spaces
    return bits.replace('111', '-').replace('000', ' ').replace('1', '.').replace('0', '')

def getTimeUnit(bits):
    '''
    input bits, a string of 0s and 1s with fixed timing
    returns int, the single timing unit (i.e. dot or shortest pause)
    '''
    if bits == "":
        return 0
    o = re.split("0+", bits)
    if len(o) == 1:
        return len(bits)
    if o == ['','']:
        return 0
    os = len(o[0])
    for elem in o:
        if len(elem) != os:
            os = min(os,len(elem))
            break
    z = re.split("1+", bits)
    zs = len(z[1])
    for i in range(1, len(z) - 1):
        if zs != len(z[i]):
            zs = min(zs, len(z[i]))
            break
    return min(os, zs)


def nextTelePair(one, zero, tu):
    tele = nextTeleSingle(one, tu)
    if len(zero) == 3 * tu:
        tele += " "
    elif len(zero) == 7 * tu:
        tele += "   "
    return tele
    
        
def nextTeleSingle(one, tu):
    tele = ""
    if len(one) == tu:
        tele += "."
    elif len(one) == 3 * tu:
        tele += "-"
    return tele


def decodeBits(bits):
    '''
    input bits, a string of 0s and 1s with fixed timing
    returns string, a morse code message
    '''
    morse = ""
    bits = bits.strip("0")
    tu = getTimeUnit(bits)
    ones = re.split("0+", bits)
    zeros = re.split("1+",bits)
    for i in range(len(zeros) - 1):
        morse += nextTelePair(ones[i], zeros[i + 1], tu)
    return morse
    

def decodeMorse(morseCode):
    '''
    input morseCode, a string of dots, dashes, and spaces
    returns string, a human-readable message
    '''
    result = ""
    morseCode = morseCode.replace("   ", " SPACE ")
    morses = morseCode.split()
    for morse in morses:
        if morse == "SPACE":
            result += " "
        else:
            result += MORSE_CODE[morse]
    return result


print(decodeBits(JudeBits))
print(decodeMorse(decodeBits(JudeBits)))
