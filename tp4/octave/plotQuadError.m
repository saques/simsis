function plotQuadError(file_name,color)
  file = fopen(file_name,'r');
  file2 = fopen('../analytic_numericSolutions.txt','r');
  N = str2num(fgets(file));
    N2 = str2num(fgets(file2));

  quadError = [];
  for i = 1:N
      line = fgets(file);
      line2 = fgets(file2);
      position = str2double(line);
      position2 = str2double(line2);
      quadError = [quadError;(position-position2)**2];
  end

  plot ( [1:N],quadError,"color",color);
  mean(quadError)/length(quadError)
  hold on
endfunction
