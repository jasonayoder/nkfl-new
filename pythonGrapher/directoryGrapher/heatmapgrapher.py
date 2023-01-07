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
# #It's nice to hardcode this value if you're making a lot of graphs
directory = askdirectory()
# print(directory)
files = []
for file in sorted(os.listdir(directory)):
    filename = os.fsdecode(file)
    if filename.endswith(".csv"): 
        # filenames.append(filename[0:len(filename)-4])
        files.append(directory + "/" + file)

#Someday make it extract these from the .csv
topGen = '50'
genInc = '5'
stratlen = 50

max = 0.1
inc = 0.005


# filename = askopenfilename()
PMBMFit = [] #1st dim PM, 2nd dim BM, 3rd dim Fit
for i in range(1,int(max/inc),1):
    arr = []
    for j in range(1, int(max/inc), 1):
        arr.append([])
    PMBMFit.append(arr)



genNum = 0
pm=0
bm=0
#evo, shc, rw, bal, alternate
for filenum in range(len(files)):
    with open(files[filenum]) as csvfile:
        datareader = csv.reader(csvfile, delimiter=',')
        for row in datareader:
            if(len(row)==0):
                continue
            elif(row[0]=="SIMULATION"):
                genNum = 0
                pm = float(row[1][3:])
                bm = float(row[2][3:])
            elif(row[0]=="GENERATION"):
                genNum = int(row[1])
            elif(row[0]=='PROGRAM_ROW' or row[0].startswith('BLOCK_ROW')):
                continue
            elif(row[0]=='TOTAL_STRATEGY_ROW'):
                continue
            elif(row[0]=='FITNESS_ROW'):
                if(genNum == int(topGen)):
                    pmindex = int(pm/inc)
                    bmindex = int(bm/inc)
                    if(pmindex != 0 and bmindex != 0):
                        PMBMFit[pmindex-1][bmindex-1].append(float(row[len(row)-1]))

for i in range(0,int(max/inc)-1, 1):
    for j in range(0, int(max/inc)-1, 1):
        print(i)
        print(j)
        PMBMFit[i][j] = statistics.median(PMBMFit[i][j])

print(len(PMBMFit))
print(PMBMFit)

# PMBMFit[5][0]=100

steps = [0.005,0.01,0.015,0.02,0.025,0.03,0.035,0.04,0.045,0.05,0.055,0.06,0.065,0.07,0.075,0.08,0.085,0.09,0.095,0.1]

fig, ax = plt.subplots()
im = ax.imshow(PMBMFit,  interpolation='nearest')#cmap='hot',
ax.invert_yaxis()
ax.set_ylabel("Program Mutation Rate")
ax.set_xlabel("Block Mutation Rate")
cbar = ax.figure.colorbar(im, ax=ax)
ax.set_xticks(np.arange(20), labels=steps)
ax.set_yticks(np.arange(20), labels=steps)
# fig.colorbar()
plt.show()


# toPlot = []

# for filenum in range(len(filenames)):
#     plotarr = []
#     for i in range (0, int(topGen)+1, int(genInc)):
#         arr = []
#         for val in dicts[filenum].get(i):
#             arr.append(val)
#         plotarr.append(arr)
#     toPlot.append(plotarr)



# fig, ax = plt.subplots()


# index = 0
# for plot in toPlot:
#     plot_error = "standard_error"
#     #plot_error = "standard_deviation"
#     mean_values = []
#     lower_errors = []
#     upper_errors = []

#     print(filenames[index] + " plotting...")
#     print("Length " + str(len(plot)))

#     xaxis = np.arange(0, int(topGen), int(genInc))
#     xaxis = np.append(xaxis, int(topGen))#we want it to be inclusive of top gen
#     # print(maxNumOfWalks

#     for i in range(len(plot)):
#         # print(plot[i])
#         # if(plot[i]==[]):
#         #     continue
#         mean = statistics.mean(plot[i])
#         mean_values.append(mean)

#         std = statistics.stdev(plot[i])
#         if plot_error == "standard_error":
#             error = std/np.sqrt(len(plot[i]))
#             lower_errors.append( mean - error )
#             upper_errors.append( mean + error )
#         elif plot_error == "standard_deviation":
#             lower_errors.append( mean - std )
#             upper_errors.append( mean + std )
#         else:
#             print("plot_error must be standard_error or standard_deviation")
#             exit(1)
#     # for i in range(l)
                
#     # print(looksBetweenWalksPerSim)
#     # print(colors)
#     leg1 = ax.plot(xaxis, mean_values,  color = colors[index], label = filenames[index])
#     legerror1 = ax.fill_between(xaxis, lower_errors, upper_errors, alpha=0.25, color = colors[index])

#     index = index + 1

# ax.set_xlabel('Generations')
# ax.set_ylabel('Avg Final Fitness of Best in Generation')
# handles, labels = ax.get_legend_handles_labels()
# # sort both labels and handles by labels
# labels, handles = zip(*sorted(zip(labels, handles), key=lambda t: t[0]))
# ax.legend(handles, labels, loc = 'upper right')

# # ax.set_ylim([0, max(mean_values) + 0.1])
# # ax2.set_ylim([0, 1])
# # ax2.legend(loc = 'upper right')
# fig.set_size_inches(5,5 )  


# SMALL_SIZE = 10
# MEDIUM_SIZE = 12
# BIGGER_SIZE = 13

# plt.rc('font', size=BIGGER_SIZE)          # controls default text sizes
# plt.rc('axes', titlesize=BIGGER_SIZE)     # fontsize of the axes title
# plt.rc('axes', labelsize=MEDIUM_SIZE)    # fontsize of the x and y labels
# plt.rc('xtick', labelsize=SMALL_SIZE)    # fontsize of the tick labels
# plt.rc('ytick', labelsize=SMALL_SIZE)    # fontsize of the tick labels
# plt.rc('legend', fontsize=MEDIUM_SIZE)    # legend fontsize
# plt.rc('figure', titlesize=BIGGER_SIZE)  # fontsize of the figure title

# # plt.savefig("plot.png", dpi=300, bbox_inches='tight')
# plt.show()