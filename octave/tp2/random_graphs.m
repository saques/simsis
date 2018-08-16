function random_graphs(base_path, m, generations)
  random_samples = zeros(generations, m);
  for i = 1:m
    [radius alive cmx cmy cmz] = textread(strcat(base_path, strcat(num2str(i-1), "_.statistics")), "%f %f %f %f %f");
    random_samples(:, i) = alive(1:generations);
  endfor
  graphStd(1:generations, random_samples, false, "Generations", "Mean alive cells", "");
 
  [real_radius real_alive cmx cmy cmz] = textread(strcat(base_path, "r.statistics"), "%f %f %f %f %f");
  
  meanV = [];
  for i = 1:generations
    meanV(end+1) = mean(random_samples(i,:));
  endfor
  figure(3);
  # plot(1:generations, abs(meanV - real_alive'(1:generations)));
  plot(1:generations, meanV);
  hold on;
  plot(1:generations, real_alive'(1:generations));
  xlabel('Generations');
  ylabel('Alive cells');
  legend_str{1}='Mean alive cells in random samples';
  legend_str{2}='Alive cells in proper pattern';
  legend(legend_str)
  legend('boxon')
  title('');
endfunction
