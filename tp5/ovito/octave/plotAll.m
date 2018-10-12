Q = graphQ('../flow0.2.txt',18,1,0.033);
q1 = mean(Q(49:end));
std1 = std(Q(49:end));
Q = graphQ('../flow0.25.txt',18,1,0.033);
q2 = mean(Q(49:end));
std2 = std(Q(49:end));
Q = graphQ('../flow0.35.txt',18,1,0.033);
q3 = mean(Q(49:end));
std3 = std(Q(49:end));
Q = graphQ('../flow0.5.txt',18,1,0.033);
q4 = mean(Q(49:end));
std4 = std(Q(49:end));
c = plotError(500,0.2,0.25,0.35,0.5,q1,q2,q3,q4);
plotBeverloo(500,4.97,0.2,0.25,0.35,0.5,q1,std1,q2,std2,q3,std3,q4,std4)