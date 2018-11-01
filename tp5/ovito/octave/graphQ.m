function Q = graphQ(file_name,window_size,window_step,delta)
  file = fopen(file_name,'r');
  N = str2num(fgets(file));
  format long;
  Data = eval(fgets(file));
  format long;
  indo = bsxfun(@plus, (1 : window_size), (0 :window_step:numel(Data) - window_size).');
  Q = sum(Data(indo)')/(window_size*delta);
  x = [window_size*delta:window_step*delta:length(Data)*delta];
  plot(x,Q, "linewidth",2);
    xlabel ("t (s)","fontsize", 28);
       ylabel ("Particulas","fontsize", 28);
         set(gca,"fontsize",28);

endfunction