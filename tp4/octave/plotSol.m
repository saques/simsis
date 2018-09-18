function plotSol(file_name,color)
  file = fopen(file_name,'r');
  N = str2num(fgets(file));
  trajectory = [];
  for i = 1:N
      line = fgets(file);
      position = str2double(line);
      trajectory = [trajectory;position];
  end

  plot ( [1:N],trajectory,"color",color);
  hold on
endfunction