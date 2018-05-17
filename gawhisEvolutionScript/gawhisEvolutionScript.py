from subprocess import Popen, PIPE, STDOUT, DEVNULL
import random

def randomInputValues(count):
    values = []
    for i in range(count):
        values.append(random.uniform(-1, 1))
    return values

def getResultFromOutput(clientOutput):
    output = ""
    for line in clientOutput.stdout:
        output = line
    return output

def getOuptuptForPlayerWithParam(param1, param2, param3, param4):
    return Popen(['java', '-jar', '../out/artifacts/gawihsClient_jar/gawihsClient.jar', str(param1), str(param2), str(param3), str(param4)], stdout=PIPE, stderr=STDOUT, universal_newlines=True)

def printResults(playerValues, playerResults):
    print("Parameters:", playerValues)
    print("Results:", playerResults)

playerOneValues = randomInputValues(4);
playerTwoValues = randomInputValues(4);
playerThreeValues = randomInputValues(4);

playerOneResults, playerTwoResults, playerThreeResults = [],[],[]

for x in range(1):
    print("round:", x+1)
    #start server
    Popen(['java', '-Djava.library.path=../../gawihs/lib/native', '-jar', '../../gawihs/gawihs.jar', '0', '0', '50000', 'noanim', 'autoclose'], stdout=DEVNULL)

    #start clients
    playerOneOutput = getOuptuptForPlayerWithParam(playerOneValues[0], playerOneValues[1], playerOneValues[2], playerOneValues[3])
    playerTwoOutput = getOuptuptForPlayerWithParam(playerTwoValues[0], playerOneValues[1], playerOneValues[2], playerOneValues[3])
    playerThreeOutput = getOuptuptForPlayerWithParam(playerThreeValues[0], playerOneValues[1], playerOneValues[2], playerOneValues[3])

    #get Client Result
    playerOneResults.append(getResultFromOutput(playerOneOutput))
    playerTwoResults.append(getResultFromOutput(playerTwoOutput))
    playerThreeResults.append(getResultFromOutput(playerThreeOutput))

printResults(playerOneValues, playerOneResults)
printResults(playerTwoValues, playerTwoResults)
printResults(playerThreeValues, playerThreeResults)