Q = graphQ('../flow0.2.txt',200,50,0.002);
q1 = mean(Q(13:end))
std1 = std(Q(13:end))* 4
Q = graphQ('../flow0.25.txt',200,50,0.002);
q2 = mean(Q(13:end))
std2 = std(Q(13:end))* 4
Q = graphQ('../flow0.3.txt',200,50,0.002);
q3 = mean(Q(13:end))
std3 = std(Q(13:end)) * 4
Q = graphQ('../flow0.35.txt',200,50,0.002);
q4 = mean(Q(13:end))
std4 = std(Q(13:end)) * 4
A = 1.24019830697443 * 2;
c = plotError(1000 / A,0.2,0.25,0.3,0.35,q1,q2,q3,q4)
plotBeverloo(1000 / A,c,0.2,0.25,0.3,0.35,q1,std1,q2,std2,q3,std3,q4,std4)