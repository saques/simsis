  file = fopen('../kinematic0.2.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data));
  hold on
  
  file = fopen('../kinematic0.25.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"r");
  hold on
  
  file = fopen('../kinematic0.35.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"m");
  hold on
  
  file = fopen('../kinematic0.5.txt','r');
  format long;
  Data = eval(fgets(file));
  format long;
  plot ([0:1/60:5],log10(Data),"k");
  hold on

  xlabel ("t (segundos)","fontsize", 25);
  h = ylabel ("log(Energia cinetica) (J)");
  l = legend ("D = 0.2 m", "D = 0.25 m","D = 0.35 m","D = 0.5 m");
  set (l, "fontsize", 22,"location", "northeastoutside") 

  set (h, "fontsize", 25) 
  set(gca,"fontsize",25);