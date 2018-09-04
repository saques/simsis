function vHisto(histoBin,v_file)
  file = fopen(v_file,'r');
  N = str2num(fgets(file));
  v=[];
  for i = 1:N
    str2num(fgets(file));
    line = str2num(fgets(file));
    v = [v;line/N];
  end
  hist(v,histoBin);

endfunction