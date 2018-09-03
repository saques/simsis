function animation(M,particle,static_file, dynamic_file, output_file)
  file = fopen(static_file,'r');
  N = str2num(fgetl(file));
  L = str2num(fgetl(file));
  fclose(file);
  [radius colour] = textread(static_file,"%f %f", 'headerLines', 2);
  [x y vx vy] = textread(dynamic_file,"%f %f %f %f", 'headerLines', 1);
  file = fopen(output_file,'r');
  neighbour={};
  for i = 1:N
    line = fgetl(file);
    line = substr(line,2,length(line)-2);
    neighbour{end+1} = textscan(line,'%d');
  end
  plot = scatter(x,y,15,'b','filled');
  title('Cell Index Method');
  xlabel('X');
  ylabel('Y');
  hold on;
  particleX = x(particle+1);
  particleY = y(particle+1);
  plot = scatter(particleX,particleY,15,'g','filled');
  particle_neighbours = neighbour{particle+1}{1}(2:end);
  neighboursX = x(particle_neighbours+1);
  neighboursY = y(particle_neighbours+1);
  plot = scatter(neighboursX,neighboursY,15,'r','filled');
  set(gca,'xtick',[0:(L/M):L]);
  set(gca,'ytick',[0:(L/M):L]);
  set(gca,'XAxisLocation','top','YAxisLocation','left','ydir','reverse');
  grid on;
endfunction