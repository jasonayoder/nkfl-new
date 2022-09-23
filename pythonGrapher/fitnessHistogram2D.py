import csv
from tkinter.filedialog import askopenfilename
from copy import copy
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.colors import LogNorm


filename = askopenfilename()
fig,axes = plt.subplots()
data = []
cycle = []
e = 0
with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
        for i in range(len(row)):
            cycle.append(e)
            data.append(float(row[i]))
        e+=1
cmap = copy(plt.cm.plasma)
cmap.set_bad(cmap(0))
h, xedges, yedges = np.histogram2d(cycle, data,bins=100)
pcm = axes.pcolormesh(xedges, yedges, h.T,norm=LogNorm(), cmap=cmap,rasterized=True)#
fig.colorbar(pcm, ax=axes, label="# points", pad=0)
axes.set_title("2d histogram and log color scale")

plt.show()