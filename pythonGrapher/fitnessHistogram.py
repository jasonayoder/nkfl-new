import csv
from tkinter.filedialog import askopenfilename
from copy import copy
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.colors import LogNorm

# rng = np.random.default_rng(19680801)
filename = askopenfilename()
fig,axes = plt.subplots(3)
data = []
n_bins = 200
with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
        data.append([])
        for col in row:
            data[-1].append(float(col))

# dist1 = rng.standard_normal(30000)
# axes[0].hist(dist1)
axes[0].hist(data[0],bins=n_bins)
axes[0].set_yscale('log')
axes[1].hist(data[int(len(data)/2)],bins=n_bins)
axes[1].set_yscale('log')
axes[2].hist(data[-1],bins=n_bins)
axes[2].set_yscale('log')

plt.show()