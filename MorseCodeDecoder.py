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
JudeBits = "1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011"

class Cluster(object):
    


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


def decodeBits(bits):
    '''
    input bits, a string of 0s and 1s with fixed timing
    returns string, a morse code message
    '''
    bits = bits.strip("0")
    

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
