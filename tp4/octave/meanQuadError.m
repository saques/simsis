function [step,mqe] = meanQuadError(file_name,i)
  file = fopen(file_name,'r');
  file2 = fopen(strcat('../analytic_numericSolutions',num2str(i),'.txt'),'r');
  step = str2num(fgets(file));
  step = str2num(fgets(file2));
  aprox = eval(fgets(file));
  analytic = eval(fgets(file2));
  quadError =  (aprox - analytic).**2;
  file_name
  mqe = mean(quadError);
endfunction