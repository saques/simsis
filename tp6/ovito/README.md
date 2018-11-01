    private static double frameRate = 30;
    private static Long seed = null;
    private static int M = 8;
    private static int N = 175;
    private static float L = 20, W = 20, minRadius = 0.25f, maxRadius = 0.29f, mass = 65.0f, v = 2.25f;
    private static double DeltaTime = 4E-4;
    private static double k = 1.2E5, gamma = 100, mu = 0.1, kt = 2.4E5;
    private static double D = 1.2;
    private static double A = 2000;
    private static double B = 0.08;
    private static double tau = 0.5;
    private static int dumpEach = (int) ((1.0/frameRate) / DeltaTime);
    private static double pathRadius = 0.5;
	
	
	La seed es el ultimo numero del nombre de la carpeta.