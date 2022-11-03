import csv
import os, glob
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib import cm
# from colorspacious import cspace_converter
from collections import OrderedDict
from tkinter.filedialog import askopenfilename
from colour import Color
 
import statistics

cmaps=OrderedDict()
#ask the user to sleect the folder containing the experiments
evodevo = askopenfilename()
shc = askopenfilename()
randomwalk = askopenfilename()
balanced = askopenfilename()
alternate = askopenfilename()


kValues = []

kArrayS = []
kArrayF = []


kpurewalk = []
kSHC = []
kAlternate = []
kBalance = []


arrayS = []
arrayF = []
currS = []
currF = []
arrayPureWalk = []
arraySHC = []
arrayAlternate = []
arrayBalance = []
currPureWalk = []
currSHC = []
currAlternate = []
currBalance = []



topGen = '1000'

toSkip = 0 #skip lines
#parse the CSV file

specialStrat = "none"
with open(evodevo) as csvfile:
    datareader = csv.reader(csvfile, delimiter=',')
    currentK = ""
    for row in datareader:
        if(toSkip > 0):
            toSkip = toSkip - 1
            continue
        elif(row[0] == "SIMULATION"):
            # print(currentK + " " + row[5] + "==" + str(currentK==row[5]))
            specialStrat = 'none'
            if(currentK == ""):
                currentK = int(row[5][8:])
            if(currentK != int(row[5][8:])):
                kValues.append(currentK)
                currentK = int(row[5][8:])
                # print(currentK)
                kArrayS.append(arrayS)
                kArrayF.append(arrayF)
                # kpurewalk.append(arrayPureWalk)
                # kSHC.append(arraySHC)
                # kAlternate.append(arrayAlternate)
                # kBalance.append(arrayBalance)
                arrayS = []
                arrayF = []
                # arrayPureWalk = []
                # arraySHC = []
                # arrayAlternate = []
                # arrayBalance = []
        elif(len(row)==0):
            print("finished parsing data")
            # print(row)
        elif(row[0] == 'GENERATION'):
            if(row[1] != topGen):
                toSkip = 2
                continue
        elif(row[0] == 'STRATEGY_ROW'):
            j = 0
            for i in row:
                if(i != 'STRATEGY_ROW'):
                    currS.append(i)
                    j = j + 1
            arrayS.append(currS)
            currS = []
        elif(row[0] == 'FITNESS_ROW'):
            if(specialStrat == 'none'):
                j=0
                for i in row:
                    if(i != 'FITNESS_ROW'):
                        currF.append(float(i))
                        j = j + 1
                arrayF.append(currF)
                currF = []

kArrayS.append(arrayS)
kArrayF.append(arrayF)
kValues.append(currentK)

arrayS = []
arrayF = []
currS = []
currF = []
arrayPureWalk = []
arraySHC = []
arrayAlternate = []
arrayBalance = []
currPureWalk = []
currSHC = []
currAlternate = []
currBalance = []

with open(evodevo) as csvfile:
    datareader = csv.reader(csvfile, delimiter=',')
    currentK = ""
    for row in datareader:
        if(toSkip > 0):
            toSkip = toSkip - 1
            continue
        elif(row[0] == "SIMULATION"):
            # print(currentK + " " + row[5] + "==" + str(currentK==row[5]))
            specialStrat = 'none'
            if(currentK == ""):
                currentK = int(row[5][8:])
            if(currentK != int(row[5][8:])):
                currentK = int(row[5][8:])
                # print(currentK)
                kSHC.append(arrayF)
                # kpurewalk.append(arrayPureWalk)
                # kSHC.append(arraySHC)
                # kAlternate.append(arrayAlternate)
                # kBalance.append(arrayBalance)
                arrayS = []
                arrayF = []
                # arrayPureWalk = []
                # arraySHC = []
                # arrayAlternate = []
                # arrayBalance = []
        elif(len(row)==0):
            print("finished parsing data")
            # print(row)
        elif(row[0] == 'GENERATION'):
            if(row[1] != topGen):
                toSkip = 2
                continue
        elif(row[0] == 'STRATEGY_ROW'):
            j = 0
            for i in row:
                if(i != 'STRATEGY_ROW'):
                    currS.append(i)
                    j = j + 1
            arrayS.append(currS)
            currS = []
        elif(row[0] == 'FITNESS_ROW'):
            if(specialStrat == 'none'):
                j=0
                for i in row:
                    if(i != 'FITNESS_ROW'):
                        currF.append(float(i))
                        j = j + 1
                arrayF.append(currF)
                currF = []

kSHC.append(arrayF)

arrayS = []
arrayF = []
currS = []
currF = []
arrayPureWalk = []
arraySHC = []
arrayAlternate = []
arrayBalance = []
currPureWalk = []
currSHC = []
currAlternate = []
currBalance = []

