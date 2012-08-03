from scipy.stats.morestats import wilcoxon

info_gain_naive_bayes = [0.6678571428571428, 0.7008928571428571, 0.7107142857142857, 0.7227678571428572, 0.7348214285714286, 0.7504464285714286, 0.746875, 0.7477678571428571, 0.74375, 0.7419642857142857, 0.7424107142857143, 0.74375, 0.7433035714285714, 0.7401785714285715, 0.7410714285714286, 0.7428571428571429, 0.7410714285714286, 0.7361607142857143, 0.7370535714285714, 0.7361607142857143, 0.7352678571428571, 0.7334821428571429, 0.7361607142857143, 0.7325892857142857, 0.7303571428571428, 0.7285714285714285, 0.7272321428571429, 0.7290178571428572, 0.7276785714285714, 0.7258928571428571, 0.7276785714285714, 0.721875, 0.7232142857142857, 0.7245535714285715, 0.7209821428571429, 0.71875, 0.7147321428571428, 0.7183035714285714, 0.7165178571428571, 0.7147321428571428, 0.7174107142857142, 0.7111607142857143, 0.70625, 0.7022321428571429, 0.7013392857142857, 0.7, 0.7026785714285714, 0.6973214285714285, 0.6928571428571428, 0.69375, 0.6892857142857143, 0.6910714285714286, 0.690625, 0.6901785714285714, 0.6919642857142857, 0.6924107142857143, 0.6941964285714286, 0.6915178571428572, 0.690625, 0.6901785714285714, 0.6888392857142858, 0.6901785714285714, 0.6910714285714286, 0.6888392857142858, 0.6879464285714286, 0.6883928571428571, 0.6875, 0.6875, 0.6883928571428571, 0.6870535714285714, 0.6866071428571429, 0.6857142857142857, 0.6861607142857142, 0.6852678571428571, 0.6848214285714286, 0.6852678571428571, 0.6883928571428571, 0.6870535714285714, 0.6866071428571429, 0.6861607142857142, 0.6875, 0.6883928571428571, 0.6870535714285714, 0.6848214285714286, 0.6848214285714286, 0.6861607142857142, 0.6857142857142857, 0.6857142857142857, 0.6857142857142857, 0.6870535714285714, 0.6879464285714286, 0.6852678571428571, 0.6834821428571428, 0.6834821428571428, 0.6816964285714285, 0.6834821428571428, 0.6834821428571428, 0.6830357142857143, 0.678125, 0.6803571428571429, ]
info_gain_linear_svm = [0.6544642857142857, 0.675, 0.6955357142857143, 0.7066964285714286, 0.7040178571428571, 0.7, 0.6977678571428572, 0.6986607142857143, 0.6995535714285714, 0.6955357142857143, 0.6803571428571429, 0.6928571428571428, 0.6839285714285714, 0.6803571428571429, 0.6928571428571428, 0.6803571428571429, 0.6982142857142857, 0.6950892857142857, 0.6955357142857143, 0.6959821428571429, 0.6964285714285714, 0.6941964285714286, 0.6808035714285714, 0.6758928571428572, 0.6741071428571429, 0.6879464285714286, 0.6785714285714286, 0.6870535714285714, 0.6946428571428571, 0.6763392857142857, 0.690625, 0.6808035714285714, 0.6790178571428571, 0.6794642857142857, 0.6799107142857143, 0.6776785714285715, 0.6825892857142857, 0.6763392857142857, 0.6633928571428571, 0.665625, 0.6660714285714285, 0.6571428571428571, 0.6732142857142858, 0.6616071428571428, 0.6589285714285714, 0.6535714285714286, 0.6517857142857143, 0.6678571428571428, 0.6674107142857143, 0.6540178571428571, 0.6571428571428571, 0.65625, 0.665625, 0.6620535714285715, 0.65625, 0.6491071428571429, 0.6575892857142858, 0.6575892857142858, 0.6620535714285715, 0.6607142857142857, 0.6508928571428572, 0.6611607142857143, 0.6433035714285714, 0.6459821428571428, 0.6584821428571429, 0.659375, 0.6598214285714286, 0.6647321428571429, 0.6477678571428571, 0.6665178571428572, 0.6508928571428572, 0.66875, 0.6580357142857143, 0.6544642857142857, 0.6709821428571429, 0.6566964285714286, 0.6709821428571429, 0.6611607142857143, 0.6638392857142857, 0.6660714285714285, 0.6651785714285714, 0.665625, 0.6633928571428571, 0.6616071428571428, 0.66875, 0.6571428571428571, 0.6678571428571428, 0.6691964285714286, 0.6625, 0.6607142857142857, 0.6683035714285714, 0.6629464285714286, 0.6571428571428571, 0.6607142857142857, 0.6508928571428572, 0.6638392857142857, 0.665625, 0.6598214285714286, 0.6611607142857143, 0.659375, ]
info_gain_hyperbolic_svm = [0.6611607142857143, 0.6866071428571429, 0.69375, 0.70625, 0.6964285714285714, 0.6964285714285714, 0.6946428571428571, 0.6941964285714286, 0.6950892857142857, 0.6955357142857143, 0.6763392857142857, 0.6964285714285714, 0.6848214285714286, 0.6803571428571429, 0.690625, 0.678125, 0.6839285714285714, 0.6888392857142858, 0.6861607142857142, 0.6928571428571428, 0.6830357142857143, 0.6816964285714285, 0.6991071428571428, 0.6758928571428572, 0.6803571428571429, 0.6888392857142858, 0.6763392857142857, 0.6861607142857142, 0.690625, 0.6852678571428571, 0.6888392857142858, 0.6919642857142857, 0.6767857142857143, 0.6776785714285715, 0.678125, 0.68125, 0.6745535714285714, 0.6772321428571428, 0.6709821428571429, 0.6741071428571429, 0.6611607142857143, 0.6642857142857143, 0.6669642857142857, 0.6647321428571429, 0.6651785714285714, 0.665625, 0.6642857142857143, 0.6723214285714286, 0.671875, 0.671875, 0.6620535714285715, 0.665625, 0.6683035714285714, 0.66875, 0.6696428571428571, 0.6705357142857142, 0.6683035714285714, 0.6589285714285714, 0.6683035714285714, 0.6633928571428571, 0.6647321428571429, 0.6629464285714286, 0.6616071428571428, 0.6589285714285714, 0.6714285714285714, 0.6607142857142857, 0.671875, 0.665625, 0.6549107142857142, 0.6602678571428572, 0.6558035714285714, 0.6678571428571428, 0.665625, 0.6674107142857143, 0.6575892857142858, 0.6700892857142857, 0.6700892857142857, 0.66875, 0.6638392857142857, 0.6691964285714286, 0.6625, 0.6700892857142857, 0.6705357142857142, 0.6651785714285714, 0.6696428571428571, 0.6683035714285714, 0.6745535714285714, 0.6700892857142857, 0.6674107142857143, 0.6625, 0.6625, 0.6584821428571429, 0.6571428571428571, 0.6642857142857143, 0.6598214285714286, 0.6723214285714286, 0.6629464285714286, 0.6758928571428572, 0.665625, 0.6602678571428572, ]

