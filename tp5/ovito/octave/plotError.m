function [c] = plotError(N,d1,d2,d3,d4,q1,q2,q3,q4)
    qe1 = @(c) N .* sqrt(9.807).*(d1 - c.* 0.025) .** (1.5);
    qe2 = @(c) N .* sqrt(9.807).*(d2 - c.* 0.025) .** (1.5);
    qe3 = @(c) N .* sqrt(9.807).*(d3 - c.* 0.025) .** (1.5);
    qe4 = @(c) N .* sqrt(9.807).*(d4 - c.* 0.025) .** (1.5);
    
    ec = (qe1(0:(1/100):8) - ones(1,801)*q1).**2 + (qe2(0:(1/100):8) - ones(1,801)*q2) .**2 + (qe3(0:(1/100):8) - ones(1,801)*q3).**2 + (qe4(0:(1/100):8) - ones(1,801)*q4).**2;
    plot([0:(1/100):8],ec, "linewidth",2);  
    [minVal,ind] = min(ec);
    c = (0:(1/100):8)(ind);
    ec(ind)
      xlabel ("c","fontsize", 28);
       ylabel ("EC","fontsize", 28);
         set(gca,"fontsize",28);

endfunction