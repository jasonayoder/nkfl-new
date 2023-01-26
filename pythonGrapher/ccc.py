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

cmaps=OrderedDict()
#ask the user to sleect the folder containing the experiments
# alternate = askopenfilename()

topGen = '200'
genInc = '5'
stratlen = 50

#filenames = ['All Phenotypic', 'All Genetic', 'All Epigenetic', 'All Primed']
filenames = ['Plasticity Full', 'Plasticity Increase','Plasticity Decrease','Plasticity Evolved', 'Plasticity Flip', 'Plasticity Shift']
#filenames = ['Phenotypic + Epigenetic', 'Phenotypic + Predisposed', 'Phenotypic + Genotypic', 'Epigenetic + Predisposed', 'Epigenetic + Genotypic', 'Predisposed + Genotypic']#, 'P10 G6', 'P14 G10', 'P0 G0', 'P20 G20']
dicts = []
for i in range(len(filenames)):
    dicts.append({})
files = []
for i in range(len(filenames)):
    files.append(askopenfilename(title=filenames[i]))

genNum = 0

for j in range (len(filenames)):
    for i in range (0, int(topGen)+1, int(genInc)):
        arr = []
        dicts[j].update({i : arr})

#evo, shc, rw, bal, alternate
for filenum in range(len(filenames)):
    with open(files[filenum]) as csvfile:
        datareader = csv.reader(csvfile, delimiter=',')
        for row in datareader:
            if(len(row)==0):
                continue
            elif(row[0]=="SIMULATION"):
                genNum = 0
            elif(row[0]=="GENERATION"):
                genNum = int(row[1])
            elif(row[0]=='PROGRAM_ROW' or row[0].startswith('BLOCK_ROW')):
                continue
            elif(row[0]=='TOTAL_STRATEGY_ROW'):
                continue
            elif(row[0]=='FITNESS_ROW'):
                dicts[filenum].get(genNum).append(float(row[len(row)-1]))


toPlot = []

for filenum in range(len(filenames)):
    plotarr = []
    for i in range (0, int(topGen)+1, int(genInc)):
        arr = []
        for val in dicts[filenum].get(i):
            arr.append(val)
        plotarr.append(arr)
    toPlot.append(plotarr)



fig, ax = plt.subplots()
colors = [Color("Black"), Color("Yellow"), Color("Orange"), Color("Red"), Color("Purple"), Color("Pink"), Color("Blue")]
namesToPlot = filenames#, 'Random Walker', 'Steepest Hill Climber', 'Balance']


index = 0
for plot in toPlot:
    plot_error = "standard_error"
    #plot_error = "standard_deviation"
    mean_values = []
    lower_errors = []
    upper_errors = []

    print(namesToPlot[index] + " plotting...")
    print("Length " + str(len(plot)))

    xaxis = np.arange(0, len(plot)*5, 5)
    # print(maxNumOfWalks

    for i in range(len(plot)):
        mean = statistics.mean(plot[i])
        mean_values.append(mean)

        std = statistics.stdev(plot[i])
        if plot_error == "standard_error":
            error = std/np.sqrt(len(plot[i]))
            lower_errors.append( mean - error )
            upper_errors.append( mean + error )
        elif plot_error == "standard_deviation":
            lower_errors.append( mean - std )
            upper_errors.append( mean + std )
        else:
            print("plot_error must be standard_error or standard_deviation")
            exit(1)
    # for i in range(l)
                
    # print(looksBetweenWalksPerSim)

    leg1 = ax.plot(xaxis, mean_values, color = colors[index].hex, label = namesToPlot[index])
    legerror1 = ax.fill_between(xaxis, lower_errors, upper_errors, alpha=0.25, color = colors[index].hex)

    index = index + 1

ax.set_xlabel('Generations')
ax.set_ylabel('Avg Final Fitness of Best in Generation')
ax.legend(loc = 'upper right')

# ax.set_ylim([0, max(mean_values) + 0.1])
# ax2.set_ylim([0, 1])
# ax2.legend(loc = 'upper right')
fig.set_size_inches(5,5 )  


SMALL_SIZE = 10
MEDIUM_SIZE = 12
BIGGER_SIZE = 13

plt.rc('font', size=BIGGER_SIZE)          # controls default text sizes
plt.rc('axes', titlesize=BIGGER_SIZE)     # fontsize of the axes title
plt.rc('axes', labelsize=MEDIUM_SIZE)    # fontsize of the x and y labels
plt.rc('xtick', labelsize=SMALL_SIZE)    # fontsize of the tick labels
plt.rc('ytick', labelsize=SMALL_SIZE)    # fontsize of the tick labels
plt.rc('legend', fontsize=MEDIUM_SIZE)    # legend fontsize
plt.rc('figure', titlesize=BIGGER_SIZE)  # fontsize of the figure title

# plt.savefig("plotOutput/"+"fitnesses.png", dpi=300, bbox_inches='tight')
plt.show()