function plotBeverloo(N,c,d1,d2,d3,d4,q1,std1,q2,std2,q3,std3,q4,std4)
    q = @(d) N .* sqrt(9.807).*(d - c.* 0.025) .** (1.5);
    plot ([0:0.05:0.75],q([0:0.05:0.75]),"linewidth",2);
    hold on
    scatter ([d1,d2,d3,d4],[q1,q2,q3,q4],15,"r",'filled');
    x = errorbar([d1,d2,d3,d4], [q1,q2,q3,q4], [std1,std2,std3,std4]);
    set (x, "linestyle", 'none') 

  xlabel ("D (m)","fontsize", 25);
  h = ylabel ("Caudal (particulas/segundo)");
  set (h, "fontsize", 25) 
  l = legend ("Ley de Beverloo c = 0.74", "Caudales experimentales");
  set (l, "fontsize", 22,"location", "southeast") 
  set(gca,"fontsize",28);


endfunction