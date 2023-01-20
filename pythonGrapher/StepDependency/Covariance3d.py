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
        elif line[0].startswith("CovarianceWith:"):
            runsOfGenToStepToStepToCovariance[-1][gen][stepOrder[step]] = {}
            for i in range(len(stepOrder)):
                runsOfGenToStepToStepToCovariance[-1][gen][stepOrder[step]][stepOrder[i]] = float(line[1+i])
            step+=1

genToStepToStepToCovariance = {}
for run in runsOfGenToStepToStepToCovariance:
    for gen in run.keys():
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
fig = plt.figure()
ax1 = fig.add_subplot(111, projection='3d')

xpos = np.arange(0,len(stepOrder),1)
ypos = np.arange(0,len(stepOrder),1)
xpos, ypos = np.meshgrid(xpos,ypos)
xpos = xpos.flatten()
ypos = ypos.flatten()
zpos = np.zeros(len(stepOrder)*len(stepOrder))
dx = np.ones(len(stepOrder)*len(stepOrder))
dy = np.ones(len(stepOrder)*len(stepOrder))
dz = data.flatten()
colors = plt.cm.jet((dz-dz.min())/(dz.max()-dz.min()))
ax1.bar3d(xpos, ypos, zpos, dx, dy, dz, colors)
ax1.set_xlabel('Step Type')
ax1.set_ylabel('Step Type')
ax1.set_xticklabels(stepOrder)
ax1.set_yticklabels(stepOrder)
ax1.set_zlabel('Covariance')
for i in maxgenArray:
    out = ""
    for j in i:
        out += format(j,'.2f')
        out += ", "
    print(out)
plt.show()

