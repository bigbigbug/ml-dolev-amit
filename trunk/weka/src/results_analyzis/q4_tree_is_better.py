from scipy.stats.morestats import wilcoxon
from numpy.lib.function_base import average, median

#tree = [96.19047619047619, 96.28571428571429, 95.61904761904762, 96.0, 96.57142857142857, 96.57142857142857, 95.14285714285714, 96.0, 96.19047619047619, 95.9047619047619, 96.28571428571429, 95.61904761904762, 97.33333333333333, 94.85714285714286, 95.14285714285714, 94.28571428571429, 94.19047619047619, 96.38095238095238, 95.04761904761905, 94.85714285714286, 96.19047619047619, 96.38095238095238, 96.38095238095238, 96.47619047619048, 96.19047619047619, 94.57142857142857, 96.38095238095238, 96.47619047619048, 96.47619047619048, 94.95238095238095, 96.19047619047619, 95.14285714285714, 95.71428571428571, 94.85714285714286, 95.9047619047619, 96.66666666666667, 94.95238095238095, 94.57142857142857, 94.19047619047619, 94.19047619047619, 95.9047619047619, 95.9047619047619, 94.66666666666667, 96.0952380952381, 96.0, 95.9047619047619, 97.14285714285714, 96.19047619047619, 96.28571428571429, 96.19047619047619]
#C457NN = [96.0, 94.95238095238095, 94.28571428571429, 95.61904761904762, 95.23809523809524, 96.38095238095238, 95.23809523809524, 96.0, 95.04761904761905, 96.0952380952381, 96.19047619047619, 94.57142857142857, 96.47619047619048, 96.38095238095238, 95.42857142857143, 94.85714285714286, 94.57142857142857, 96.19047619047619, 94.76190476190476, 94.47619047619048, 94.95238095238095, 95.61904761904762, 96.19047619047619, 96.0, 95.04761904761905, 95.33333333333333, 96.28571428571429, 95.52380952380952, 96.47619047619048, 95.61904761904762, 95.80952380952381, 95.14285714285714, 96.0952380952381, 95.04761904761905, 95.33333333333333, 96.85714285714286, 95.23809523809524, 96.0952380952381, 93.52380952380952, 95.52380952380952, 95.71428571428571, 96.0, 94.57142857142857, 96.66666666666667, 96.0, 95.61904761904762, 96.38095238095238, 95.61904761904762, 96.0, 95.61904761904762]


tree = [96.18604651162791, 96.18604651162791, 96.37209302325581, 96.46511627906976, 96.74418604651163, 96.09302325581395, 95.90697674418605, 96.18604651162791, 96.27906976744185, 96.27906976744185, 96.09302325581395, 96.18604651162791, 96.65116279069767, 96.18604651162791, 95.81395348837209, 96.0, 96.55813953488372, 96.18604651162791, 95.72093023255815, 95.72093023255815, 96.0, 95.90697674418605, 96.09302325581395, 96.0, 96.46511627906976, 96.18604651162791, 96.09302325581395, 95.90697674418605, 95.81395348837209, 96.55813953488372, 96.0, 96.46511627906976, 96.0, 96.09302325581395, 96.18604651162791, 95.72093023255815, 96.27906976744185, 95.62790697674419, 94.79069767441861, 95.81395348837209, 96.09302325581395, 96.18604651162791, 96.37209302325581, 96.37209302325581, 96.27906976744185, 96.09302325581395, 96.46511627906976, 96.74418604651163, 96.0, 96.18604651162791]

C457NN = [94.13953488372093, 95.53488372093024, 96.46511627906976, 95.44186046511628, 95.72093023255815, 95.53488372093024, 95.06976744186046, 96.0, 94.88372093023256, 95.81395348837209, 94.97674418604652, 94.69767441860465, 96.37209302325581, 95.25581395348837, 94.79069767441861, 95.53488372093024, 96.46511627906976, 96.0, 95.90697674418605, 95.62790697674419, 95.81395348837209, 94.32558139534883, 95.16279069767442, 94.4186046511628, 94.97674418604652, 96.0, 95.81395348837209, 95.34883720930233, 95.72093023255815, 95.90697674418605, 95.53488372093024, 95.72093023255815, 95.25581395348837, 95.62790697674419, 96.55813953488372, 96.37209302325581, 96.09302325581395, 94.51162790697674, 95.16279069767442, 94.79069767441861, 95.25581395348837, 94.69767441860465, 96.46511627906976, 95.44186046511628, 95.81395348837209, 96.55813953488372, 95.25581395348837, 96.46511627906976, 94.97674418604652, 94.97674418604652]


print 'average tree = ',average(tree)
print 'average C4.5(7NN) = ', average(C457NN)
print 'median tree = ',median(tree)
print 'median C4.5(7NN) = ', median(C457NN)


print 'wilcoxon test for J48 or C4.5(7NN):', wilcoxon(tree, C457NN)

