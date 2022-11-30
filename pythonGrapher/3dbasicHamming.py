from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from turtle import st
from matplotlib.colors import rgb2hex
import numpy as np
import matplotlib.pyplot as plt

filename = askopenfilename()

gen = -1
runs = 0

randWalk = steepest = alternateLookWalk = False

generationFitnesses = [{}]
# comparisonFitnesses = []

with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for line in reader:
        if line[0] == "GENERATION":
            gen=int(line[1])
        elif line[0] == "HAMMING_DISTANCE_FROM_PHENOTYPE_TO_BEST":
                generationFitnesses[runs][gen] = [int(el) for el in line[1:]]
        elif line[0] == "COMPARISON_STRATEGIES":
            runs += 1
            generationFitnesses.append({})

ax = plt.figure().add_subplot(projection='3d')

genFitRearrange = {}
for runVals in generationFitnesses:
    for key,val in runVals.items():
        if not key in genFitRearrange.keys():
            genFitRearrange[key] = []
        genFitRearrange[key].append(val)
maxGen = max(genFitRearrange.keys())
for gen,vals in genFitRearrange.items():
    x = range(0,len(vals[0]))
    y = np.average(vals, axis=0)
    ax.plot(x,y,zs=gen,zdir='z',color=[gen/maxGen,0,0])


# plt.legend()
plt.xlabel("steps") 
plt.ylabel("average hamming distance of best")
ax.set_zlabel("generation")
plt.show()