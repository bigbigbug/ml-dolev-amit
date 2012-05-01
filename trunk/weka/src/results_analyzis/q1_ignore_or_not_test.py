from scipy.stats.morestats import wilcoxon, binom_test, shapiro

j48 = [98.44097995545657,64.38053097345133,77.87610619469027,94.56366237482118,57.142857142857146,52.13849287169043,70.5,57.77777777777778,73.828125,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,82.31009365244537,88.95,82.25806451612904,67.12962962962963,100.0,97.05246913580247,80.51282051282051,70.0,96.19047619047619,75.83176396735719,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,84.55114822546973,90.0,72.45862884160756,81.51515151515152,58.125765618619845,92.07920792079207,]
ignoreAttributes1NN = [99.66592427616926,62.83185840707964,83.6283185840708,94.27753934191702,60.0,49.62661235573659,68.4,74.44444444444444,69.140625,100.0,80.70175438596492,83.33333333333333,79.72972972972973,69.11538461538461,77.41935483870968,90.35,83.87096774193549,59.25925925925926,100.0,97.43055555555556,82.05128205128206,65.55555555555556,96.47619047619048,77.96610169491525,93.33333333333333,99.24953095684803,92.63203651380135,62.25165562913907,88.83089770354906,80.0,73.5224586288416,87.17171717171718,61.188240097999184,95.04950495049505,]
ignoreAttributes3NN = [99.66592427616926,62.83185840707964,83.6283185840708,93.13304721030043,60.0,49.62661235573659,71.8,71.11111111111111,71.74479166666667,100.0,75.43859649122807,83.33333333333333,79.72972972972973,69.0,77.41935483870968,90.35,80.64516129032258,59.25925925925926,100.0,97.43055555555556,80.51282051282051,65.55555555555556,96.47619047619048,77.96610169491525,93.33333333333333,99.24953095684803,92.4798956748533,62.25165562913907,88.3089770354906,70.0,73.5224586288416,87.17171717171718,61.188240097999184,95.04950495049505,]
ignoreAttributes5NN = [99.66592427616926,62.83185840707964,83.6283185840708,94.56366237482118,60.0,50.033944331296674,69.7,70.37037037037037,73.95833333333333,100.0,78.94736842105263,83.33333333333333,78.37837837837837,69.03846153846153,80.22892819979188,90.35,80.64516129032258,59.25925925925926,100.0,97.43055555555556,81.02564102564102,70.0,96.47619047619048,77.96610169491525,93.33333333333333,99.53095684803002,92.71897413605738,61.58940397350993,87.68267223382045,90.0,71.8676122931442,87.17171717171718,61.188240097999184,95.04950495049505,]
ignoreAttributes7NN = [98.7750556792873,62.83185840707964,83.6283185840708,94.27753934191702,63.80952380952381,50.30549898167006,70.9,71.48148148148148,73.828125,100.0,75.43859649122807,83.33333333333333,78.37837837837837,69.03846153846153,81.58168574401665,90.35,81.45161290322581,59.25925925925926,100.0,97.42283950617283,81.02564102564102,70.0,96.47619047619048,77.96610169491525,93.33333333333333,99.53095684803002,93.0449902195175,59.602649006622514,87.05636743215031,90.0,71.8676122931442,87.17171717171718,61.188240097999184,95.04950495049505,]
ignoreAttributes9NN = [98.7750556792873,62.83185840707964,83.6283185840708,94.84978540772532,63.80952380952381,50.30549898167006,69.7,70.37037037037037,74.21875,100.0,80.70175438596492,83.33333333333333,76.35135135135135,69.03846153846153,81.47762747138397,90.35,81.45161290322581,59.25925925925926,100.0,97.42283950617283,80.51282051282051,70.0,96.0952380952381,77.96610169491525,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,86.74321503131524,90.0,72.34042553191489,87.17171717171718,58.10534912209065,92.07920792079207,]
ignoreAttributes11NN = [98.7750556792873,62.83185840707964,83.6283185840708,94.84978540772532,63.80952380952381,50.712830957230146,70.5,70.0,73.4375,100.0,80.70175438596492,83.33333333333333,76.35135135135135,69.03846153846153,82.10197710718002,89.55,80.64516129032258,59.25925925925926,100.0,97.4074074074074,80.51282051282051,70.0,96.0952380952381,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,59.602649006622514,86.0125260960334,90.0,72.34042553191489,87.17171717171718,58.10534912209065,92.07920792079207,]
ignoreAttributes13NN = [98.44097995545657,62.83185840707964,83.6283185840708,94.56366237482118,56.19047619047619,50.509164969450104,69.7,67.5925925925926,72.52604166666667,100.0,80.70175438596492,83.33333333333333,77.02702702702703,69.03846153846153,82.51821019771072,89.55,80.64516129032258,59.25925925925926,100.0,97.4074074074074,80.51282051282051,70.0,96.0952380952381,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,59.602649006622514,85.28183716075156,90.0,71.74940898345153,84.34343434343434,58.10534912209065,92.07920792079207,]
ignoreAttributes15NN = [98.44097995545657,62.83185840707964,83.6283185840708,94.56366237482118,56.19047619047619,50.509164969450104,70.4,70.0,72.52604166666667,100.0,78.94736842105263,83.33333333333333,77.02702702702703,69.03846153846153,82.51821019771072,89.55,81.45161290322581,59.49074074074074,100.0,97.4074074074074,80.51282051282051,70.0,96.23809523809524,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,59.602649006622514,84.8643006263048,90.0,71.74940898345153,84.34343434343434,58.391180073499385,92.07920792079207,]
ignoreAttributes17NN = [98.44097995545657,63.05309734513274,83.6283185840708,94.56366237482118,56.19047619047619,50.98438560760353,69.9,67.4074074074074,72.265625,100.0,77.19298245614036,83.33333333333333,77.02702702702703,69.03846153846153,82.51821019771072,89.55,81.45161290322581,60.18518518518518,100.0,97.38425925925925,80.51282051282051,70.0,96.23809523809524,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,58.94039735099338,84.75991649269311,90.0,71.8676122931442,84.34343434343434,58.391180073499385,92.07920792079207,]
ignoreAttributes19NN = [98.44097995545657,63.05309734513274,83.6283185840708,94.56366237482118,56.19047619047619,51.18805159538357,70.2,67.5925925925926,72.39583333333333,100.0,75.43859649122807,83.33333333333333,77.02702702702703,69.03846153846153,82.72632674297607,89.55,81.45161290322581,62.96296296296296,100.0,97.38425925925925,80.51282051282051,70.0,96.23809523809524,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,58.94039735099338,84.55114822546973,90.0,71.8676122931442,84.34343434343434,58.391180073499385,92.07920792079207,]
ignoreAttributes21NN = [98.44097995545657,63.05309734513274,83.6283185840708,94.56366237482118,56.19047619047619,51.18805159538357,70.3,67.22222222222223,73.4375,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,82.93444328824141,88.95,81.45161290322581,64.58333333333333,100.0,97.36111111111111,80.51282051282051,70.0,96.23809523809524,75.89453860640302,93.33333333333333,99.53095684803002,92.97978700282547,58.94039735099338,84.34237995824634,90.0,71.8676122931442,84.34343434343434,58.391180073499385,92.07920792079207,]
ignoreAttributes23NN = [98.44097995545657,63.05309734513274,83.6283185840708,94.56366237482118,56.19047619047619,51.323828920570264,70.3,67.77777777777777,73.56770833333333,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,83.14255983350677,88.95,81.45161290322581,66.89814814814815,100.0,97.36111111111111,80.51282051282051,70.0,96.14285714285714,75.89453860640302,93.33333333333333,99.53095684803002,92.97978700282547,58.94039735099338,84.44676409185804,90.0,71.8676122931442,82.42424242424242,58.738260514495714,92.07920792079207,]
ignoreAttributes25NN = [98.44097995545657,63.05309734513274,82.30088495575221,94.56366237482118,56.19047619047619,51.73116089613035,70.2,68.14814814814815,73.30729166666667,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,83.14255983350677,88.95,81.45161290322581,66.89814814814815,100.0,97.36111111111111,80.51282051282051,70.0,96.14285714285714,75.89453860640302,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,84.23799582463465,90.0,72.45862884160756,82.42424242424242,58.738260514495714,92.07920792079207,]

