function Q = graphQ(file_name,window_size,window_step)
  file = fopen(file_name,'r');
  Data = eval(fgets(file));
  format long;
  indo = bsxfun(@plus, (1 : window_size), (0 :window_step:numel(Data) - window_size).');
  last = window_size;
  Q = [];
  while ( last <= length(Data))
    Q = [ Q ; window_size / (Data(last) - Data(last-window_size + 1))];
    last += window_step;
  endwhile
     plot (Data((window_size/2):(end-(window_size/2))),Q,"linewidth",3);
    xlabel ("t (s)","fontsize", 28);
       ylabel ("Particulas","fontsize", 28);
         set(gca,"fontsize",28);

endfunction