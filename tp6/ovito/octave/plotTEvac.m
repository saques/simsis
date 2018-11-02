function plotTEvac()
 
  v1 = [81.50199999986341000;86.99359999985062000;86.96879999985067000;85.76439999985348000;88.60519999984686000];
  v2 = [51.38359999993360000;52.48319999993104000;47.24799999994324000;50.79999999993496000;54.63559999992602000];
  v3 = [55.60279999992377000;49.20079999993869000;54.90439999992539600;54.01759999992746000;50.58239999993547000];
  v4 = [52.10759999993191400;51.81079999993260500;52.26879999993154000;50.94159999993463000;50.89319999993474400];
  v5 = [55.02919999992510500;56.73599999992113000;50.18199999993640000;50.40799999993587500;52.44079999993114000];
  
  mean1 = mean(v1)
  std1 = std(v1)*3;
  mean2 = mean(v2)
  std2 = std(v2)*3;
  mean3 = mean(v3)
  std3 = std(v3)*3;
  mean4 = mean(v4)
  std4 = std(v4)*3;
  mean5 = mean(v5)
  std5 = std(v5)*3;
  scatter ([1,2.25,3,4.5,6], [mean1,mean2,mean3,mean4,mean5],15,"r",'filled');
  hold on;
  x = errorbar([1,2.25,3,4.5,6], [mean1,mean2,mean3,mean4,mean5], [std1,std2,std3,std4,std5]);
  xlabel ("Velocidad deseada (m/s)","fontsize", 40);
  set(x,"linewidth",1.5);
  h = ylabel ("Tiempo de evacuacion");
  set (h, "fontsize", 40) 
  set(gca,"fontsize",40);

endfunction