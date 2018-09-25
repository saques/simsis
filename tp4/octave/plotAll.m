function plotAll(num)

  plotSol(strcat('../verlet_numericSolutions',num2str(num),'.txt'),'r');
  plotSol(strcat('../beeman_numericSolutions',num2str(num),'.txt'),'g');
  plotSol(strcat('../gearPred_numericSolutions',num2str(num),'.txt'),'b');
  plotSol(strcat('../analytic_numericSolutions',num2str(num),'.txt'),'m');

  xlabel ("t (segundos)","fontsize",15);
  ylabel ("x (metros)","fontsize",15);
  l = legend ("Verlet", "Beeman","Gear Predictor Corrector","Solucion Analitica");
  set (l, "fontsize", 15) 
  set(gca,"fontsize",15);


endfunction