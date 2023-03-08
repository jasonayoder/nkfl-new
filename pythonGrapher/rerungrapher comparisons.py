from cProfile import label, run
import csv
from logging import NullHandler
from re import M, T
from tkinter.filedialog import askopenfilename
from mpl_toolkits.axes_grid1 import make_axes_locatable
from turtle import st
import numpy as np
import matplotlib.pyplot as plt


def getnkToFinalFitness(filename):
    n = 0
    k = 0

    nkToFinalFitnesses = {}

    with open(filename) as csvfile:
        reader = csv.reader(csvfile)
        for line in reader:
            if len(line) == 0:
                continue
            elif line[0] == "RERUN_ON:":
                for i in range(len(line)):
                    if line[i] == "N value:":
                        n = int(line[i+1])
                    elif line[i] == "K value:":
                        k = int(line[i+1])
            elif line[0] == "FITNESS_ROW":
                if n not in nkToFinalFitnesses.keys():
                    nkToFinalFitnesses[n] = {}
                if k not in nkToFinalFitnesses[n]:
                    nkToFinalFitnesses[n][k] = []
                nkToFinalFitnesses[n][k].append(float(line[-1]))
    return nkToFinalFitnesses
        

landscapes = ["K6", "Even Odd K6", "Xor K6", "Random K6"]#["K2", "K6", "K10", "K14"]#"EO K6", "Random K0..14", "XOR K6"]
width = .8
fig, ax = plt.subplots()
for i in range(len(landscapes)):
    landscape = landscapes[i]
    nkToFinalFitnesses = getnkToFinalFitness(askopenfilename(title = landscape))
    labels = []
    data = []
    error = []
    for n in nkToFinalFitnesses.keys():
        for k in nkToFinalFitnesses[n].keys():
            labels.append(f'K:{k}')
            data.append(np.mean(nkToFinalFitnesses[n][k]))
            error.append(np.std(nkToFinalFitnesses[n][k])/np.sqrt(len(nkToFinalFitnesses[n][k])))
    x = np.arange(len(labels))
    ax.bar(x+(width*(-.5+i/len(landscapes)+.5/len(landscapes))),height = data,yerr=error,width=width/len(landscapes),label = landscape) 
    ax.set_xticks(x,labels)
ax.legend(bbox_to_anchor=(1,1), loc="upper left")
# filename = askopenfilename()
# fig, ax = plt.subplots()
# labels = [f'N:{n} K:{k}' for k in nkToFinalFitnesses[n].keys() for n in nkToFinalFitnesses.keys()]
# data = [np.mean(nkToFinalFitnesses[n][k]) for k in nkToFinalFitnesses[n].keys() for n in nkToFinalFitnesses.keys()]
# error = [np.std(nkToFinalFitnesses[n][k])/np.sqrt(len(nkToFinalFitnesses[n][k])) for k in nkToFinalFitnesses[n].keys() for n in nkToFinalFitnesses.keys()]
# ax.bar(x=range(len(labels)),height = data,yerr=error,tick_label=labels) 
# ax.set_xticklabels(labels)
ax.set_xlabel("New Landscape Difficulty")
ax.set_ylabel("Fitness")
ax.set_title("Running the Final Generation on New Landscapes")
plt.show()