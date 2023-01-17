from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from mpl_toolkits.axes_grid1 import make_axes_locatable
from turtle import st
import numpy as np
import matplotlib.pyplot as plt

filename = askopenfilename()

gen = -1
runs = 0
step = 0

runsOfGenToStepToStepToCovariance = [{}]
stepOrder = []

with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for line in reader:
        if len(line)==0:
            runsOfGenToStepToStepToCovariance.append({})
        elif line[0] == "GENERATION":
            gen=int(line[1])
            runsOfGenToStepToStepToCovariance[-1][gen] = {}
        elif line[0] == "VALID_STEPS:":
            stepOrder = line[1:-1]
            step = 0
        elif line[0].startswith("Probability_Given:"):
            runsOfGenToStepToStepToCovariance[-1][gen][stepOrder[step]] = {}
            for i in range(len(stepOrder)):
                runsOfGenToStepToStepToCovariance[-1][gen][stepOrder[step]][stepOrder[i]] = float(line[1+i])
            step+=1

genToStepToStepToCovariance = {}
for run in runsOfGenToStepToStepToCovariance:
    for gen in run.keys():
        if gen not in genToStepToStepToCovariance:
            genToStepToStepToCovariance[gen] = {}
        for step1 in run[gen].keys():
            if step1 not in genToStepToStepToCovariance[gen].keys():
                genToStepToStepToCovariance[gen][step1] = {}
            for step2 in run[gen][step1].keys():
                if step2 not in genToStepToStepToCovariance[gen][step1].keys():
                    genToStepToStepToCovariance[gen][step1][step2] = 0
                genToStepToStepToCovariance[gen][step1][step2] += run[gen][step1][step2]/len(runsOfGenToStepToStepToCovariance)

maxgen = max(genToStepToStepToCovariance.keys())

maxgenArray = []
for step1 in stepOrder:
    maxgenArray.append([])
    for step2 in stepOrder:
        maxgenArray[-1].append(genToStepToStepToCovariance[maxgen][step1][step2])

data = np.array(maxgenArray)
fig, ax = plt.subplots()
im = ax.imshow(data,cmap='inferno')
ax.set_xticks(np.arange(len(stepOrder)),labels = stepOrder)
ax.set_yticks(np.arange(len(stepOrder)),labels = stepOrder)
plt.setp(ax.get_xticklabels(), rotation=45, ha="right",
         rotation_mode="anchor")
divider = make_axes_locatable(ax)
cax = divider.append_axes("right", size="5%", pad=0.05)
plt.colorbar(im,cax=cax)
fig.tight_layout()
print(filename)
ax.set_title("P(X|Y) EO6")
ax.set_xlabel("Step X")
ax.set_ylabel("Step Y")
for i in maxgenArray:
    out = ""
    for j in i:
        out += format(j,'.2f')
        out += ", "
    print(out)
plt.show()
