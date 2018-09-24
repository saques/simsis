function plotSol(file_name,color)
  file = fopen(file_name,'r');
  step = str2num(fgets(file));
  trajectory  = eval(fgets(file));

  plot ( [0:step:5],trajectory,"color",color);
  hold on
endfunction