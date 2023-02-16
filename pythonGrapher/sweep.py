from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from mpl_toolkits.axes_grid1 import make_axes_locatable
from turtle import st
import numpy as np
import matplotlib.pyplot as plt


def FinalFitnessOfGens(filename):
    genNum = 0
    gens = {0:[]}
    with open(filename) as csvfile:
        reader = csv.reader(csvfile)
        for line in reader:
            if(len(line)==0):
                continue
            elif(line[0]=="GENERATION"):
                genNum = int(line[1])
                if(not gens.keys().__contains__(genNum)):
                    gens[genNum] = []
            elif(line[0]=='FITNESS_ROW'):
                gens[genNum].append(float(line[-1]))
    return gens

landscapes = 21#["K2", "K6", "K10", "K14"]#"EO K6", "Random K0..14", "XOR K6"]
width = .8
fig, ax = plt.subplots()

data = []
error = []
for i in range(landscapes):
    genToFinalFitnesses = FinalFitnessOfGens(askopenfilename(title = i))
    maxGen = max(genToFinalFitnesses.keys())
    data.append(np.mean(genToFinalFitnesses[maxGen]))
    error.append(np.std(genToFinalFitnesses[maxGen])/np.sqrt(len(genToFinalFitnesses[maxGen])))
upper = []
lower = []
for i in range(len(data)):
    upper.append(data[i]+error[i])
    lower.append(data[i]-error[i])
xaxis = np.arange(0,105,5)
ax.plot(xaxis, data)
ax.fill_between(xaxis,upper,lower,alpha=.25)
ax.set_ylabel("Fitness after 400 generations")
ax.set_xlabel("Percentage Developed Traits")

plt.show()