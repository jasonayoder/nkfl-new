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

    for i in range(len(runTogenToPlasticityArr[0][maxgen])):
        data.append([0]*(len(runTogenToPlasticityArr)-1))
        for run in range(len(runTogenToPlasticityArr)-1):
            for j in range(len(runTogenToPlasticityArr[run][maxgen][i])-n,len(runTogenToPlasticityArr[run][maxgen][i])):
                if(j<len(runTogenToPlasticityArr[run][maxgen][i]) and j>=0):
                    if runTogenToPlasticityArr[run][maxgen][i][j]=='1':
                        data[i][run] += 1
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

labels = ['Plastic', 'Decreasing', 'Increasing', 'Evolved','Flip', 'Shift']
for label in labels:
    plot(askopenfilename(title=label),label)
plt.legend()
plt.ylim([0,n+1])
plt.show()