with open(randomwalk) as csvfile:
    datareader = csv.reader(csvfile, delimiter=',')
    currentK = ""
    for row in datareader:
        if(toSkip > 0):
            toSkip = toSkip - 1
            continue
        elif(row[0] == "SIMULATION"):
            # print(currentK + " " + row[5] + "==" + str(currentK==row[5]))
            specialStrat = 'none'
            if(currentK == ""):
                currentK = int(row[5][8:])
            if(currentK != int(row[5][8:])):
                currentK = int(row[5][8:])
                # print(currentK)
                kpurewalk.append(arrayF)
                # kpurewalk.append(arrayPureWalk)
                # kSHC.append(arraySHC)
                # kAlternate.append(arrayAlternate)
                # kBalance.append(arrayBalance)
                arrayS = []
                arrayF = []
                # arrayPureWalk = []
                # arraySHC = []
                # arrayAlternate = []
                # arrayBalance = []
        elif(len(row)==0):
            print("finished parsing data")
            # print(row)
        elif(row[0] == 'GENERATION'):
            if(row[1] != topGen):
                toSkip = 2
                continue
        elif(row[0] == 'STRATEGY_ROW'):
            j = 0
            for i in row:
                if(i != 'STRATEGY_ROW'):
                    currS.append(i)
                    j = j + 1
            arrayS.append(currS)
            currS = []
        elif(row[0] == 'FITNESS_ROW'):
            if(specialStrat == 'none'):
                j=0
                for i in row:
                    if(i != 'FITNESS_ROW'):
                        currF.append(float(i))
                        j = j + 1
                arrayF.append(currF)
                currF = []
    kpurewalk.append(arrayF)

arrayS = []
arrayF = []
currS = []
currF = []
arrayPureWalk = []
arraySHC = []
arrayAlternate = []
arrayBalance = []
currPureWalk = []
currSHC = []
currAlternate = []
currBalance = []

with open(balanced) as csvfile:
    datareader = csv.reader(csvfile, delimiter=',')
    currentK = ""
    for row in datareader:
        if(toSkip > 0):
            toSkip = toSkip - 1
            continue
        elif(row[0] == "SIMULATION"):
            # print(currentK + " " + row[5] + "==" + str(currentK==row[5]))
            specialStrat = 'none'
            if(currentK == ""):
                currentK = int(row[5][8:])
            if(currentK != int(row[5][8:])):
                currentK = int(row[5][8:])
                # print(currentK)
                kBalance.append(arrayF)
                # kpurewalk.append(arrayPureWalk)
                # kSHC.append(arraySHC)
                # kAlternate.append(arrayAlternate)
                # kBalance.append(arrayBalance)
                arrayS = []
                arrayF = []
                # arrayPureWalk = []
                # arraySHC = []
                # arrayAlternate = []
                # arrayBalance = []
        elif(len(row)==0):
            print("finished parsing data")
            # print(row)
        elif(row[0] == 'GENERATION'):
            if(row[1] != topGen):
                toSkip = 2
                continue
        elif(row[0] == 'STRATEGY_ROW'):
            j = 0
            for i in row:
                if(i != 'STRATEGY_ROW'):
                    currS.append(i)
                    j = j + 1
            arrayS.append(currS)
            currS = []
        elif(row[0] == 'FITNESS_ROW'):
            if(specialStrat == 'none'):
                j=0
                for i in row:
                    if(i != 'FITNESS_ROW'):
                        currF.append(float(i))
                        j = j + 1
                arrayF.append(currF)
                currF = []

kBalance.append(arrayF)

arrayS = []
arrayF = []
currS = []
currF = []
arrayPureWalk = []
arraySHC = []
arrayAlternate = []
arrayBalance = []
currPureWalk = []
currSHC = []
currAlternate = []
currBalance = []

with open(alternate) as csvfile:
    datareader = csv.reader(csvfile, delimiter=',')
    currentK = ""
    for row in datareader:
        if(toSkip > 0):
            toSkip = toSkip - 1
            continue
        elif(row[0] == "SIMULATION"):
            # print(currentK + " " + row[5] + "==" + str(currentK==row[5]))
            specialStrat = 'none'
            if(currentK == ""):
                currentK = int(row[5][8:])
            if(currentK != int(row[5][8:])):
                currentK = int(row[5][8:])
                # print(currentK)
                kAlternate.append(arrayF)
                # kpurewalk.append(arrayPureWalk)
                # kSHC.append(arraySHC)
                # kAlternate.append(arrayAlternate)
                # kBalance.append(arrayBalance)
                arrayS = []
                arrayF = []
                # arrayPureWalk = []
                # arraySHC = []
                # arrayAlternate = []
                # arrayBalance = []
        elif(len(row)==0):
            print("finished parsing data")
            # print(row)
        elif(row[0] == 'GENERATION'):
            if(row[1] != topGen):
                toSkip = 2
                continue
        elif(row[0] == 'STRATEGY_ROW'):
            j = 0
            for i in row:
                if(i != 'STRATEGY_ROW'):
                    currS.append(i)
                    j = j + 1
            arrayS.append(currS)
            currS = []
        elif(row[0] == 'FITNESS_ROW'):
            if(specialStrat == 'none'):
                j=0
                for i in row:
                    if(i != 'FITNESS_ROW'):
                        currF.append(float(i))
                        j = j + 1
                arrayF.append(currF)
                currF = []
        
