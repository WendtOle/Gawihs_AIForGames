from subprocess import Popen, PIPE, STDOUT, DEVNULL
import numpy
import random

def randomInputValues(count):
    values = []
    for i in range(count):
        values.append(random.uniform(-1, 1))
    return normalize(values)

def mutate(parent,gen):
    half_mutation_rate = 10
    final_half_mutation_rate = (half_mutation_rate - (gen))/100
    values = []
    for i in range(len(parent)):
        values.append(parent[i] + random.uniform(-final_half_mutation_rate, final_half_mutation_rate))
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
    return Popen(['java', '-jar', '../out/artifacts/gawihsClient_jar/gawihsClient.jar', "evolutionalMode",  "3", str(params[0]), str(params[0]), str(params[0]), str(params[0])], stdout=PIPE, stderr=STDOUT, universal_newlines=True)

def start_server():
    Popen(['java', '-Djava.library.path=../../gawihs/lib/native', '-jar', '../../gawihs/gawihs.jar', '0', '0', '50000',
           'noanim', 'autoclose'], stdout=DEVNULL)

def game(groups,rounds_per_group):
    winner = []
    for index_01 in range(len(groups)):

        results = [[],[],[]]
        cur_group = groups[index_01]

        for index_02 in range(rounds_per_group):
            #start server
            start_server()

            playerOutputs = []

            #put players into game
            for i in range(3):
                playerOutputs.append(getOuptuptForPlayerWithParam(cur_group[i]))

            results[0].append(getResultFromOutput(playerOutputs[0]))
            results[1].append(getResultFromOutput(playerOutputs[1]))
            results[2].append(getResultFromOutput(playerOutputs[2]))

        final_result = []
        for i in range(3):
            resultAsInt = [int(numeric_string) for numeric_string in results[i]]
            final_result.append(numpy.mean(resultAsInt))

        max_Value = max(final_result)
        winner_index = final_result.index(max_Value)
        winner.append(cur_group[winner_index])
    return winner

def find_groups(player):
    player_ = player
    groups = []
    for i in range(int(len(player)/3)):
        group = []
        for i in range(3):
            cur_index = random.randint(0, len(player_) - 1)
            cur_player_values = player_[cur_index]
            group.append(cur_player_values)
            player_.remove(cur_player_values)
        groups.append(group)
    return groups

best_parents = []

#init children first time complely random
children = []
for i in range(27):
    children.append(randomInputValues(4))

for gen in range(3):
    print("Generation", gen+1)
    groups_first_round = find_groups(children)

    winner_first_round = game(groups_first_round,3)
    print("winner round one:",winner_first_round)

    groups_second_round = find_groups(winner_first_round)

    winner_second_round = game(groups_second_round,3)
    print("winner round second:", winner_second_round)

    #recombinate
    best_parents = winner_second_round
    for par_index in range(3):
        for child_index in range(9):
            children.append(mutate(winner_second_round[par_index],gen))

print("best:",best_parents)

