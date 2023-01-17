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
            elif line[0] == "ProbabilityEndsWith:":
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

simulations = {"K2":[],"K6":[],"K10":[],"K14":[]}#,"K16":[]}
sums = {"K2":0,"K6":0,"K10":0,"K14":0}
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

ax.set_ylabel('Probability distribution')
ax.set_title('Distribution of Final Steps')
ax.legend(bbox_to_anchor=(1,1), loc="upper left")

plt.show()
    



# fig = plt.figure()
# ax = fig.subplots()
# plt.xticks(range(len(stepOrder)),stepOrder)
# plt.xlabel('Steps')
# plt.ylabel('Probability of program ending with step')
# plt.title("Probability of Each Step Occuring at End")
# vals = []
# for step in stepOrder:
#     vals.append(genToStepToExpectedValue[max(genToStepToExpectedValue.keys())][step])
# plt.bar(range(len(vals)),vals)
# plt.setp(ax.get_xticklabels(), rotation=15, ha="right",
#          rotation_mode="anchor")
# plt.show()