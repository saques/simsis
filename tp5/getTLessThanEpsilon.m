function T = getTLessThanEpsilon(X, D, epsilon, spf, L = 0.75)
  
  [maxvalue, idx] = max(X);
  T = 0;
  for i = idx(1)+(L/spf):length(X)
    V = X(i)*D;
    for j = i+1:min(length(X), i+1+D)
      V -= X(j);
    endfor
    if(abs(V) < epsilon)
      T = i;
      break
    endif
  endfor 
  
  T *= spf;
  
endfunction
