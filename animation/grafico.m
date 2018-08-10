t10BrutePer = [6,5,5,6,5,4,8,9,7,5];
t10CellPer = [15,21,14,21,15,12,18,19,19,14];
t100BrutePer = [9,10,10,13,12,10,9,11,10,12];
t100CellPer = [16,15,16,18,15,16,16,16,16,16];
t1000BrutePer = [71,84,80,81,86,97,94,95,90,81];
t1000CellPer = [27,43,34,32,32,27,27,30,33,30];

t10BruteBox = [5,7,5,4,6,5,6,4,5,6];
t10CellBox = [17,21,20,14,14,16,20,19,21,20];
t100BruteBox = [9,7,9,9,8,15,10,8,7,9];
t100CellBox = [14,18,20,17,16,20,21,17,18,15];
t1000BruteBox = [65,89,80,74,73,69,95,65,85,60];
t1000CellBox = [30,28,30,32,24,28,26,28,34,28];

meanBrutePer = [mean(t10BrutePer),mean(t100BrutePer),mean(t1000BrutePer)];
meanCellPer = [mean(t10CellPer),mean(t100CellPer),mean(t1000CellPer)];
meanBruteBox = [mean(t10BruteBox),mean(t100BruteBox),mean(t1000BruteBox)];
meanCellBox = [mean(t10CellBox),mean(t100CellBox),mean(t1000CellBox)];

stdBrutePer = [std(t10BrutePer),std(t100BrutePer),std(t1000BrutePer)];
stdCellPer = [std(t10CellPer),std(t100CellPer),std(t1000CellPer)];
stdBruteBox = [std(t10BruteBox),std(t100BruteBox),std(t1000BruteBox)];
stdCellBox = [std(t10CellBox),std(t100CellBox),std(t1000CellBox)];


#errorbar([10,100,1000],meanBruteBox,stdBruteBox);
#hold on;
#errorbar([10,100,1000],meanCellBox,stdCellBox,"~r");
#xlabel('N');
#ylabel('Tiempo(ms)');
#title('Comparacion sin condiciones periodicas de contorno');
#legend ("Fuerza Bruta", "Cell Index Method");

errorbar([10,100,1000],meanBrutePer,stdBrutePer);
hold on;
errorbar([10,100,1000],meanCellPer,stdCellPer,"~r");
xlabel('N');
ylabel('Tiempo(ms)');
title('Comparacion con condiciones periodicas de contorno');
legend ("Fuerza Bruta", "Cell Index Method");