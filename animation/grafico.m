t10BrutePer = [6,5,5,6,5,4,8,9,7,5];
t10CellPer = [15,21,14,21,15,12,18,19,19,14];
t100BrutePer = [9,10,10,13,12,10,9,11,10,12];
t100CellPer = [16,15,16,18,15,16,16,16,16,16];
t1000BrutePer = [71,84,80,81,86,97,94,95,90,81];
t1000CellPer = [27,43,34,32,32,27,27,30,33,30];
t10000BrutePer = [3330,3163,3366,3154,3166,3102,2993,3197,3260,3289];
t10000CellPer = [793,590,644,611,566,615,584,582,591,691];

t10BruteBox = [5,7,5,4,6,5,6,4,5,6];
t10CellBox = [17,21,20,14,14,16,20,19,21,20];
t100BruteBox = [9,7,9,9,8,15,10,8,7,9];
t100CellBox = [14,18,20,17,16,20,21,17,18,15];
t1000BruteBox = [65,89,80,74,73,69,95,65,85,60];
t1000CellBox = [30,28,30,32,24,28,26,28,34,28];
t10000BruteBox = [2178,2303,2550,2214,2960,2344,2732,2158,2499,2398];
t10000CellBox = [600,618,629,565,758,560,584,572,575,578];

meanBrutePer = [mean(t100BrutePer),mean(t1000BrutePer),mean(t10000BrutePer)];
meanCellPer = [mean(t100CellPer),mean(t1000CellPer),mean(t10000CellPer)];
meanBruteBox = [mean(t100BruteBox),mean(t1000BruteBox),mean(t10000BruteBox)];
meanCellBox = [mean(t100CellBox),mean(t1000CellBox),mean(t10000CellBox)];


stdBrutePer = [std(t100BrutePer),std(t1000BrutePer),std(t10000BrutePer)];
stdCellPer = [std(t100CellPer),std(t1000CellPer),std(t10000CellPer)];
stdBruteBox = [std(t100BruteBox),std(t1000BruteBox),std(t10000BruteBox)];
stdCellBox = [std(t100CellBox),std(t1000CellBox),std(t10000CellBox)];

#stdBrutePer = [std(log10(t100BrutePer)),std(log10(t1000BrutePer)),std(log10(t10000BrutePer))];
#stdCellPer = [std(log10(t100CellPer)),std(log10(t1000CellPer)),std(log10(t10000CellPer))];
#stdBruteBox = [std(log10(t100BruteBox)),std(log10(t1000BruteBox)),std(log10(t10000BruteBox))];
#stdCellBox = [std(log10(t100CellBox)),std(log10(t1000CellBox)),std(log10(t10000CellBox))];

clf;
errorbar([100,1000,10000],meanBruteBox,stdBruteBox,"-*");
hold on;
errorbar([100,1000,10000],meanCellBox,stdCellBox,"~r-*");
xlabel('N');
ylabel('Tiempo(ms)');
title('Sin condiciones de contorno');
legend({"Fuerza Bruta","Cell Index Method"}, "location", "northeastoutside");


#clf;
#errorbar([100,1000,10000],log10(meanBrutePer),stdBrutePer,"-*");
#hold on;
#errorbar([100,1000,10000],log10(meanCellPer),stdCellPer,"~r-*");
#xlabel('N');
#ylabel('log10(Tiempo)(ms)');
#title('Con condiciones de contorno');
#legend({"Fuerza Bruta","Cell Index Method"}, "location", "northeastoutside");



