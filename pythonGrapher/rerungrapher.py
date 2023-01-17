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

fig, ax = plt.subplots()
labels = [f'N:{n} K:{k}' for k in nkToFinalFitnesses[n].keys() for n in nkToFinalFitnesses.keys()]
data = [np.mean(nkToFinalFitnesses[n][k]) for k in nkToFinalFitnesses[n].keys() for n in nkToFinalFitnesses.keys()]
error = [np.std(nkToFinalFitnesses[n][k])/np.sqrt(len(nkToFinalFitnesses[n][k])) for k in nkToFinalFitnesses[n].keys() for n in nkToFinalFitnesses.keys()]
ax.bar(x=range(len(labels)),height = data,yerr=error,tick_label=labels) 
# ax.set_xticklabels(labels)
plt.show()