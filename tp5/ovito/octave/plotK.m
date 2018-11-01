  file = fopen('../kinematic0.2.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"linewidth",2);
  hold on
  
  file = fopen('../kinematic0.25.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"r","linewidth",2);
  hold on
  
  file = fopen('../kinematic0.3.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"m","linewidth",2);
  hold on
  
  file = fopen('../kinematic0.35.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"k","linewidth",2);
  hold on

  xlabel ("t (segundos)","fontsize", 25);
  h = ylabel ("log(Energia cinetica) (J)");
  l = legend ("D = 0.2 m", "D = 0.25 m","D = 0.3 m","D = 0.35 m");
  set (l, "fontsize", 23,"location", "northeastoutside") 

  set (h, "fontsize", 28) 
  set(gca,"fontsize",28);