function quadraticError()
  plotQuadError('../verlet_numericSolutions.txt','r');
 %% plotQuadError('../beeman_numericSolutions.txt','g');
  plotQuadError('../gearPred_numericSolutions.txt','b');

  xlabel ("t");
  ylabel ("x");


endfunction