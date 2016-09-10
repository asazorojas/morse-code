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
    def __init__(self, loc):
        self.currentPoints = []
        self.centroid = None
        self.previousPoints = []
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
        del self.currentPoints[:]
        
    def update(self):
        if len(self.currentPoints) > 0:
            s = 0.0
            for p in self.currentPoints:
                s += p
            self.centroid = s / len(self.currentPoints)
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
    def __init__(self, stream, numClusters):
        self.clusters = []
        self.bitCollection = []
        self.timeUnits = [0,0,0]
        self.dist = {}
        self.keys = []
        self.converged = False
        
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
        
        if len(self.keys) == 1 or len(self.keys) == 2:
            self.timeUnits[0] = self.keys[0]
            self.timeUnits[1] = self.keys[0] * 3
            self.timeUnits[2] = self.keys[0] * 7
            self.converged = True
        else:
            self.initializeClusters()
        
    def initializeClusters(self):
        self.clusters.append(Cluster(float(self.keys[0])))
        self.clusters.append(Cluster((float(self.keys[0]) + float(self.keys[-1])) / 2))
        self.clusters.append(Cluster(float(self.keys[-1])))
        
    def assignToClosestCluster(self):
        self.clear()
        for key in self.keys:
            bestCluster = Cluster(5000)
            closest = 10000000.0
            for c in self.clusters:
                d = c.getDistance(key)
                if d < closest:
                    closest = d
                    bestCluster = c
            for i in range(self.dist[key]):
                bestCluster.addPoint(key)
                
    def calculateTimeUnits(self):
        for i in range(3):
            self.timeUnits[i] = self.clusters[i].getLocation()
            
    def clear(self):
        for c in self.clusters:
            c.clearPoints()
            
    def converge(self):
        if not self.converged:
            self.assignToClosestCluster()
            while not self.converged:
                self.update()
                self.assignToClosestCluster()
                if not self.didChange():
                    self.converged = True
            self.calculateTimeUnits()
        
    def didChange(self):
        for c in self.clusters:
            if c.didChange():
                return True
        return False
        
    def update(self):
        for c in self.clusters:
            c.update()
            
    def getTimeUnit(self, index):
        return self.timeUnits[index]
        
    def printBitCollection(self):
        for bit in self.bitCollection:
            print(bit)
            
    def printClusterPoints(self):
        for c in self.clusters:
            print("Points for cluster at " + str(c.getLocation()))
            c.printPoints()
            
    def printClusters(self):
        for c in self.clusters:
            print(c.getLocation())
            
    def printDidChange(self):
        print(self.didChange)
        
    def printDistances(self):
        for key in self.keys:
            best = -1.0
            closest = 10000000.0
            for c in self.clusters:
                d = c.getDistance(key)
                print("From cluster at " + str(c.getLocation()) + \
                "to point at " + str(key) + " is: " + str(d))
                if d < closest:
                    closest = d
                    best = c.getLocation()
            print("Closest to: " + str(best))
    
    def printDistribution(self):
        for key in self.keys:
            print("Length: " + str(key) + " occurred " + str(self.dist[key]) + " times")
            
    def printKeys(self):
        for key in self.keys:
            print(key)
    
    def printTimeUnits(self):
        for t in self.timeUnits:
            print(t)
        
        
    
def decodeBitsAdvanced(fuzzyBits):
    '''
    input bits, a string of 0s and 1s with variable timing
    returns string, a morse code message
    '''
    morse = ""
    fuzzyBits = fuzzyBits.strip("0")
    km = KMeans(fuzzyBits, 3)
    km.converge()
    thresh13 = (km.getTimeUnit(0) + km.getTimeUnit(1)) / 2
    thresh37 = (km.getTimeUnit(1) + km.getTimeUnit(2)) / 2
    print(km.getTimeUnit(0), km.getTimeUnit(1), km.getTimeUnit(2))
    print(thresh13, thresh37)
    if thresh13 == thresh37:
        thresh37 = thresh13 / 2.0 * 5.0
    print(thresh13, thresh37)
    ones = re.split("0+", fuzzyBits)
    zeros = re.split("1+", fuzzyBits)
    for i in range(len(zeros) - 1):
        morse += nextTelePairFuzzy(ones[i], zeros[i + 1], thresh13, thresh37)
    return morse


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


def nextTelePairFuzzy(one, zero, thresh13, thresh37):
    tele = nextTeleSingleFuzzy(one, thresh13)
    if len(zero) >= thresh13 and len(zero) < thresh37:
        tele += " "
    elif len(zero) >= thresh37:
        tele += "   "
    return tele
    
def nextTeleSingleFuzzy(one, thresh13):
    tele = ""
    if len(one) <= thresh13:
        tele += "."
    else:
        tele += "-"
    return tele


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


