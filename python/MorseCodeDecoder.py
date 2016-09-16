import re
import matplotlib.pyplot as plt
import seaborn as sns

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
fuzzyTest = "00000000000000011111111000000011111111111100000000000111111111000001111111110100000000111111111111011000011111111011111111111000000000000000000011111111110000110001111111111111000111000000000001111111111110000111111111100001100111111111110000000000111111111111011100001110000000000000000001111111111010111111110110000000000000001111111111100001111111111110000100001111111111111100000000000111111111000000011000000111000000000000000000000000000011110001111100000111100000000111111111100111111111100111111111111100000000011110011111011111110000000000000000000000111111111110000000011111000000011111000000001111111111110000000001111100011111111000000000111111111110000011000000000111110000000111000000000011111111111111000111001111111111001111110000000000000000000001111000111111111100001111111111111100100000000001111111100111111110111111110000000011101111111000111000000001001111111000000001111111111000000000111100001111111000000000000011111111100111111110111111111100000000000111111110000001100000000000000000000111111101010000010000001111111100000000011111000111111111000000111111111110011111111001111111110000000011000111111110000111011111111111100001111100001111111100000000000011110011101110001000111111110000000001111000011111110010110001111111111000000000000000000111111111110000000100000000000000000011110111110000001000011101110000000000011111111100000011111111111100111111111111000111111111000001111111100000000000001110111111111111000000110011111111111101110001111111111100000000111100000111100000111111111100000111111111111000000011111111000000000001000000111100000001000001111100111111111110000000000000000000010001111111100000011111111100000000000000100001111111111110111001111111111100000111111100001111111111000000000000000000000000011100000111111111111011110000000010000000011111111100011111111111100001110000111111111111100000000000000111110000011111001111111100000000000011100011100000000000011111000001111111111101000000001110000000000000000000000000000111110010000000000111111111000011111111110000000000111111111111101111111111100000000010000000000000011111111100100001100000000000000111100111100000000001100000001111111111110000000011111111111000000000111100000000000000000000111101111111111111000000000001111000011111000011110000000001100111111100111000000000100111000000000000111110000010000011111000000000000001111111111100000000110111111111100000000000000111111111111100000111000000000111111110001111000000111111110111111000000001111000000000010000111111111000011110001111111110111110000111111111111000000000000000000000000111111111110000000111011111111100011111110000000001111111110000011111111100111111110000000001111111111100111111111110000000000110000000000000000001000011111111110000000001111111110000000000000000000000011111111111111000000111111111000001111111110000000000111111110000010000000011111111000011111001111111100000001110000000011110000000001011111111000011111011111111110011011111111111000000000000000000100011111111111101111111100000000000000001100000000000000000011110010111110000000011111111100000000001111100011111111111101100000000111110000011110000111111111111000000001111111111100001110111111111110111000000000011111111101111100011111111110000000000000000000000000010000111111111100000000001111111110111110000000000000000000000110000011110000000000001111111111100110001111111100000011100000000000111110000000011111111110000011111000001111000110000000011100000000000000111100001111111111100000111000000001111111111000000111111111100110000000001111000001111111100011100001111111110000010011111111110000000000000000000111100000011111000001111000000000111111001110000000011111111000100000000000011111111000011001111111100000000000110111000000000000111111111111000100000000111111111110000001111111111011100000000000000000000000000"

