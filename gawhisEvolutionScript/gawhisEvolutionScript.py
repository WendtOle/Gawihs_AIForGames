from subprocess import Popen, PIPE, STDOUT, DEVNULL
import numpy
import random

def randomInputValues(count):
    values = []
    for i in range(count):
        values.append(random.uniform(-1, 1))
    return normalize(values)

def mutate(parent):
    values = []
    for i in range(len(parent)):
        values.append(parent[i] + random.uniform(-0.1, 0.1))
    return normalize(values)

def normalize(values):
    total = numpy.sum(numpy.absolute(values))
    return [x / total for x in values]

def getResultFromOutput(clientOutput):
    output = ""
    for line in clientOutput.stdout:
        output = line
    return output

def getOuptuptForPlayerWithParam(params):
    return Popen(['java', '-jar', '../out/artifacts/gawihsClient_jar/gawihsClient.jar', str(params[0]), str(params[0]), str(params[0]), str(params[0])], stdout=PIPE, stderr=STDOUT, universal_newlines=True)

def printResults(playerValues, playerResults):
    playerResultsAsInt = [int(numeric_string) for numeric_string in playerResults]
    print("Parameters:", playerValues)
    print("Results:", playerResultsAsInt)
    print("Mean result:", numpy.mean(playerResultsAsInt))

playerValues = [randomInputValues(4), randomInputValues(4),randomInputValues(4)]

playerResults = [[],[],[]]


for gen in range(10):
    for x in range(10):
        print("round:", x+1)

        #start server
        Popen(['java', '-Djava.library.path=../../gawihs/lib/native', '-jar', '../../gawihs/gawihs.jar', '0', '0', '50000', 'noanim', 'autoclose'], stdout=DEVNULL)

        playerOutputs = []
        for i in range(3):
            playerOutputs.append(getOuptuptForPlayerWithParam(playerValues[i]))

        # get Client Result
        playerResults[0].append(getResultFromOutput(playerOutputs[0]))
        playerResults[1].append(getResultFromOutput(playerOutputs[1]))
        playerResults[2].append(getResultFromOutput(playerOutputs[2]))

    allMeanResults =[]

    for i in range(3):
        playerResultsAsInt = [int(numeric_string) for numeric_string in playerResults[i]]
        allMeanResults.append(numpy.mean(playerResultsAsInt))
        #print("Values:", playerValues[i])
        #print("Results:", playerResults[i])
        print("Mean Result:", allMeanResults[i])

    max_value = max(allMeanResults)
    max_index = allMeanResults.index(max_value)

    print("max value:", max_value)
    #print("max index:", max_index)

    parent = playerValues[max_index]
    print("new parent:", parent)
    for i in range(3):
        playerValues[i] = mutate(parent)


