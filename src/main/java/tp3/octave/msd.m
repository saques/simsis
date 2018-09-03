function msd(msd_file)
  file = fopen(msd_file,'r');
  N = str2num(fgets(file));
  msd=[];
  for i = 1:N
    str2num(fgets(file));
    line = str2num(fgets(file));
    msd = [msd;line];
  end
  plot([1:N],msd);

endfunction