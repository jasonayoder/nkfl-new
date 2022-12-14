import csv
import os, glob
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib import cm
# from colorspacious import cspace_converter
from collections import OrderedDict
from tkinter.filedialog import askopenfilename
from tkinter.filedialog  import askdirectory  
from colour import Color
 
import statistics

cmaps=OrderedDict()
#ask the user to sleect the folder containing the experiments
# directory = 'C:/Users/jacob/git/nkfl-new/pythonGrapher/121222/toGraph'#It's nice to hardcode this value if you're making a lot of graphs
directory = askdirectory()
print(directory)

topGen = '400'
genInc = '5'
stratlen = 50


filename = askopenfilename()

gen_step_fit = []

for i in range(0, int(topGen)+1, int(genInc)):
    steps = []
    for i in range(stratlen+1):
        steps.append([])
    gen_step_fit.append(steps)

genNum = 0


with open(filename) as csvfile:
    datareader = csv.reader(csvfile, delimiter=',')
    for row in datareader:
        if(len(row)==0):
            continue
        elif(row[0]=="SIMULATION"):
            genNum = 0
        elif(row[0]=="GENERATION"):
            genNum = int(int(row[1])/int(genInc))
        elif(row[0]=='PROGRAM_ROW' or row[0].startswith('BLOCK_ROW')):
            continue
        elif(row[0]=='TOTAL_STRATEGY_ROW'):
            continue
        elif(row[0]=='FITNESS_ROW'):
            for index in range(1,len(row)):
                # print('gen = ' + str(genNum))
                # print('step num = ' + str(index))
                gen_step_fit[genNum][index-1].append(float(row[index]))

fig = plt.figure()
ax = fig.add_subplot(projection='3d')

index = 0


colors = []
for i in range(len(gen_step_fit)):
    colors.append(cm.CMRmap(i/(len(gen_step_fit)*2)))
for step_fit in gen_step_fit:
    plot_error = "standard_error"
    #plot_error = "standard_deviation"
    mean_values = []
    lower_errors = []
    upper_errors = []

    # print(namesToPlot[index] + " plotting...")
    # print("Length " + str(len(plot)))

    xaxis = np.arange(0, stratlen+1)
    # xaxis = np.append(xaxis, int(topGen))#we want it to be inclusive of top gen
    # print(maxNumOfWalks

    plot = step_fit
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
    # print(colors)
    leg1 = ax.plot(xaxis, mean_values, zs=index, zdir='y', color = colors[index])
    # legerror1 = ax.fill_between(xaxis, lower_errors, upper_errors, alpha=0.25, color = colors[index])
    index = index + 1

ax.set_xlabel('Step')
ax.set_ylabel('Generations')
ax.set_zlabel('Fitness')
# handles, labels = ax.get_legend_handles_labels()
# # sort both labels and handles by labels
# labels, handles = zip(*sorted(zip(labels, handles), key=lambda t: t[0]))
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

# plt.savefig("plot.png", dpi=300, bbox_inches='tight')
plt.show()