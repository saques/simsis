function graphs(file_name, pattern_title, generations) 
#  file = fopen(file_name,'r');
  [radius alive cmx cmy cmz] = textread(file_name,"%f %f %f %f %f");
  figure(1);
  plot(1:generations, radius(1:generations));
  title(strcat(pattern_title, ' Radius over the generations'));
  ylabel('Radius');
  xlabel('Generations');
  
  figure(2);
  plot(1:generations, alive(1:generations));
  title(strcat(pattern_title, ' Alive cells over the generations'));
  ylabel('Alive cells');
  xlabel('Generations');
  
  figure(3);
  centerOfMass = sqrt(cmx .** 2 + cmy .** 2 + cmz .** 2);
  plot(1:generations, centerOfMass(1:generations));
  title(strcat(pattern_title, ' Center of mass over the generations'));
  ylabel('Center of mass');
  xlabel('Generations');
  
endfunction