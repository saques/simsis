function [] = graphStd(X, Y, doLog = true, Xcapt, Ycapt, graphTitle)
  meanV = [];
  stdV = [];
  
  
  for i = 1:rows(Y)
    if doLog == true
      meanV(end+1) = mean(log10(Y(i,:)));
      stdV(end+1) = std(log10(Y(i,:)));
    else
      meanV(end+1) = mean(Y(i,:));
      stdV(end+1) = std(Y(i,:))
    endif
    
      
  endfor
  
  if doLog == true
    scale = log10(X);
  else
    scale = X;
  endif
  
  clf;
  errorbar(scale, meanV, stdV,"~-*");
  hold on;
  xlabel(Xcapt);
  ylabel(Ycapt);
  title(graphTitle);
  
endfunction
