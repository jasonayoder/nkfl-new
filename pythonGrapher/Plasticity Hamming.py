import csv
import os, glob
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib import cm
# from colorspacious import cspace_converter
from collections import OrderedDict
from tkinter.filedialog import askopenfilename
from colour import Color
 
import statistics

#filename = askopenfilename()


n = 20
def plot(filename,label):
    runTogenToPlasticityArr = [{}]
    with open(filename) as csvfile:
        datareader = csv.reader(csvfile, delimiter=',')
        run = 0
        gen = 0
        for row in datareader:
            if(len(row)==0):
                run+=1
                runTogenToPlasticityArr.append({})
            elif(row[0]=='GENERATION'):
                gen = int(row[1])
            elif(row[0]=='Plasticities:'):
                runTogenToPlasticityArr[run][gen] = row[1:-1]
    maxgen = max(runTogenToPlasticityArr[0].keys())
    #data = [[0]*len(runTogenToPlasticityArr)]*len(runTogenToPlasticityArr[0][maxgen])
    data = []

    for i in range(1,len(runTogenToPlasticityArr[0][maxgen])):
        data.append([0]*(len(runTogenToPlasticityArr)-1))
        for run in range(len(runTogenToPlasticityArr)-1):
            cur = runTogenToPlasticityArr[run][maxgen][i].rjust(32,'0')
            pre = runTogenToPlasticityArr[run][maxgen][i-1].rjust(32,'0')
            for j in range(32-n,32):
                if(j<len(cur) and j>=0):
                    if cur[j]!=pre[j]:
                        data[i-1][run] += 1
            data[i-1][run]/=n
    avg = [0]*len(data)
    low = [0]*len(data)
    high = [0]*len(data)
    for i in range(len(data)):
        err = np.std(data[i])/np.sqrt(len(data[i]))
        avg[i] = np.mean(data[i])
        low[i] = avg[i]-err
        high[i] = avg[i]+err

    xaxis = np.arange(0,len(avg))
    plt.plot(xaxis, avg,label=label)
    plt.fill_between(xaxis, low, high, alpha=0.25)

labels = ['Plastic', 'Decreasing', 'Increasing', 'Evolved', 'Flip', 'Shift']
for label in labels:
    plot(askopenfilename(title=label),label)
plt.legend()
plt.ylim([-.5,1.5])
plt.show()