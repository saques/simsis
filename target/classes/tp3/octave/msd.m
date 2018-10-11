function msd(msd_file)
  file = fopen(msd_file,'r');
  N = str2num(fgets(file));
  y=[];
  x = [1:N];
  for i = 1:N
    str2num(fgets(file));
    line = str2num(fgets(file));
    y = [y;line];
  end
  y = y';
  p=polyfit(x,y,1);
  hold on
  plot(x,y,'ro','markersize',4,'markerfacecolor','r');
  z=@(x) polyval(p,x);
  fplot(z,[x(1),x(end)]);
  xlabel('x')
  ylabel('y')
  grid on
  title('Polinomio aproximador')
hold off

 

endfunction