symm_naive_bayes = [0.6620535714285715, 0.6977678571428572, 0.7120535714285714, 0.7232142857142857, 0.7339285714285714, 0.7495535714285714, 0.75, 0.7464285714285714, 0.7428571428571429, 0.7419642857142857, 0.7424107142857143, 0.74375, 0.7433035714285714, 0.7401785714285715, 0.7410714285714286, 0.7428571428571429, 0.7410714285714286, 0.7361607142857143, 0.7370535714285714, 0.7361607142857143, 0.7352678571428571, 0.7334821428571429, 0.7361607142857143, 0.7325892857142857, 0.7303571428571428, 0.7285714285714285, 0.7272321428571429, 0.7290178571428572, 0.7276785714285714, 0.7258928571428571, 0.7276785714285714, 0.721875, 0.7232142857142857, 0.7245535714285715, 0.7209821428571429, 0.71875, 0.7147321428571428, 0.7183035714285714, 0.7165178571428571, 0.7147321428571428, 0.7174107142857142, 0.7111607142857143, 0.70625, 0.7022321428571429, 0.7013392857142857, 0.7, 0.7026785714285714, 0.6973214285714285, 0.6928571428571428, 0.69375, 0.6892857142857143, 0.6910714285714286, 0.690625, 0.6901785714285714, 0.6919642857142857, 0.6924107142857143, 0.6941964285714286, 0.6915178571428572, 0.690625, 0.6901785714285714, 0.6888392857142858, 0.6901785714285714, 0.6910714285714286, 0.6888392857142858, 0.6879464285714286, 0.6883928571428571, 0.6875, 0.6875, 0.6883928571428571, 0.6870535714285714, 0.6866071428571429, 0.6857142857142857, 0.6861607142857142, 0.6852678571428571, 0.6848214285714286, 0.6852678571428571, 0.6883928571428571, 0.6870535714285714, 0.6866071428571429, 0.6861607142857142, 0.6875, 0.6883928571428571, 0.6870535714285714, 0.6848214285714286, 0.6848214285714286, 0.6861607142857142, 0.6857142857142857, 0.6857142857142857, 0.6857142857142857, 0.6870535714285714, 0.6879464285714286, 0.6852678571428571, 0.6834821428571428, 0.6834821428571428, 0.6816964285714285, 0.6834821428571428, 0.6834821428571428, 0.6830357142857143, 0.678125, 0.6803571428571429, ]
symm_linear_svm = [0.6638392857142857, 0.6808035714285714, 0.6790178571428571, 0.6870535714285714, 0.6986607142857143, 0.7004464285714286, 0.70625, 0.6946428571428571, 0.7084821428571428, 0.6950892857142857, 0.7196428571428571, 0.7049107142857143, 0.7049107142857143, 0.7058035714285714, 0.7044642857142858, 0.7080357142857143, 0.7080357142857143, 0.7116071428571429, 0.7058035714285714, 0.6973214285714285, 0.7089285714285715, 0.70625, 0.7071428571428572, 0.6964285714285714, 0.7075892857142857, 0.7066964285714286, 0.7026785714285714, 0.7071428571428572, 0.7138392857142857, 0.7089285714285715, 0.7084821428571428, 0.7075892857142857, 0.7053571428571429, 0.7004464285714286, 0.7, 0.7013392857142857, 0.6941964285714286, 0.6919642857142857, 0.7013392857142857, 0.6986607142857143, 0.6897321428571429, 0.68125, 0.6928571428571428, 0.6745535714285714, 0.6830357142857143, 0.6941964285714286, 0.7, 0.7004464285714286, 0.6910714285714286, 0.6901785714285714, 0.6767857142857143, 0.6973214285714285, 0.6857142857142857, 0.6821428571428572, 0.6875, 0.6933035714285715, 0.6928571428571428, 0.6946428571428571, 0.6866071428571429, 0.6857142857142857, 0.6901785714285714, 0.6790178571428571, 0.6825892857142857, 0.6883928571428571, 0.6866071428571429, 0.6857142857142857, 0.6977678571428572, 0.6866071428571429, 0.6910714285714286, 0.6928571428571428, 0.6910714285714286, 0.6883928571428571, 0.6950892857142857, 0.690625, 0.6973214285714285, 0.6941964285714286, 0.6973214285714285, 0.6991071428571428, 0.696875, 0.6973214285714285, 0.6959821428571429, 0.6982142857142857, 0.6955357142857143, 0.69375, 0.6959821428571429, 0.6964285714285714, 0.6964285714285714, 0.6946428571428571, 0.6950892857142857, 0.6973214285714285, 0.6946428571428571, 0.6973214285714285, 0.6924107142857143, 0.6910714285714286, 0.6915178571428572, 0.6897321428571429, 0.6892857142857143, 0.6901785714285714, 0.6901785714285714, 0.6834821428571428, ]
symm_hyperbolic_svm = [0.6709821428571429, 0.6754464285714286, 0.6883928571428571, 0.7044642857142858, 0.7133928571428572, 0.7236607142857143, 0.7245535714285715, 0.7223214285714286, 0.71875, 0.7174107142857142, 0.7241071428571428, 0.7205357142857143, 0.7241071428571428, 0.7205357142857143, 0.7191964285714286, 0.7205357142857143, 0.7232142857142857, 0.721875, 0.7232142857142857, 0.7232142857142857, 0.7214285714285714, 0.7241071428571428, 0.7232142857142857, 0.7191964285714286, 0.7223214285714286, 0.7196428571428571, 0.721875, 0.7227678571428572, 0.7214285714285714, 0.7223214285714286, 0.7209821428571429, 0.7241071428571428, 0.7169642857142857, 0.7178571428571429, 0.7133928571428572, 0.7120535714285714, 0.7008928571428571, 0.7080357142857143, 0.7098214285714286, 0.6959821428571429, 0.7058035714285714, 0.7049107142857143, 0.6866071428571429, 0.6986607142857143, 0.7, 0.7, 0.6982142857142857, 0.7, 0.6982142857142857, 0.6991071428571428, 0.6995535714285714, 0.6995535714285714, 0.703125, 0.7008928571428571, 0.703125, 0.7013392857142857, 0.6959821428571429, 0.6924107142857143, 0.6955357142857143, 0.7004464285714286, 0.6946428571428571, 0.7017857142857142, 0.6964285714285714, 0.7004464285714286, 0.7004464285714286, 0.6973214285714285, 0.6964285714285714, 0.6959821428571429, 0.6995535714285714, 0.6959821428571429, 0.6941964285714286, 0.6964285714285714, 0.6982142857142857, 0.6941964285714286, 0.6933035714285715, 0.6955357142857143, 0.6995535714285714, 0.6973214285714285, 0.69375, 0.7004464285714286, 0.6973214285714285, 0.6955357142857143, 0.6964285714285714, 0.6982142857142857, 0.7013392857142857, 0.6977678571428572, 0.7, 0.69375, 0.696875, 0.6982142857142857, 0.6986607142857143, 0.7053571428571429, 0.696875, 0.6977678571428572, 0.6964285714285714, 0.6933035714285715, 0.7004464285714286, 0.6986607142857143, 0.6915178571428572, 0.6977678571428572, ]

