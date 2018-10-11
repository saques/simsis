function trajectory(v_file)
  file = fopen(v_file,'r');
  N = str2num(fgets(file));
  trajectory = [];
  for i = 1:N
      line = fgets(file);
      line = fgets(file);
      elems = strsplit(line, ';');
      position = str2double(elems);
      trajectory = [trajectory;position];
  end


plot3 (trajectory(:,1),trajectory(:,2), [1:N]);
xlabel ("r.*sin (t)");
ylabel ("r.*cos (t)");
zlabel ("z");
title ("plot3 display of 3-D helix");

hold on

endfunction