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
fuzzyBits = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000"

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
            return not (self.currentPoints == self.previousPoints)
    
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
        
    def printCentroid(self):
        print(self.centroid)
    
    def printLocation(self):
        print(self.location)
        
    def printPoints(self):
        result = ""
        for point in self.currentPoints:
            result += str(point) + " "
        print(result[:-1])
    
    def printPreviousPoints(self):
        result = ""
        for point in self.previousPoints:
            result += str(point) + " "
        print(result[:-1])
        

class KMeans(object):
    clusters = []
    bitCollection = []
    timeUnits = [0,0,0]
    dist = {}
    keys = []
    
    def __init__(self, stream, numClusters):
        stream = stream.strip("0")
        
        if len(stream) == 0:
            self.bitCollection.append("")
        else:
            ones = re.split("0+", stream)
            zeros = re.split("1+", stream)
            if len(zeros) == 0:
                self.bitCollection.append(ones[0])
            else:
                for i in range(len(ones) - 1):
                    self.bitCollection.append(ones[i])
                    self.bitCollection.append(zeros[i + 1])
                self.bitCollection.append(ones[-1])
        
        for bit in self.bitCollection:
            l = len(bit)
            if l in self.dist:
                self.dist[l] += 1
            else:
                self.dist[l] = 1
        self.keys = sorted(self.dist.keys())
        self.initializeClusters()
        
    def initializeClusters(self):
        self.clusters.append(Cluster(float(self.keys[0])))
        self.clusters.append(Cluster((float(self.keys[0]) + float(self.keys[-1])) / 2 + 1))
        self.clusters.append(Cluster(float(self.keys[-1])))
        
    def assignToClosestCluster(self):
        self.clear()
        for key in self.keys:
            bestCluster = Cluster(-5000)
            closest = 10000000.0
            for c in self.clusters:
                d = c.getDistance(key)
                if d < closest:
                    closest = d
                    bestCluster = c
            for i in range(self.dist[key]):
                bestCluster.addPoint(key)
                
    def calculateTimeUnits(self):
        sortedClusters = sorted(self.clusters, key = lambda x: x.getLocation(), reverse = False)
        for i in range(3):
            self.timeUnits[i] = sortedClusters[i].getLocation()
            
    def clear(self):
        for c in self.clusters:
            c.clearPoints()
        
        
    
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
