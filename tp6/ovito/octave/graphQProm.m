function Q = graphQProm(file_name1,file_name2,file_name3,file_name4,file_name5,window_size,window_step)
  file1 = fopen(file_name1,'r');
  Data1 = eval(fgets(file1));
   file2 = fopen(file_name2,'r');
  Data2 = eval(fgets(file2));
   file3 = fopen(file_name3,'r');
  Data3 = eval(fgets(file3));
   file4 = fopen(file_name4,'r');
  Data4 = eval(fgets(file4));
   file5 = fopen(file_name5,'r');
  Data5 = eval(fgets(file5));
  Data = (Data1+Data2+Data3+Data4+Data5)./5;
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
       ylabel ("Caudal (Peatones / s)","fontsize", 28);
         set(gca,"fontsize",28);

endfunction