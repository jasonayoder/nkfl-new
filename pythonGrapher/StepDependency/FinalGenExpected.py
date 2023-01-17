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


runsOfGenToStepToExpectedValue = [{}]
stepOrder = []

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

fig = plt.figure()
ax = fig.subplots()
plt.xticks(range(len(stepOrder)),stepOrder)
plt.xlabel('Steps')
plt.ylabel('Expected Value')
plt.title("Expected number of each step in final generation")
vals = []
for step in stepOrder:
    vals.append(genToStepToExpectedValue[max(genToStepToExpectedValue.keys())][step])
plt.bar(range(len(vals)),vals)
plt.setp(ax.get_xticklabels(), rotation=15, ha="right",
         rotation_mode="anchor")
plt.show()