kAlternate.append(arrayF)

arrayS = []
arrayF = []
currS = []
currF = []
arrayPureWalk = []
arraySHC = []
arrayAlternate = []
arrayBalance = []
currPureWalk = []
currSHC = []
currAlternate = []
currBalance = []

print(len(kArrayF[0][0]))

simsPerK = len(kArrayS[0])
stepsPerSim = len(kArrayS[0][0])

kLooksBetweenWalk = []
arrLooksBetweenWalk = []
RWSatWalks = []

kFitness = []
arrFitness = []
fitness = []

numtot = 0
nk = 0

#arrange out-of-order Ks
SSorted= []
FSorted= []
PureWalkSorted = []
SHCSorted = []
AlternateSorted = []
BalancedSorted = []
for i in range(len(kValues)):
    SSorted.append([])
    FSorted.append([])
    PureWalkSorted.append([])
    SHCSorted.append([])
    AlternateSorted.append([])
    BalancedSorted.append([])


for i in range(len(kValues)):
    SSorted[kValues[i]] = kArrayS[i]
    FSorted[kValues[i]] = kArrayF[i]
    PureWalkSorted[kValues[i]] = kpurewalk[i]
    SHCSorted[kValues[i]] = kSHC[i]
    AlternateSorted[kValues[i]] = kAlternate[i]
    BalancedSorted[kValues[i]] = kBalance[i]

print(kValues)

kArrayS = SSorted
kArrayF = FSorted
kpurewalk = PureWalkSorted
kSHC = SHCSorted
kAlternate = AlternateSorted
kBalance = BalancedSorted

fig, ax = plt.subplots()
# ax2 = ax.twinx()
# plt.title("Final Fitnesses of Strategies across K-Values")

#make an array of k colors

colors = [Color("Black"), Color("Orange"), Color("Red"), Color("Green"), Color("Purple")]
namesToPlot = ['Evolved', 'Random Walker', 'Steepest Hill Climber', 'Alternating SHC/RW', 'Balance']
toPlot = []
toPlot.append(kArrayF)
toPlot.append(kpurewalk)
toPlot.append(kSHC)
toPlot.append(kAlternate)
toPlot.append(kBalance)

index = 0
for plot in toPlot:
    plot_error = "standard_error"
    #plot_error = "standard_deviation"
    mean_values = []
    lower_errors = []
    upper_errors = []

    print(namesToPlot[index] + " plotting...")
    print("Length " + str(len(plot)))

    xaxis = np.arange(0, len(plot), 1)
    # print(maxNumOfWalks)
    finalFits = []
    for i in range(0, len(plot)):
        finalFits.append([])

    for i in range(0, len(plot)):
        for j in range(0, len(plot[i])):
            finalFits[i].append(plot[i][j][len(plot[i][j])-1])

    n = len(finalFits * 100) #1000 is from numTestsForComparison from java code

    for i in range(len(finalFits)):
        mean = statistics.mean(finalFits[i])
        mean_values.append(mean)

        std = statistics.stdev(finalFits[i])
        if plot_error == "standard_error":
            error = std/np.sqrt(n)
            lower_errors.append( mean - error )
            upper_errors.append( mean + error )
        elif plot_error == "standard_deviation":
            lower_errors.append( mean - std )
            upper_errors.append( mean + std )
        else:
            print("plot_error must be standard_error or standard_deviation")
            exit(1)
    # for i in range(l)
                
    # print(looksBetweenWalksPerSim)

    leg1 = ax.plot(xaxis, mean_values, color = colors[index].hex, label = namesToPlot[index])
    legerror1 = ax.fill_between(xaxis, lower_errors, upper_errors, alpha=0.25, color = colors[index].hex)

    index = index + 1

ax.set_xlabel('K-Value')
ax.set_ylabel('Average Final Fitness')
ax.legend(loc = 'upper right')

# ax.set_ylim([0, max(mean_values) + 0.1])
# ax2.set_ylim([0, 1])
# ax2.legend(loc = 'upper right')
fig.set_size_inches(5,5 )  


SMALL_SIZE = 10
MEDIUM_SIZE = 12
BIGGER_SIZE = 13

plt.rc('font', size=BIGGER_SIZE)          # controls default text sizes
plt.rc('axes', titlesize=BIGGER_SIZE)     # fontsize of the axes title
plt.rc('axes', labelsize=MEDIUM_SIZE)    # fontsize of the x and y labels
plt.rc('xtick', labelsize=SMALL_SIZE)    # fontsize of the tick labels
plt.rc('ytick', labelsize=SMALL_SIZE)    # fontsize of the tick labels
plt.rc('legend', fontsize=MEDIUM_SIZE)    # legend fontsize
plt.rc('figure', titlesize=BIGGER_SIZE)  # fontsize of the figure title

# plt.savefig("plotOutput/"+"fitnesses.png", dpi=300, bbox_inches='tight')
plt.show()