useAllAttributes1NN = [99.88864142538975,63.05309734513274,83.6283185840708,93.8483547925608,60.95238095238095,48.676171079429736,69.0,74.44444444444444,71.61458333333333,100.0,77.19298245614036,83.33333333333333,79.72972972972973,69.03846153846153,78.04370447450572,90.35,83.87096774193549,59.25925925925926,100.0,97.43055555555556,81.53846153846153,65.55555555555556,96.52380952380952,77.96610169491525,93.33333333333333,99.24953095684803,92.69723973049337,66.2251655629139,88.83089770354906,80.0,73.04964539007092,87.87878787878788,61.739485504287465,95.04950495049505,]
useAllAttributes3NN = [99.88864142538975,63.05309734513274,83.6283185840708,92.70386266094421,60.95238095238095,48.676171079429736,71.4,71.11111111111111,71.875,100.0,77.19298245614036,83.33333333333333,79.72972972972973,69.0,77.83558792924038,90.35,80.64516129032258,59.25925925925926,100.0,97.43055555555556,81.02564102564102,65.55555555555556,96.52380952380952,77.96610169491525,93.33333333333333,99.24953095684803,92.63203651380135,66.2251655629139,88.4133611691023,70.0,73.04964539007092,87.87878787878788,61.739485504287465,95.04950495049505,]
useAllAttributes5NN = [99.88864142538975,63.05309734513274,83.6283185840708,94.9928469241774,60.95238095238095,48.26883910386965,69.8,70.37037037037037,72.52604166666667,100.0,80.70175438596492,83.33333333333333,78.37837837837837,69.03846153846153,80.02081165452654,90.35,80.64516129032258,59.25925925925926,100.0,97.43055555555556,80.51282051282051,70.0,96.52380952380952,77.96610169491525,93.33333333333333,99.53095684803002,92.67550532492936,62.913907284768214,87.89144050104385,90.0,72.93144208037825,87.87878787878788,61.739485504287465,95.04950495049505,]
useAllAttributes7NN = [98.55233853006682,63.05309734513274,83.6283185840708,94.70672389127324,62.857142857142854,49.89816700610998,71.0,71.48148148148148,72.91666666666667,100.0,78.94736842105263,83.33333333333333,78.37837837837837,69.03846153846153,81.47762747138397,90.35,81.45161290322581,59.25925925925926,100.0,97.42283950617283,81.02564102564102,70.0,96.52380952380952,77.96610169491525,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,87.160751565762,90.0,72.93144208037825,87.87878787878788,61.739485504287465,95.04950495049505,]
useAllAttributes9NN = [98.55233853006682,63.05309734513274,83.6283185840708,94.84978540772532,62.857142857142854,49.89816700610998,69.8,70.37037037037037,72.13541666666667,100.0,78.94736842105263,83.33333333333333,76.35135135135135,69.03846153846153,82.10197710718002,90.35,81.45161290322581,59.25925925925926,100.0,97.42283950617283,80.51282051282051,70.0,96.19047619047619,77.96610169491525,93.33333333333333,99.53095684803002,93.0667246250815,59.602649006622514,86.84759916492693,90.0,72.69503546099291,87.87878787878788,58.47284605961617,92.07920792079207,]
useAllAttributes11NN = [98.55233853006682,63.05309734513274,83.6283185840708,94.84978540772532,62.857142857142854,51.052274270196875,70.7,70.0,72.265625,100.0,78.94736842105263,83.33333333333333,76.35135135135135,69.03846153846153,82.20603537981269,89.55,80.64516129032258,59.25925925925926,100.0,97.4074074074074,80.51282051282051,70.0,96.19047619047619,76.14563716258631,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,86.0125260960334,90.0,72.69503546099291,87.87878787878788,58.47284605961617,92.07920792079207,]
useAllAttributes13NN = [98.44097995545657,63.05309734513274,83.6283185840708,94.56366237482118,55.23809523809524,50.509164969450104,69.7,67.5925925925926,73.046875,100.0,78.94736842105263,83.33333333333333,77.70270270270271,69.03846153846153,81.99791883454735,89.55,80.64516129032258,59.25925925925926,100.0,97.4074074074074,80.51282051282051,70.0,96.19047619047619,76.14563716258631,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,85.17745302713988,90.0,72.57683215130024,84.54545454545455,58.47284605961617,92.07920792079207,]
useAllAttributes15NN = [98.44097995545657,63.05309734513274,83.6283185840708,94.56366237482118,55.23809523809524,50.509164969450104,70.6,70.0,73.69791666666667,100.0,77.19298245614036,83.33333333333333,77.70270270270271,69.03846153846153,81.99791883454735,89.55,81.45161290322581,59.49074074074074,100.0,97.4074074074074,80.51282051282051,70.0,96.23809523809524,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,59.602649006622514,84.9686847599165,90.0,72.57683215130024,84.54545454545455,58.47284605961617,92.07920792079207,]
useAllAttributes17NN = [98.44097995545657,63.27433628318584,83.6283185840708,94.56366237482118,55.23809523809524,51.052274270196875,70.1,67.4074074074074,72.91666666666667,100.0,78.94736842105263,83.33333333333333,77.02702702702703,69.03846153846153,82.20603537981269,89.55,81.45161290322581,60.18518518518518,100.0,97.38425925925925,80.51282051282051,70.0,96.23809523809524,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,58.94039735099338,84.8643006263048,90.0,72.45862884160756,84.54545454545455,58.47284605961617,92.07920792079207,]
useAllAttributes19NN = [98.44097995545657,63.27433628318584,83.6283185840708,94.56366237482118,56.19047619047619,51.052274270196875,70.2,67.5925925925926,72.65625,100.0,75.43859649122807,83.33333333333333,77.02702702702703,69.03846153846153,82.41415192507804,89.55,81.45161290322581,62.96296296296296,100.0,97.38425925925925,80.51282051282051,70.0,96.23809523809524,76.14563716258631,93.33333333333333,99.53095684803002,93.00152140838948,59.602649006622514,84.55114822546973,90.0,72.45862884160756,84.54545454545455,58.47284605961617,92.07920792079207,]
useAllAttributes21NN = [98.44097995545657,63.27433628318584,83.6283185840708,94.56366237482118,56.19047619047619,51.052274270196875,70.3,67.22222222222223,72.78645833333333,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,82.41415192507804,88.95,81.45161290322581,64.58333333333333,100.0,97.36111111111111,80.51282051282051,70.0,96.23809523809524,75.95731324544884,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,84.44676409185804,90.0,71.8676122931442,84.54545454545455,58.47284605961617,92.07920792079207,]
useAllAttributes23NN = [98.44097995545657,63.27433628318584,83.6283185840708,94.56366237482118,56.19047619047619,50.91649694501018,70.3,67.77777777777777,72.52604166666667,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,82.51821019771072,88.95,81.45161290322581,66.89814814814815,100.0,97.36111111111111,80.51282051282051,70.0,96.19047619047619,75.95731324544884,93.33333333333333,99.53095684803002,92.97978700282547,58.94039735099338,84.44676409185804,90.0,71.8676122931442,82.42424242424242,58.71784401796652,92.07920792079207,]
useAllAttributes25NN = [98.44097995545657,63.27433628318584,82.30088495575221,94.56366237482118,57.142857142857146,51.25594025797692,70.2,68.14814814814815,72.65625,100.0,73.6842105263158,83.33333333333333,77.02702702702703,69.03846153846153,82.10197710718002,88.95,81.45161290322581,66.89814814814815,100.0,97.36111111111111,80.51282051282051,70.0,96.19047619047619,75.95731324544884,93.33333333333333,99.53095684803002,92.97978700282547,59.602649006622514,84.13361169102296,90.0,72.22222222222223,82.42424242424242,58.71784401796652,92.07920792079207,]

