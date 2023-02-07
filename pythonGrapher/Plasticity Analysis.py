import csv
import numpy as np
import matplotlib.pyplot as plt
# from colorspacious import cspace_converter
from tkinter.filedialog import askopenfilename

#filename = askopenfilename()


def plot(filename,n):
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
    # data = {'p1':[],'p11':[],'p01':[]}
    data = [[],[],[]]
    for run in runTogenToPlasticityArr:
        if(len(run.keys())==0):
            break
        maxgen = min(run.keys())
        measures = [{'n1':0,'n0':0,'n11':0,'n00':0,'n10':0,'n01':0}]*n
        for i in range(len(run[maxgen])):
            run[maxgen][i] = run[maxgen][i].rjust(32,'0')
            for j in range(n):
                if(run[maxgen][i][-j]=='1'):
                    measures[j]['n1']+=1
                else:
                    measures[j]['n0']+=1
                if(i>0):
                    if(run[maxgen][i-1][-j]=='1'):
                        if(run[maxgen][i][-j]=='1'):
                            measures[j]['n11']+=1
                        else:
                            measures[j]['n10']+=1
                    else:
                        if(run[maxgen][i][-j]=='1'):
                            measures[j]['n01']+=1
                        else:
                            measures[j]['n00']+=1
        for i in range(n):
            if(measures[i]['n1']==0):
                data[0].append(0)
            else:
                data[0].append(measures[i]['n1']/(measures[i]['n1']+measures[i]['n0']))
            if(measures[i]['n11']==0):
                data[1].append(0)
            else:    
                data[1].append(measures[i]['n11']/(measures[i]['n11']+measures[i]['n10']))
            if(measures[i]['n01']==0):
                data[2].append(0)
            else:
                data[2].append(measures[i]['n01']/(measures[i]['n01']+measures[i]['n00']))
    plt.violinplot(data,showmeans=True)

    data = [[],[],[]]
    for run in runTogenToPlasticityArr:
        if(len(run.keys())==0):
            break
        maxgen = max(run.keys())
        measures = [{'n1':0,'n0':0,'n11':0,'n00':0,'n10':0,'n01':0}]*n
        for i in range(len(run[maxgen])):
            run[maxgen][i] = run[maxgen][i].rjust(32,'0')
            for j in range(n):
                if(run[maxgen][i][-j]=='1'):
                    measures[j]['n1']+=1
                else:
                    measures[j]['n0']+=1
                if(i>0):
                    if(run[maxgen][i-1][-j]=='1'):
                        if(run[maxgen][i][-j]=='1'):
                            measures[j]['n11']+=1
                        else:
                            measures[j]['n10']+=1
                    else:
                        if(run[maxgen][i][-j]=='1'):
                            measures[j]['n01']+=1
                        else:
                            measures[j]['n00']+=1
        for i in range(n):
            if(measures[i]['n1']==0):
                data[0].append(0)
            else:
                data[0].append(measures[i]['n1']/(measures[i]['n1']+measures[i]['n0']))
            if(measures[i]['n11']==0):
                data[1].append(0)
            else:    
                data[1].append(measures[i]['n11']/(measures[i]['n11']+measures[i]['n10']))
            if(measures[i]['n01']==0):
                data[2].append(0)
            else:
                data[2].append(measures[i]['n01']/(measures[i]['n01']+measures[i]['n00']))
    plt.violinplot(data,showmeans=True)#,label="Evolved Distribution")
    # plt.legend("Random","Evolved")
    plt.xticks([1,2,3],labels = ["P(1)","P(11|1x)","P(01|0x)"])

    

n = 20

# labels = ['Plastic', 'Decreasing', 'Increasing', 'Evolved']#,'Flip', 'Shift']
plot(askopenfilename(),n)
plt.show()