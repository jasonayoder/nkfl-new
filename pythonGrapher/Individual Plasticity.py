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


def plot(filename,label,n,s,count):
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
            for j in range(len(runTogenToPlasticityArr[run][maxgen][i])-int(n/s),len(runTogenToPlasticityArr[run][maxgen][i])):
                if(j<len(runTogenToPlasticityArr[run][maxgen][i]) and j>=0):
                    if runTogenToPlasticityArr[run][maxgen][i][j]=='1':
                        data[i][run] += 1
    for i in range(count):
        run = [0]*len(data)
        for j in range(len(data)):
            run[j] = data[j][i]
        xaxis = np.arange(0,len(run))
        plt.plot(xaxis, run,label=(label+str(i)))

# labels = ['Increase', 'Decrease', 'Evolved', 'Increase Evolved', 'Decrease Evolved']
label = 'S1_'
n = 20
s = 1
count = 2

# labels = ['Plastic', 'Decreasing', 'Increasing', 'Evolved']#,'Flip', 'Shift']
plot(askopenfilename(title=label),label,n,s,count)
plt.legend()
plt.ylim([0,n+1])
plt.show()