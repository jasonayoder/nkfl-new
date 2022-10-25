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

generationWalkProb = [{}]
# comparisonFitnesses = []

with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for line in reader:
        if line[0] == "GENERATION":
            gen=int(line[1])
        
        elif line[0] == "WALK_PROBABILITY_ROW":
            generationWalkProb[runs][gen] = []
            for s in line[1:]:
                generationWalkProb[runs][gen].append(float(s))
                
        elif line[0] == "COMPARISON_STRATEGIES":
            runs += 1
            generationWalkProb.append({})

ax = plt.figure().add_subplot(projection='3d')

genWalkRearrange = {}
for runVals in generationWalkProb:
    for key,val in runVals.items():
        if not key in genWalkRearrange.keys():
            genWalkRearrange[key] = []
        genWalkRearrange[key].append(val)
maxGen = max(genWalkRearrange.keys())
for gen,val in genWalkRearrange.items():
    x = range(0,len(val[0]))
    y = np.average(val,axis=0)
    ax.plot(x,y,zs=gen,zdir='z',color=[gen/maxGen,0,0])

# plt.legend()
plt.xlabel("steps") 
plt.ylabel("walk probability")
ax.set_zlabel("generation")
plt.show()