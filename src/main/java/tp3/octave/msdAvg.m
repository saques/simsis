function msdAvg(varargin)
   msd=[];
  
  for i = 1:length (varargin)
    file = fopen(varargin{i},'r');

    N = str2num(fgets(file));
    
    sd = [];
    
    for i = 1:N
      str2num(fgets(file));
      z = str2num(fgets(file));
      sd = [sd;z];
    end
    
    msd = [msd , sd];
    
  
  endfor
  plot([1:N],sum(msd') /length (varargin) ) ;

endfunction