class Cluster(object):    
    def __init__(self, loc):
        self.currentPoints = []
        self.centroid = None
        self.previousPoints = []
        self.location = loc
    
    ##  Methods for claiming currentPoints and calculating centroid.
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
        '''
        After new points have been assigned to this cluster, this method
        calculates the new centroid of the cluster and moves the cluster
        to that location.
        '''
        if len(self.currentPoints) > 0:
            s = 0.0
            for p in self.currentPoints:
                s += p
            self.centroid = s / len(self.currentPoints)
            self.location = self.centroid
    
    
    ##  Getter methods.
    def getLocation(self):
        return self.location
        
    def getDistance(self, point):
        return abs(self.location - point)
    
    ##  Printer methods.
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
        
        ##  Populate this.bitCollection.
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
        
        
        ##  Populate this.dist.
        for bit in self.bitCollection:
            l = len(bit)
            if l in self.dist:
                self.dist[l] += 1
            else:
                self.dist[l] = 1
        self.keys = sorted(self.dist.keys())
        
        ##  Handle short inputs (i.e. displays fewer than three timing units)
        if len(self.keys) == 1 or len(self.keys) == 2:
            self.timeUnits[0] = self.keys[0]
            self.timeUnits[1] = self.keys[0] * 3
            self.timeUnits[2] = self.keys[0] * 7
            self.converged = True
        
        ##  Handle long inputs via KMeans
        else:
            self.initializeClusters()
        
    def initializeClusters(self):
        '''
        Populates this.clusters with this.numClusters Cluster objects,
        whose initial locations are from this.keys (the minimum, the
        maximum, and the middle between the two).
        '''
        self.clusters.append(Cluster(float(self.keys[0])))
        self.clusters.append(Cluster((float(self.keys[0]) + float(self.keys[-1])) / 2))
        self.clusters.append(Cluster(float(self.keys[-1])))
        
    def assignToClosestCluster(self):
        '''
        Assigns cluster-labels to each length-point from the fuzzy input,
        which is subsequently used by the clusters to re-calculate their
        centroids and move accordingly.
        '''
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
        '''
        Assigns the closest Cluster to each point, calculates the centroid
        for those Clusters based off of those points, moves the Clusters
        to their respective centroids, and repeats until assignment on the next
        iteration is the same.
        '''
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
    
    ##  Getter methods.
    def getTimeUnit(self, index):
        return self.timeUnits[index]
        
    ##  Printer methods.
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
    
    ##  Plotter methods.
    def plotDistribution(self):
        xmax = max(self.keys)
        ymax = max(self.dist.values())
        plt.figure()
        plt.bar(self.dist.keys(), self.dist.values())
        plt.title("Bit Length Frequencies")
        plt.xlabel("Number of Characters")
        plt.ylabel("Frequency")
        plt.axis([0, xmax, 0, ymax])
        plt.axvline((self.getTimeUnit(0) + self.getTimeUnit(1)) / 2, color='b', linestyle='dashed', linewidth=2)
        plt.axvline((self.getTimeUnit(1) + self.getTimeUnit(2)) / 2, color='b', linestyle='dashed', linewidth=2)
        plt.show()
        
        
# Required as per problem API
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
    ones = re.split("0+", fuzzyBits)
    zeros = re.split("1+", fuzzyBits)
    for i in range(len(zeros) - 1):
        morse += nextTelePairFuzzy(ones[i], zeros[i + 1], thresh13, thresh37)
    return morse

# Required as per problem API
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
    
# Required as per problem API
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
            try:
                result += MORSE_CODE[morse]
            except KeyError:
                result += "(KEYERR: "+morse+")"
    return result                

# Helper function for 3 of 3 in the series
def nextTelePairFuzzy(one, zero, thresh13, thresh37):
    tele = nextTeleSingleFuzzy(one, thresh13)
    if len(zero) >= thresh13 and len(zero) < thresh37:
        tele += " "
    elif len(zero) >= thresh37:
        tele += "   "
    return tele

# Helper function for 3 of 3 in the series
def nextTeleSingleFuzzy(one, thresh13):
    tele = ""
    if len(one) <= thresh13:
        tele += "."
    else:
        tele += "-"
    return tele

# Helper function for 2 of 3 in the series
def nextTelePair(one, zero, tu):
    tele = nextTeleSingle(one, tu)
    if len(zero) == 3 * tu:
        tele += " "
    elif len(zero) == 7 * tu:
        tele += "   "
    return tele
    
# Helper function for 2 of 3 in the series
def nextTeleSingle(one, tu):
    tele = ""
    if len(one) == tu:
        tele += "."
    elif len(one) == 3 * tu:
        tele += "-"
    return tele    

# Helper function for 2 of 3 in the series
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

# Function to rapidly iterate through possible thresholds;
# not a valid way of solving 3 of 3.
def bruteThreshholds(fuzzyBits):
    fuzzyBits = fuzzyBits.strip("0")
    f = open('BruteForceDump', 'w')
    ones = re.split("0+", fuzzyBits)
    zeros = re.split("1+", fuzzyBits)
    lowerStart = 7
    lowerStop = 8       # exclusive
    lowerStep = 0.25
    upperStart = 15
    upperStop = 16      # exclusive
    upperStep = 0.25
    for lower in range(0, int((lowerStop - lowerStart) / lowerStep), 1):
        for upper in range(0, int((upperStop - upperStart) / upperStep), 1):
            morse = ""
            for i in range(len(zeros) - 1):
                morse += nextTelePairFuzzy(ones[i], zeros[i + 1],
                    lowerStart + lower * lowerStep, upperStart + upper * upperStep)
            f.write(str(lowerStart + lower * lowerStep) + " " + str(upperStart + upper * upperStep) + '\n')
            f.write(decodeMorse(morse))
            f.write('\n\n')

