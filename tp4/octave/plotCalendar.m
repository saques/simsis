function plotCalendar(file_name)
  distances = dlmread(file_name);
  halfdist = idivide(length(distances), 2);
  semilogy([-halfdist: halfdist - 1],distances);
  [m, idx] = min(distances);
  M = max(distances);
  axis ([-halfdist, halfdist - 1, m, M], "tic", "labely");
  yticks([10**8:10**8:10**9, 10**9:10**9:10**10]);
  xlabel ("Time centered at Sept. 5 1977 (days)");
  ylabel ("Voyager's minimum distance to Jupiter and Saturn(km)");
  halfdist
endfunction
