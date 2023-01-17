from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from turtle import st
import numpy as np
import matplotlib.pyplot as plt

filename = askopenfilename()

gen = -1
runs = 0

randWalk = steepest = alternateLookWalk = False

generationFitnesses = [{}]
comparisonFitnesses = []

with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for line in reader:
        if len(line) == 0:
            break
        if line[0] == "GENERATION":
            # print("gen:"+line[1])
            gen=int(line[1])
        elif line[0] == "FITNESS_ROW":
            # print("fit:"+line[-1])
            if randWalk:
                comparisonFitnesses[runs-1]["RandomWalk"] = [float(el) for el in line[1:]]
                randWalk = False
            elif steepest:
                comparisonFitnesses[runs-1]["Steepest"] = [float(el) for el in line[1:]]
                steepest = False
            elif alternateLookWalk:
                comparisonFitnesses[runs-1]["alternateLookWalk"] = [float(el) for el in line[1:]]
                alternateLookWalk = False
            else:
                generationFitnesses[runs][gen] = [float(el) for el in line[1:]]
        elif line[0] == "COMPARISON_STRATEGIES":
            runs += 1
            generationFitnesses.append({})
            comparisonFitnesses.append({})
        elif line[0] == "PureWalk":
            randWalk = True
        elif line[0] == "Steep Hill Climb":
            steepest = True
        elif line[0] == "AlternateLookWalk":
            alternateLookWalk = True
compRe = {}
for map in comparisonFitnesses:
    for key, value in map.items():
        if compRe.get(key) is not None:
            for i in range(len(value)):
                compRe[key][i].append(value[i])
        else:
            compRe[key] = []
            for i in value:
                compRe[key].append([i])

finalGens = [[]]
for runVals in generationFitnesses:
    if(len(runVals)>0):
        maxGen = max(runVals.keys())
        maxGen = runVals[maxGen]
        for step in range(len(maxGen)):
            if step < len(finalGens):
                finalGens[step].append(maxGen[step])
            else:
                finalGens.append([maxGen[step]])

for key, value in compRe.items():
    mean = []
    low = []
    high = []
    for i in value:
        mean.append(np.mean(i))
        low.append(np.mean(i) - (np.std(i)/np.sqrt(len(value))))
        high.append(np.mean(i) + (np.std(i)/np.sqrt(len(value))))
    plt.plot(range(len(mean)), mean,label=key)
    plt.fill_between(range(len(mean)), low, high, alpha=.25)

mean = []
low = []
high = []
for i in finalGens:
    mean.append(np.mean(i))
    low.append(np.mean(i) - (np.std(i)/np.sqrt(len(value))))
    high.append(np.mean(i) + (np.std(i)/np.sqrt(len(value))))
plt.plot(range(len(mean)), mean,label="Final Generation")
plt.fill_between(range(len(mean)), low, high, alpha=.25)

plt.legend()
plt.xlabel("steps") 
plt.ylabel("average fitness")
plt.show()