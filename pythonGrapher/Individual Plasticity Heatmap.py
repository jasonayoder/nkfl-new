import csv
import numpy as np
import matplotlib.pyplot as plt
# from colorspacious import cspace_converter
from tkinter.filedialog import askopenfilename

#filename = askopenfilename()


def plot(filename,n,index):
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
    arr = []
    for i in range(len(runTogenToPlasticityArr[index][maxgen])):
        el = runTogenToPlasticityArr[index][maxgen][i].rjust(32,'0')
        arr.append([])
        for j in range(n):
            arr[-1].append(int(el[-(j+1)]))
    data = np.array(np.rot90(arr))
    plt.imshow(data,cmap='inferno')

n = 20
index = 0

# labels = ['Plastic', 'Decreasing', 'Increasing', 'Evolved']#,'Flip', 'Shift']
plot(askopenfilename(),n,index)
plt.show()