from matplotlib.pyplot import *
from numpy.lib.function_base import average
import numpy
from matplotlib.mlab import normpdf

def createHistogram(list1,list2=None,_title='',firstTitle='random_reordering',secondTitle='spins_first',thirdTitle='moves_first',fourthTitle='interleve_reordering', fifthTitle = 'no_reordering'):
    n, bins, patches = hist([list1,list2],bins=20,
    color=['crimson', 'orange', ], label=[firstTitle + " (" + str(average(list1))[0:5] + ")", secondTitle + " (" + str(average(list2))[0:5] + ")"])
    legend()
    xlabel('%correct')
    ylabel('number of results')
    title(_title)
    show()

from matplotlib.pyplot import plot,show, legend
def createXYSpreadGraph(list1,list2,name1,name2):
    l1=plot(range(1,len(list1)*2+1,2),list1,'blue',label=name1)
    l2=plot(range(1,len(list2)*2+1,2),list2,'red',label=name2)
    xlabel('k')
    ylabel('%correct (average)')
    legend()
    show()

ignoreAverages = []
ignoreAverages.append(average(ignoreAttributes1NN))
ignoreAverages.append(average(ignoreAttributes3NN))
ignoreAverages.append(average(ignoreAttributes5NN))
ignoreAverages.append(average(ignoreAttributes7NN))
ignoreAverages.append(average(ignoreAttributes9NN))
ignoreAverages.append(average(ignoreAttributes11NN))
ignoreAverages.append(average(ignoreAttributes13NN))
ignoreAverages.append(average(ignoreAttributes15NN))
ignoreAverages.append(average(ignoreAttributes17NN))
ignoreAverages.append(average(ignoreAttributes19NN))
ignoreAverages.append(average(ignoreAttributes21NN))
ignoreAverages.append(average(ignoreAttributes23NN))
ignoreAverages.append(average(ignoreAttributes25NN))
useAllAverages = []
useAllAverages.append(average(useAllAttributes1NN))
useAllAverages.append(average(useAllAttributes3NN))
useAllAverages.append(average(useAllAttributes5NN))
useAllAverages.append(average(useAllAttributes7NN))
useAllAverages.append(average(useAllAttributes9NN))
useAllAverages.append(average(useAllAttributes11NN))
useAllAverages.append(average(useAllAttributes13NN))
useAllAverages.append(average(useAllAttributes15NN))
useAllAverages.append(average(useAllAttributes17NN))
useAllAverages.append(average(useAllAttributes19NN))
useAllAverages.append(average(useAllAttributes21NN))
useAllAverages.append(average(useAllAttributes23NN))
useAllAverages.append(average(useAllAttributes25NN))



print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes3NN, useAllAttributes3NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes5NN, useAllAttributes5NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes7NN, useAllAttributes7NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes9NN, useAllAttributes9NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes11NN, useAllAttributes11NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes13NN, useAllAttributes13NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes15NN, useAllAttributes15NN)
print 'wilcoxon test for use all attributes or ignore all:', wilcoxon(ignoreAttributes17NN, useAllAttributes17NN)

print 'shapiro normality test', shapiro(ignoreAttributes7NN)

createXYSpreadGraph(ignoreAverages, useAllAverages, 'ignore used attributes in KNN', 'use all attributes in KNN')


#the histogram is not very educational... results are not distributed normally. 
createHistogram(ignoreAttributes7NN, useAllAttributes7NN, 'histogram: % correcteness ','7NN ignore attributes used by tree', '7NN use all attributes')
