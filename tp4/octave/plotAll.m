function plotAll()
  plotSol('../verlet_numericSolutions.txt','r');
 %% plotSol('../beeman_numericSolutions.txt','g');
  plotSol('../gearPred_numericSolutions.txt','b');
  plotSol('../analytic_numericSolutions.txt','m');

  xlabel ("t");
  ylabel ("x");
  legend ("verlet", "gearPred","analytic");


endfunction