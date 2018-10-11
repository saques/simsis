function msdAvg(varargin)
   y=[];
    
  for i = 1:length (varargin)
    file = fopen(varargin{i},'r');

    N = str2num(fgets(file));
    x = [1:N];
    sd = [];
    
    for i = 1:N
      str2num(fgets(file));
      z = str2num(fgets(file));
      sd = [sd;z];
    end
    
    y = [y , sd];
    
  
  endfor
  deviation = std(y');
  y = mean(y');
  p=polyfit(x,y,1);
  hold on
  plot(x,y,'ro','markersize',4,'markerfacecolor','r');
  z=@(x) polyval(p,x);
  
  errorbar(x, z(x(1):x(end)), deviation,"~-*");

  xlabel('x')
  ylabel('y')
  grid on
  title('Polinomio aproximador')
hold off

endfunction