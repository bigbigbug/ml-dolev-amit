from matplotlib.axis import XAxis
from scipy.stats.mstats_basic import friedmanchisquare
from scipy.stats.morestats import wilcoxon, binom_test
from matplotlib.pyplot import *
from numpy.lib.function_base import average
import numpy
from matplotlib.mlab import normpdf


J48 = [98.44097995545657,64.38053097345133,77.87610619469027,94.56366237482118,57.142857142857146,52.13849287169043,70.5,57.77777777777778,73.828125,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,82.31009365244537,88.95,82.25806451612904,67.12962962962963,100.0,97.05246913580247,80.51282051282051,70.0,96.19047619047619,75.83176396735719,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,84.55114822546973,90.0,72.45862884160756,81.51515151515152,58.125765618619845,92.07920792079207,]
IBk = [99.10913140311804,52.876106194690266,77.87610619469027,95.13590844062946,63.80952380952381,44.33129667345553,72.0,74.44444444444444,70.18229166666667,100.0,82.45614035087719,79.16666666666667,82.43243243243244,54.26923076923077,76.69094693028096,95.85,76.61290322580645,59.25925925925926,100.0,98.37962962962963,96.41025641025641,66.66666666666667,96.57142857142857,91.46264908976774,93.33333333333333,99.24953095684803,90.78461204086068,62.25165562913907,98.95615866388309,60.0,69.8581560283688,99.29292929292929,65.39403838301348,96.03960396039604,]
useAllAttributes7NN = [98.55233853006682,63.05309734513274,83.6283185840708,94.70672389127324,62.857142857142854,49.89816700610998,71.0,71.48148148148148,72.91666666666667,100.0,78.94736842105263,83.33333333333333,78.37837837837837,69.03846153846153,81.47762747138397,90.35,81.45161290322581,59.25925925925926,100.0,97.42283950617283,81.02564102564102,70.0,96.52380952380952,77.96610169491525,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,87.160751565762,90.0,72.93144208037825,87.87878787878788,61.739485504287465,95.04950495049505,]


def createGraph(list1,list2=None,list3=None,_title='',firstTitle='random_reordering',secondTitle='spins_first',thirdTitle='moves_first'):
    n, bins, patches = hist([list1,list2,list3],bins=20,normed = 1,
    color=['crimson', 'orange', 'chartreuse'], label=[firstTitle + " (" + str(average(list1))[0:5] + ")", secondTitle + " (" + str(average(list2))[0:5] + ")", thirdTitle + " (" + str(average(list3))[0:5] + ")"])
    legend()
    title(_title)
#    mu = average(list1)
#    sigma = numpy.std(list1) 
#    y = normpdf( bins, mu, sigma)
#    l = plot(bins, y, 'k--', linewidth=7,color='crimson')
#    if (list2 != None):
#        mu = average(list2)
#        sigma = numpy.std(list2) 
#        y = normpdf( bins, mu, sigma)
#        l = plot(bins, y, 'k--', linewidth=7,color='orange')
#    if (list3 != None):
#        mu = average(list3)
#        sigma = numpy.std(list3) 
#        y = normpdf( bins, mu, sigma)
#        l = plot(bins, y, 'k--', linewidth=7,color='chartreuse')
    xlabel('%correct')
    ylabel('frequency')
    show()

print 'wilcoxon test for J48 or C4.5(7NN):', wilcoxon(J48, useAllAttributes7NN)
print 'wilcoxon test for IBk or C4.5(7NN):', wilcoxon(IBk, useAllAttributes7NN)

x = (0,0)
for i in range(len(J48)):
    if J48[i] < useAllAttributes7NN[i]:
      x = (x[0]+1,x[1])
    elif J48[i] > useAllAttributes7NN[i]:
        x = (x[0], x[1]+1)
        
print x
print binom_test(x, 0,0.4)

print 'J48 = ', average(J48)
print 'IBk = ', average(IBk)
print 'useAllAttributes7NN = ', average(useAllAttributes7NN)


createGraph(J48, IBk, useAllAttributes7NN, 'J48 -vs- IBk -vs- C4.5(7NN)',firstTitle='J48',secondTitle='IBk',thirdTitle='C4.5(7NN)')


