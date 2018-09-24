function plotMeanQuadraticError(N)
  ecm1 = [];
  ecm2 = [];
  ecm3 = [];
  steps = [];
 
  
  for i = 0:(N-1)
    i
    fflush(stdout);
    [auxStep , auxEcm] = meanQuadError(strcat('../verlet_numericSolutions',num2str(i),'.txt'),i)
    ecm1 = [ecm1;auxEcm];
    steps = [steps;auxStep];
    [auxStep , auxEcm] = meanQuadError(strcat('../beeman_numericSolutions',num2str(i),'.txt'),i)
    ecm2 = [ecm2;auxEcm];
    [auxStep , auxEcm] = meanQuadError(strcat('../gearPred_numericSolutions',num2str(i),'.txt'),i)
    ecm3 = [ecm3;auxEcm];
  end
  
  plot(log10(steps),log10(ecm1),"color",'r');
  hold on;
  plot(log10(steps),log10(ecm2),"color",'g');
  hold on;
  plot(log10(steps),log10(ecm3),"color",'b');

  xlabel ("log(delta T) (segundos)","fontsize", 15);
  ylabel ("log(Error cuadratico medio)","fontsize", 15);
  l = legend ("Verlet", "Beeman","Gear Predictor Corrector");
  set (l, "fontsize", 15,"location", "northwest") 
  set(gca,"fontsize",15);


endfunction