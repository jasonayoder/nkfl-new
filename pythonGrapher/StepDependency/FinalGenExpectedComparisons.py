from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from turtle import st
import numpy as np
import matplotlib.pyplot as plt




def getStepToExpectedValueLast(filename):
    stepOrder = []
    gen = -1
    runs = 0
    runsOfGenToStepToExpectedValue = [{}]
    with open(filename) as csvfile:
        reader = csv.reader(csvfile)
        for line in reader:
            if len(line)==0:
                runsOfGenToStepToExpectedValue.append({})
            elif line[0] == "GENERATION":
                gen=int(line[1])
                runsOfGenToStepToExpectedValue[-1][gen] = {}
            elif line[0] == "VALID_STEPS:":
                stepOrder = line[1:-1]
            elif line[0] == "ExpectedValues:":
                for i in range(len(stepOrder)):
                    runsOfGenToStepToExpectedValue[-1][gen][stepOrder[i]] = float(line[1+i])
    genToStepToExpectedValue = {}
    for run in runsOfGenToStepToExpectedValue:
        for gen in run.keys():
            if gen not in genToStepToExpectedValue:
                genToStepToExpectedValue[gen] = {}
            for step in run[gen].keys():
                if step not in genToStepToExpectedValue[gen].keys():
                    genToStepToExpectedValue[gen][step] = 0
                genToStepToExpectedValue[gen][step] += run[gen][step]/len(runsOfGenToStepToExpectedValue)
    return genToStepToExpectedValue[max(genToStepToExpectedValue.keys())]

simulations = {"K6":[],"DERP":[],"Even Odd":[],"Xor":[]}#,"K16":[]}
sums = {"K6":0,"DERP":0,"Even Odd":0,"Xor":0}
steps = {}

for simulation in simulations.keys():
    simulations[simulation] = getStepToExpectedValueLast(filename = askopenfilename(title=simulation))
    # print(simulation)
    # print(simulations[simulation])
    for step in simulations[simulation].keys():
        if step not in steps:
            steps[step] = {}
        steps[step][simulation] = simulations[simulation][step]

fig, ax = plt.subplots()
NUM_COLORS = len(steps.keys())
cm = plt.get_cmap('tab20')
i = 0
for step in steps.keys():
    ax.bar(steps[step].keys(), steps[step].values(), .35, label = step, bottom=list(sums.values()),color=cm(1.*i/NUM_COLORS))
    for sim in steps[step]:
        sums[sim] += steps[step][sim]
    i+=1

ax.set_ylabel('Expected Number')
ax.set_title('Expected Number on different simulations')
ax.legend(bbox_to_anchor=(1,1), loc="upper left")

plt.show()
    