ReliefF_naive_bayse  = [0.5808035714285714, 0.6044642857142857, 0.6165178571428571, 0.6290178571428572, 0.6316964285714286, 0.640625, 0.6450892857142857, 0.6486607142857143, 0.6455357142857143, 0.6473214285714286, 0.6491071428571429, 0.65, 0.6517857142857143, 0.6526785714285714, 0.6535714285714286, 0.6513392857142857, 0.6553571428571429, 0.6549107142857142, 0.6553571428571429, 0.653125, 0.653125, 0.6566964285714286, 0.6535714285714286, 0.6508928571428572, 0.6526785714285714, 0.6526785714285714, 0.6508928571428572, 0.6540178571428571, 0.6544642857142857, 0.6513392857142857, 0.6482142857142857, 0.65, 0.6482142857142857, 0.6482142857142857, 0.6482142857142857, 0.6517857142857143, 0.6607142857142857, 0.6517857142857143, 0.6477678571428571, 0.6464285714285715, 0.6428571428571429, 0.6428571428571429, 0.6433035714285714, 0.6424107142857143, 0.6441964285714286, 0.6446428571428572, 0.6450892857142857, 0.6441964285714286, 0.6459821428571428, 0.6464285714285715, 0.6477678571428571, 0.6455357142857143, 0.6477678571428571, 0.6482142857142857, 0.6473214285714286, 0.6473214285714286, 0.6491071428571429, 0.6495535714285714, 0.65, 0.6544642857142857, 0.653125, 0.6589285714285714, 0.6575892857142858, 0.6558035714285714, 0.6549107142857142, 0.6553571428571429, 0.6553571428571429, 0.65625, 0.65625, 0.6553571428571429, 0.6571428571428571, 0.6553571428571429, 0.6517857142857143, 0.6540178571428571, 0.6504464285714285, 0.6504464285714285, 0.6495535714285714, 0.6495535714285714, 0.65, 0.6491071428571429, 0.6504464285714285, 0.6495535714285714, 0.6513392857142857, 0.6491071428571429, 0.6495535714285714, 0.6495535714285714, 0.6495535714285714, 0.6491071428571429, 0.6558035714285714, 0.65625, 0.6558035714285714, 0.6535714285714286, 0.6549107142857142, 0.6611607142857143, 0.6709821428571429, 0.6705357142857142, 0.6709821428571429, 0.6776785714285715, 0.678125, 0.6821428571428572, ]
ReliefF_linear_svm  = [0.5959821428571429, 0.5964285714285714, 0.6107142857142858, 0.6165178571428571, 0.6174107142857143, 0.6366071428571428, 0.6508928571428572, 0.6508928571428572, 0.6513392857142857, 0.6513392857142857, 0.6473214285714286, 0.6428571428571429, 0.6428571428571429, 0.6464285714285715, 0.6508928571428572, 0.6424107142857143, 0.653125, 0.6477678571428571, 0.6441964285714286, 0.6526785714285714, 0.6504464285714285, 0.6535714285714286, 0.6669642857142857, 0.6513392857142857, 0.6513392857142857, 0.6517857142857143, 0.6517857142857143, 0.6517857142857143, 0.6540178571428571, 0.6540178571428571, 0.653125, 0.6647321428571429, 0.6589285714285714, 0.6575892857142858, 0.6598214285714286, 0.6607142857142857, 0.6598214285714286, 0.6575892857142858, 0.6571428571428571, 0.6580357142857143, 0.65625, 0.6611607142857143, 0.6665178571428572, 0.6638392857142857, 0.6683035714285714, 0.6633928571428571, 0.66875, 0.6678571428571428, 0.6633928571428571, 0.6616071428571428, 0.6620535714285715, 0.6589285714285714, 0.6625, 0.6558035714285714, 0.6598214285714286, 0.6669642857142857, 0.6669642857142857, 0.6660714285714285, 0.6678571428571428, 0.6705357142857142, 0.6790178571428571, 0.68125, 0.68125, 0.6830357142857143, 0.6794642857142857, 0.6830357142857143, 0.6790178571428571, 0.684375, 0.6799107142857143, 0.6888392857142858, 0.6834821428571428, 0.6821428571428572, 0.6803571428571429, 0.6875, 0.6839285714285714, 0.6825892857142857, 0.6861607142857142, 0.6852678571428571, 0.6861607142857142, 0.6794642857142857, 0.6763392857142857, 0.6758928571428572, 0.6785714285714286, 0.6794642857142857, 0.6866071428571429, 0.6848214285714286, 0.6879464285714286, 0.6879464285714286, 0.6848214285714286, 0.6892857142857143, 0.68125, 0.6866071428571429, 0.6852678571428571, 0.6861607142857142, 0.6857142857142857, 0.6839285714285714, 0.6821428571428572, 0.6861607142857142, 0.684375, 0.6959821428571429, ]
ReliefF_hyperbolic_svm =  [0.5933035714285714, 0.6151785714285715, 0.6245535714285714, 0.6370535714285714, 0.6428571428571429, 0.6522321428571428, 0.6575892857142858, 0.6544642857142857, 0.6589285714285714, 0.6571428571428571, 0.6571428571428571, 0.6602678571428572, 0.6629464285714286, 0.6607142857142857, 0.6633928571428571, 0.6616071428571428, 0.6629464285714286, 0.6633928571428571, 0.6678571428571428, 0.6709821428571429, 0.6714285714285714, 0.671875, 0.6625, 0.6767857142857143, 0.675, 0.675, 0.6763392857142857, 0.6776785714285715, 0.6696428571428571, 0.6772321428571428, 0.6785714285714286, 0.6758928571428572, 0.6772321428571428, 0.675, 0.665625, 0.671875, 0.6821428571428572, 0.6763392857142857, 0.6816964285714285, 0.6745535714285714, 0.6696428571428571, 0.6772321428571428, 0.6799107142857143, 0.6816964285714285, 0.6741071428571429, 0.6732142857142858, 0.6808035714285714, 0.6767857142857143, 0.6834821428571428, 0.6705357142857142, 0.6745535714285714, 0.6696428571428571, 0.6790178571428571, 0.675, 0.6763392857142857, 0.6799107142857143, 0.6785714285714286, 0.6803571428571429, 0.68125, 0.6830357142857143, 0.6870535714285714, 0.6825892857142857, 0.6866071428571429, 0.6870535714285714, 0.6857142857142857, 0.6901785714285714, 0.6830357142857143, 0.6888392857142858, 0.6875, 0.6839285714285714, 0.690625, 0.6901785714285714, 0.6852678571428571, 0.6883928571428571, 0.6848214285714286, 0.6870535714285714, 0.6883928571428571, 0.6857142857142857, 0.6825892857142857, 0.684375, 0.6866071428571429, 0.6825892857142857, 0.684375, 0.6852678571428571, 0.6830357142857143, 0.6834821428571428, 0.6861607142857142, 0.6870535714285714, 0.6897321428571429, 0.6897321428571429, 0.6830357142857143, 0.6825892857142857, 0.6857142857142857, 0.6950892857142857, 0.7, 0.6991071428571428, 0.6924107142857143, 0.6982142857142857, 0.7004464285714286, 0.7008928571428571, ]


list1 = info_gain_naive_bayes + info_gain_linear_svm +  info_gain_hyperbolic_svm
list2 = symm_naive_bayes + symm_linear_svm + symm_hyperbolic_svm
list3 = ReliefF_naive_bayse + ReliefF_linear_svm + ReliefF_hyperbolic_svm
print 'info gain VS symm'
print wilcoxon(list1,list2)

print 'info gain VS relieff'
print wilcoxon(list1,list3)

print 'symm VS relieff'
print wilcoxon(list2,list3)