N = 175;
P = 1:1:175;

f1 = fopen('v2.25-1-1541093770632/evacuationTimes.txt', 'r');
t1 = eval(fgets(f1));
fclose(f1);

f2 = fopen('v2.25-2-1541094683357/evacuationTimes.txt', 'r');
t2 = eval(fgets(f2));
fclose(f2);

f3 = fopen('v2.25-3-1541097450298/evacuationTimes.txt', 'r');
t3 = eval(fgets(f3));
fclose(f3);

f4 = fopen('v2.25-4-1541097822196/evacuationTimes.txt', 'r');
t4 = eval(fgets(f4));
fclose(f4);

f5 = fopen('v2.25-5-1541098152113/evacuationTimes.txt', 'r');
t5 = eval(fgets(f5));
fclose(f5);

## GRAFICO PUNTO A
hold on;

plot(t1, P, '-o');
plot(t2, P, '-o');
plot(t3, P, '-o');
plot(t4, P, '-o');
plot(t5, P, '-o');

xlabel('Tiempo transcurrido (s)');
ylabel('Peatones evacuados');

l = legend('s1 = 1541093770632 ms', 's2 = 1541094683357 ms', 's3 = 1541097450298 ms', 's4 = 1541097822196 ms', 's5 = 1541098152113 ms'); 
set(l, "fontsize", 15, "location", "southeast");
set(gca, "fontsize", 15);

print -dpng "pointAGraph.png";

hold off;
##

## GRAFICOS INDIVIDUALES
newplot();

plot(t1, P, '-o');
title('s1 = 1541093770632 ms');
xlabel('Tiempo transcurrido (s)');
ylabel('Peatones evacuados');
set(gca, "fontsize", 15);
print -dpng "pointAGraph1.png";

plot(t2, P, '-o');
title('s2 = 1541094683357 ms');
xlabel('Tiempo transcurrido (s)');
ylabel('Peatones evacuados');
set(gca, "fontsize", 15);
print -dpng "pointAGraph2.png";

plot(t3, P, '-o');
title('s3 = 1541097450298 ms');
xlabel('Tiempo transcurrido (s)');
ylabel('Peatones evacuados');
set(gca, "fontsize", 15);
print -dpng "pointAGraph3.png";

plot(t4, P, '-o');
title('s4 = 1541097822196 ms');
xlabel('Tiempo transcurrido (s)');
ylabel('Peatones evacuados');
set(gca, "fontsize", 15);
print -dpng "pointAGraph4.png";

plot(t5, P, '-o');
title('s5 = 1541098152113 ms');
xlabel('Tiempo transcurrido (s)');
ylabel('Peatones evacuados');
set(gca, "fontsize", 15);
print -dpng "pointAGraph5.png";


##


## GRAFICO PUNTO B
newplot();
hold on;

MEAN = [];
STD = [];

for i = 1:N
  arr = [t1(i) t2(i) t3(i) t4(i) t5(i)];
  MEAN(i) = mean(arr);
  STD(i) = std(arr);
endfor

errorbar(P, MEAN, STD, '-o');
xlabel('Peatones evacuados');
ylabel('Tiempo transcurrido (s)');
set(gca, "fontsize", 15);
print -dpng "pointBGraph.png";

hold off;
##

