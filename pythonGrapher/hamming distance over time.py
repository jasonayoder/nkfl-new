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

with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for line in reader:
        if line[0] == "GENERATION":
            # print("gen:"+line[1])
            gen=int(line[1])
        elif line[0] == "HAMMING_DISTANCE_FROM_BEST":
            generationFitnesses[runs][gen] = [float(el) for el in line[1:]]
        elif line[0] == "COMPARISON_STRATEGIES":
            runs += 1
            generationFitnesses.append({})
genFitRearrange = {}
for runVals in generationFitnesses:
    for key,val in runVals.items():
        if not key in genFitRearrange.keys():
            genFitRearrange[key] = []
        genFitRearrange[key].append(val)

mean = [] 
low = []
high = []
for key,value in genFitRearrange.items():
    vals = []
    for v in value:
        vals.append(v)
    mean.append(np.mean(vals))
    low.append(np.mean(vals) - (np.std(vals)/np.sqrt(len(vals))))
    high.append(np.mean(vals) + (np.std(vals)/np.sqrt(len(vals))))

plt.plot(genFitRearrange.keys(), mean,label="Final Generation")
plt.fill_between(genFitRearrange.keys(), low, high, alpha=.25)

plt.legend()
plt.xlabel("Generation") 
plt.ylabel("average hamming distance to best strategy") 
plt.show()