from cProfile import label
import csv
from tkinter.filedialog import askopenfilename
import matplotlib.pyplot as plt

filename = askopenfilename()
fig,axes = plt.subplots()
data = []
with open(filename) as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
        data.append(len(row))
plt.plot(range(len(data)),data,label="Number of Local Maxima")
plt.xlabel("Cycle")
plt.ylabel("number of Local Maxima")
plt.title("Local Maxima vs Cycle on Linear Interpolation DOP")
plt.show()