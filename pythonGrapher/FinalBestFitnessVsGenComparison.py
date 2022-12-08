from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from turtle import st
from matplotlib.colors import rgb2hex
import numpy as np
import matplotlib.pyplot as plt

# filename = askopenfilename()
def FinalBestFitness(filename, label):
    gen = -1
    runs = 0

    randWalk = steepest = alternateLookWalk = False

    generationFitnesses = [{}]

    with open(filename) as csvfile:
        reader = csv.reader(csvfile)
        for line in reader:
            if line[0] == "GENERATION":
                gen=int(line[1])
            elif line[0] == "FITNESS_ROW":
                if randWalk:
                    randWalk = False
                elif steepest:
                    steepest = False
                elif alternateLookWalk:
                    alternateLookWalk = False
                else:
                    generationFitnesses[runs][gen] = [float(el) for el in line[1:]]
            elif line[0] == "COMPARISON_STRATEGIES":
                runs += 1
                generationFitnesses.append({})
            elif line[0] == "PureWalk":
                randWalk = True
            elif line[0] == "Steep Hill Climb":
                steepest = True
            elif line[0] == "AlternateLookWalk":
                alternateLookWalk = True

    avgFinal = {}
    for runVals in generationFitnesses:
        for key,val in runVals.items():
            if(avgFinal.get(key) is None):
                avgFinal[key] = 0
            avgFinal[key] += val[-1]
    for key,val in avgFinal.items():
        avgFinal[key] /= len(generationFitnesses)
    plt.plot(avgFinal.keys(),avgFinal.values(),label=label)

FinalBestFitness(filename=askopenfilename(), label="Old")
FinalBestFitness(filename=askopenfilename(), label="New")


    # plt.plot(avgFinal.keys(),avgFinal.values())
plt.xlabel("generation") 
plt.ylabel("average final fitness")
plt.title("Comparisons of final fitness for dynamic landscapes")
plt.legend()
plt.show()