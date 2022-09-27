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
# axes[0].hist(data[0],bins=n_bins)
# axes[0].set_yscale('log')
# axes[1].hist(data[int(len(data)/2)],bins=n_bins)
# axes[1].set_yscale('log')
# axes[2].hist(data[-1],bins=n_bins)
# axes[2].set_yscale('log')

for i in range(len(axes)):
    c = int((len(data)-1)*i/(len(axes)-1))
    axes[i].hist(data[c],bins=n_bins)
    axes[i].set_yscale('log')
    axes[i].set_ylabel('Frequency')
    axes[i].set_xlabel('Fitness')
    axes[i].set_title('Cycle %d' %(c))

plt.subplots_adjust(hspace=1.2)
print("mean, standard deviation")
print(np.mean(data[0]),np.std(data[0]))
print(np.mean(data[-1]),np.std(data[-1]))


plt.show()