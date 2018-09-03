function timeHisto(histoBin,time_file)
  file = fopen(time_file,'r');
  N = str2num(fgets(file));
  times=[];
  for i = 1:N
    str2num(fgets(file));
    line = str2num(fgets(file));
    times = [times;line/N];
  end
  hist(times);

endfunction