package tp4.ship;

import common.Vector2D;
import utils.PointDumper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CraftMain {

    static final double YEAR = 31556926;

    static final double BD = 1500 ;

    static final double maxTime = YEAR*4;
    static final double delta = 60*60*24*2;

    static final double maxSpeed = 20;
    static final double maxAltitude = 4100;
    static final double karmanLine = 4100;

    static final double heightStep = 0.5;
    static final double speedStep = 0.005;

    static final double eMass = 5.97237E24, eRadius = 6371;

    static final double craftMass = 721.9;

    public static void main(String[] args) throws IOException{
        runYearGPC(100, 8.991439786408133, 5225.0);
//        gpc();

//        gpcNextTime();

        //gpcForDefined(4100.0,10.32549300563502);
    }

    private static void beeman() throws IOException {

      PointDumper beemanDumper = new PointDumper(".\\tp4\\ovito\\beeman\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

      CraftStats stats = runBeeman(delta, maxTime, beemanDumper);

      beemanDumper.dumpList(stats.getDump());

      System.out.println("Ejection speed: " + stats.getV());
      System.out.println("Departure orbit: " + stats.getH());
      System.out.println("Closest distance to Jupiter: " + stats.getMinToJupiter());
      System.out.println("Closest distance to Saturn: " + stats.getMinToSaturn());

    }

    private static void gpc() throws IOException{
        PointDumper gpcDumper = new PointDumper(".\\tp4\\ovito\\gpc\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        CraftStats stats = runGearPredictorCorrector(delta, maxTime, gpcDumper);

        System.out.println("Ejection speed: " + stats.getV());
        System.out.println("Departure orbit: " + stats.getH());
        System.out.println("Closest distance to Jupiter: " + stats.getMinToJupiter());
        System.out.println("Years to Jupiter: " + stats.gettJupiter()/YEAR);
        System.out.println("Closest distance to Saturn: " + stats.getMinToSaturn());
        System.out.println("Years to Saturn: " + stats.gettSaturn()/YEAR);

        gpcDumper.dumpList(stats.getDump());
        stats.printBestSpeedsAndEnergy(".\\tp4\\ovito\\gpc\\");
    }

    private static void gpcNextTime(){

        double t = runGearPredictorCorrectorNextTime(delta, maxTime, null, /*0.00006684491*/ 0.1);

        System.out.println("Next time: " + t);
    }

    private static void gpcForDefined(double h, double v) throws IOException{

        PointDumper gpcDumper = new PointDumper(".\\tp4\\ovito\\gpc\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);

        CraftStats stats = runGearPredictorCorrectorForDefinedValues(delta, maxTime, gpcDumper, h, v);

        System.out.println("Ejection speed: " + stats.getV());
        System.out.println("Departure orbit: " + stats.getH());
        System.out.println("Closest distance to Jupiter: " + stats.getMinToJupiter());
        System.out.println("Years to Jupiter: " + stats.gettJupiter()/YEAR);
        System.out.println("Closest distance to Saturn: " + stats.getMinToSaturn());
        System.out.println("Years to Saturn: " + stats.gettSaturn()/YEAR);

        gpcDumper.dumpList(stats.getDump());
        stats.printBestSpeedsAndEnergy(".\\tp4\\ovito\\gpc\\");

    }


    private static List<MDParticle> beemanSystem(double v, double h, PointDumper dumper){
        List<MDParticle> system = new ArrayList<>(5);

        BeemanMDParticle sun = new BeemanMDParticle(1.9885E30, 695700 , 0, 0, 0, 0, dumper);

        BeemanMDParticle earth = new BeemanMDParticle(
                5.97237E24, 6371,
                1.443040359985483E+8, -4.566821691926755E+7,
                8.429276455862507E+0, 2.831601955976786E+1,
                dumper);

        BeemanMDParticle saturn = new BeemanMDParticle(
                5.68319E+26, 58232,
                -1.075238877886715E+09, 8.538222924091074E+08,
                -6.527515746018062E+00, -7.590526046562251E+00,
                dumper
        );

        BeemanMDParticle jupiter = new BeemanMDParticle(
                1.89813E+27, 69911,
                1.061950341671551E+08, 7.544955348409320E+08,
                -1.309157032053854E+01, 2.424744678419164E+00,
                dumper
        );

        Vector2D earthPos = new Vector2D(earth.x0, earth.y0);
        Vector2D earthNor = new Vector2D(earthPos).nor();

        Vector2D earthV = new Vector2D(earth.vx0, earth.vy0);
        Vector2D earthVNor = new Vector2D(earthV).nor();

        Vector2D craftPos = new Vector2D(earthNor).scl(earthPos.mod() + eRadius + h);
        Vector2D craftV = new Vector2D(earthVNor).scl(earthV.mod() + v);

        BeemanMDParticle voyager = new BeemanMDParticle(
                craftMass, 0.01,
                craftPos.x, craftPos.y,
                craftV.x, craftV.y,
                dumper
        );

        system.add(sun);
        system.add(earth);
        system.add(jupiter);
        system.add(saturn);
        system.add(voyager);

        return system;
    }



    private static CraftStats runBeeman(double delta, double maxTime, PointDumper dumper) throws IOException {


        CraftStats stats = new CraftStats();

        for(double h = karmanLine; h < maxAltitude; h += heightStep){

            //Calculate escape velocity
            double minV = Math.sqrt((2*MDParticle.G*eMass)/(eRadius + h));

            for(double v = minV; v < maxSpeed; v += speedStep){

                System.out.printf("Running for height %f, speed %f\n", h, v);

                List<MDParticle> system = beemanSystem(v, h, dumper);

                BeemanMDParticle sun = (BeemanMDParticle)system.get(0);
                BeemanMDParticle earth = (BeemanMDParticle)system.get(1);
                BeemanMDParticle jupiter = (BeemanMDParticle)system.get(2);
                BeemanMDParticle saturn = (BeemanMDParticle)system.get(3);
                BeemanMDParticle voyager = (BeemanMDParticle)system.get(4);

                system.forEach(x -> system.forEach(y -> {
                    if(x != y) {
                        x.interact(y);
                        ((BeemanMDParticle)x).initializeF();
                    }
                }));

                double minJupiter = Double.MAX_VALUE, minSaturn = Double.MAX_VALUE;
                double tMinJupiter = 0, tMinSaturn = 0;

                system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
                dumper.dumpToList();

                for(double d = 0; d < maxTime ; d += delta){
                    system.forEach(x -> x.rDelta(delta));

                    system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));

                    double saturnDist = new Vector2D(saturn.x0, saturn.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
                    double jupiterDist = new Vector2D(jupiter.x0, jupiter.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
                    double earthDist = new Vector2D(earth.x0, earth.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
                    double sunDist = new Vector2D(sun.x0, sun.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

                    if(saturnDist <= saturn.radius || jupiterDist <= jupiter.radius || earthDist <= earth.radius || sunDist <= sun.radius){
                        dumper.getList();
                        break;
                    }

                    if(jupiterDist < minJupiter){
                        minJupiter = jupiterDist;
                        tMinJupiter = d;
                    }

                    if(saturnDist < minSaturn){
                        minJupiter = jupiterDist;
                        tMinSaturn = d;
                    }

                    for (MDParticle mdParticle : system) {
                        mdParticle.saveState(0);
                    }

                    system.forEach(x -> system.forEach(y -> {
                        if(x != y)
                            x.interact(y);
                    }));

                    system.forEach(x -> x.vDelta(delta));
                    dumper.dumpToList();
                }

                List<String> dump = dumper.getList();

                if(stats.isBetterApproach(minJupiter, minSaturn, tMinJupiter, tMinSaturn, v, h)){
                    stats.setDump(dump);
                }
            }
        }

        return stats;
    }


    private static List<MDParticle> gearPredictorCorrectorSystem(double v, double h, PointDumper dumper){
        List<MDParticle> system = new ArrayList<>(5);

        GearPredictorCorrectorParticle sun = new GearPredictorCorrectorParticle(1.9885E30, 695700 , 0, 0, 0, 0, dumper);

        GearPredictorCorrectorParticle earth = new GearPredictorCorrectorParticle(
                5.97237E24, 6371,
                1.443040359985483E+8, -4.566821691926755E+7,
                8.429276455862507E+0, 2.831601955976786E+1,
                dumper);

        GearPredictorCorrectorParticle saturn = new GearPredictorCorrectorParticle(
                5.68319E+26, 58232,
                -1.075238877886715E+09, 8.538222924091074E+08,
                -6.527515746018062E+00, -7.590526046562251E+00,
                dumper
        );

        GearPredictorCorrectorParticle jupiter = new GearPredictorCorrectorParticle(
                1.89813E+27, 69911,
                1.061950341671551E+08, 7.544955348409320E+08,
                -1.309157032053854E+01, 2.424744678419164E+00,
                dumper
        );

        Vector2D earthPos = new Vector2D(earth.x0, earth.y0);
        Vector2D earthNor = new Vector2D(earthPos).nor();

        Vector2D earthV = new Vector2D(earth.vx0, earth.vy0);
        Vector2D earthVNor = new Vector2D(earthV).nor();

        Vector2D craftPos = new Vector2D(earthNor).scl(earthPos.mod() + eRadius + h);
        Vector2D craftV = new Vector2D(earthVNor).scl(earthV.mod() + v);

        GearPredictorCorrectorParticle voyager = new GearPredictorCorrectorParticle(
                craftMass, 0.01,
                craftPos.x, craftPos.y,
                craftV.x, craftV.y,
                dumper
        );

        system.add(sun);
        system.add(earth);
        system.add(jupiter);
        system.add(saturn);
        system.add(voyager);

        return system;
    }

    private static List<MDParticle> gearPredictorCorrectorSystemNoVoyager(PointDumper dumper){
        List<MDParticle> system = new ArrayList<>(4);

        GearPredictorCorrectorParticle sun = new GearPredictorCorrectorParticle(1.9885E30, 695700 , 0, 0, 0, 0, dumper);

        GearPredictorCorrectorParticle earth = new GearPredictorCorrectorParticle(
                5.97237E24, 6371,
                1.443040359985483E+8, -4.566821691926755E+7,
                8.429276455862507E+0, 2.831601955976786E+1,
                dumper);

        GearPredictorCorrectorParticle saturn = new GearPredictorCorrectorParticle(
                5.68319E+26, 58232,
                -1.075238877886715E+09, 8.538222924091074E+08,
                -6.527515746018062E+00, -7.590526046562251E+00,
                dumper
        );

        GearPredictorCorrectorParticle jupiter = new GearPredictorCorrectorParticle(
                1.89813E+27, 69911,
                1.061950341671551E+08, 7.544955348409320E+08,
                -1.309157032053854E+01, 2.424744678419164E+00,
                dumper
        );

        system.add(sun);
        system.add(earth);
        system.add(jupiter);
        system.add(saturn);

        return system;
    }




    private static CraftStats runGearPredictorCorrector(double delta, double maxTime, PointDumper dumper) throws IOException {


        CraftStats stats = new CraftStats();

        for(double h = karmanLine; h <= maxAltitude; h += heightStep){

            //Calculate escape velocity
            double minV = Math.sqrt((2*MDParticle.G*eMass)/(eRadius + h));
            System.out.printf("Running for height %f. Jupiter: %f, Saturn: %f\n", h, stats.getMinToJupiter(), stats.getMinToSaturn());

            for(double v = minV; v < maxSpeed; v += speedStep){


                List<MDParticle> system = gearPredictorCorrectorSystem(v, h, dumper);
                System.out.println("Values v: " + v + ", h:" + h);
                GearPredictorCorrectorParticle sun = (GearPredictorCorrectorParticle)system.get(0);
                GearPredictorCorrectorParticle earth = (GearPredictorCorrectorParticle)system.get(1);
                GearPredictorCorrectorParticle jupiter = (GearPredictorCorrectorParticle)system.get(2);
                GearPredictorCorrectorParticle saturn = (GearPredictorCorrectorParticle)system.get(3);
                GearPredictorCorrectorParticle voyager = (GearPredictorCorrectorParticle)system.get(4);


                MDParticle.interact(system);

                double minJupiter = Double.MAX_VALUE, minSaturn = Double.MAX_VALUE;
                double tMinJupiter = 0, tMinSaturn = 0;

                system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
                dumper.dumpToList();


                double saturnDist , jupiterDist ,earthDist ,sunDist;

                boolean bad = false;
                for(double d = 0; d < maxTime ; d += delta){

                    system.forEach(x -> x.rDelta(delta));
                    system.forEach(MDParticle::resetForces);
                    MDParticle.interact(system);

                    double totalEnergy = 0;

                    for (MDParticle x : system) {
                        x.vDelta(delta);
                        totalEnergy += x.U + x.kineticEnergy();
                    }

                    saturnDist = new Vector2D(saturn.x0, saturn.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
                    jupiterDist = new Vector2D(jupiter.x0, jupiter.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
                    earthDist = new Vector2D(earth.x0, earth.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
                    sunDist = new Vector2D(sun.x0, sun.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

                    if(saturnDist <= (saturn.radius + BD) || jupiterDist <= (jupiter.radius + BD) || earthDist <= earth.radius || sunDist <= (sun.radius + BD)){
                        dumper.getList();
                        bad = true;
                        break;
                    }

                    if(jupiterDist < minJupiter){
                        minJupiter = jupiterDist;
                        tMinJupiter = d;
                    }

                    if(saturnDist < minSaturn){
                        minSaturn = saturnDist;
                        tMinSaturn = d;
                    }

                    system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
                    stats.logSpeedAndEnergy(new Vector2D(voyager.vx0, voyager.vy0).mod(), totalEnergy);
                    dumper.dumpToList();
                }

                List<String> dump = dumper.getList();

                if(stats.isBetterApproach(minJupiter, minSaturn, tMinJupiter, tMinSaturn, v, h) && !bad){
                    stats.setDump(dump);
                }
                stats.resetSpeedsAndEnergy();
            }
        }

        return stats;
    }


    private static CraftStats runGearPredictorCorrectorForDefinedValues(double delta, double maxTime, PointDumper dumper, double h, double v) {

        CraftStats stats = new CraftStats();

        List<MDParticle> system = gearPredictorCorrectorSystem(v, h, dumper);

        GearPredictorCorrectorParticle jupiter = (GearPredictorCorrectorParticle)system.get(2);
        GearPredictorCorrectorParticle saturn = (GearPredictorCorrectorParticle)system.get(3);
        GearPredictorCorrectorParticle voyager = (GearPredictorCorrectorParticle)system.get(4);

        MDParticle.interact(system);


        system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
        dumper.dumpToList();


        double saturnDist , jupiterDist ;
        double tMinJupiter = 0, tMinSaturn = 0;
        double minJupiter = Double.MAX_VALUE, minSaturn = Double.MAX_VALUE;

        for(double d = 0; d < maxTime ; d += delta){

            system.forEach(x -> x.rDelta(delta));
            system.forEach(MDParticle::resetForces);
            MDParticle.interact(system);

            double totalEnergy = 0;

            for (MDParticle x : system) {
                x.vDelta(delta);
                totalEnergy += x.U + x.kineticEnergy();
            }

            saturnDist = new Vector2D(saturn.x0, saturn.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
            jupiterDist = new Vector2D(jupiter.x0, jupiter.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

            if(jupiterDist < minJupiter){
                minJupiter = jupiterDist;
                tMinJupiter = d;
            }

            if(saturnDist < minSaturn){
                minSaturn = saturnDist;
                tMinSaturn = d;
            }

            system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
            stats.logSpeedAndEnergy(new Vector2D(voyager.vx0, voyager.vy0).mod(), totalEnergy);
            dumper.dumpToList();

        }

        List<String> dump = dumper.getList();

        if(stats.isBetterApproach(minJupiter, minSaturn, tMinJupiter, tMinSaturn, v, h)){
            stats.setDump(dump);
        }


        return stats;

    }


    private static double runGearPredictorCorrectorNextTime(double delta, double maxTime, PointDumper dumper, double deltaPos) {

        List<MDParticle> startingSystem = gearPredictorCorrectorSystemNoVoyager(dumper);

        GearPredictorCorrectorParticle jupiter0 = (GearPredictorCorrectorParticle)startingSystem.get(2);
        GearPredictorCorrectorParticle saturn0 = (GearPredictorCorrectorParticle)startingSystem.get(3);

        List<MDParticle> system = gearPredictorCorrectorSystemNoVoyager(dumper);

        GearPredictorCorrectorParticle jupiter = (GearPredictorCorrectorParticle)system.get(2);
        GearPredictorCorrectorParticle saturn = (GearPredictorCorrectorParticle)system.get(3);

        MDParticle.interact(system);

        double d = 0;

        double minJDist = Double.MAX_VALUE, minSDist = Double.MAX_VALUE;

        while (d <= 6E9){
            System.out.println(d);
            double jupitersDistance = new Vector2D(jupiter0.x0, jupiter0.y0).sub(new Vector2D(jupiter.x0, jupiter.y0)).mod();
            double saturnsDistance = new Vector2D(saturn0.x0, saturn0.y0).sub(new Vector2D(saturn.x0, saturn.y0)).mod();

            if(jupitersDistance <= deltaPos && saturnsDistance <= deltaPos && d >= 31558118.4)
                return d;
            else if(d >= 31558118.4){
                minJDist = Math.min(minJDist, jupitersDistance);
                minSDist = Math.min(minSDist, saturnsDistance);
            }

            system.forEach(x -> x.rDelta(delta));
            system.forEach(MDParticle::resetForces);
            MDParticle.interact(system);


            for (MDParticle x : system) {
                x.vDelta(delta);
            }

            System.out.println(jupitersDistance);


            d += delta;

        }

        System.out.println(minSDist);

        return -1;

    }

    public static CraftStats runSystemGPC(List<MDParticle> system, PointDumper dumper, double v, double h) throws IOException{
        CraftStats stats = new CraftStats();
        MDParticle.interact(system);
        system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
        dumper.dumpToList();
        GearPredictorCorrectorParticle sun = (GearPredictorCorrectorParticle)system.get(0);
        GearPredictorCorrectorParticle earth = (GearPredictorCorrectorParticle)system.get(1);
        GearPredictorCorrectorParticle jupiter = (GearPredictorCorrectorParticle)system.get(2);
        GearPredictorCorrectorParticle saturn = (GearPredictorCorrectorParticle)system.get(3);
        GearPredictorCorrectorParticle voyager = (GearPredictorCorrectorParticle)system.get(4);
        double minJupiter = Double.MAX_VALUE, minSaturn = Double.MAX_VALUE;
        double tMinJupiter = 0, tMinSaturn = 0;
        double saturnDist , jupiterDist ,earthDist ,sunDist;
        for(double d = 0; d < maxTime ; d += delta){

            system.forEach(x -> x.rDelta(delta));
            system.forEach(MDParticle::resetForces);
            MDParticle.interact(system);

            double totalEnergy = 0;

            for (MDParticle x : system) {
                x.vDelta(delta);
                totalEnergy += x.U + x.kineticEnergy();
            }

            saturnDist = new Vector2D(saturn.x0, saturn.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
            jupiterDist = new Vector2D(jupiter.x0, jupiter.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
            earthDist = new Vector2D(earth.x0, earth.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();
            sunDist = new Vector2D(sun.x0, sun.y0).sub(new Vector2D(voyager.x0, voyager.y0)).mod();

            if(saturnDist <= (saturn.radius + BD) || jupiterDist <= (jupiter.radius + BD) || earthDist <= earth.radius || sunDist <= (sun.radius + BD)){
                dumper.getList();
                break;
            }

            if(jupiterDist < minJupiter){
                minJupiter = jupiterDist;
                tMinJupiter = d;
            }

            if(saturnDist < minSaturn){
                minSaturn = saturnDist;
                tMinSaturn = d;
            }

            system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
            stats.logSpeedAndEnergy(new Vector2D(voyager.vx0, voyager.vy0).mod(), totalEnergy);
//            dumper.dump(d);
            dumper.dumpToList();
        }
        List<String> dump = dumper.getList();
        if(stats.isBetterApproach(minJupiter, minSaturn, tMinJupiter, tMinSaturn, v, h)){
            stats.setDump(dump);
        }
        stats.resetSpeedsAndEnergy();
        dumper.dumpStats();
        return stats;
    }

    public static void runYearGPC(int maxYears, double v, double h) throws IOException{
        PointDumper dumper = new PointDumper(".\\tp4\\ovito\\yearOnly\\", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
        List<MDParticle> system = gearPredictorCorrectorSystemNoVoyager(dumper);
        double delta = 60 * 60 * 24;

        for (double d = 0; d < 365 * (maxYears / 2); d++) {
            system.forEach(x -> x.rDelta(-delta));
            system.forEach(MDParticle::resetForces);
            MDParticle.interact(system);
            system.forEach(x -> x.vDelta(-delta));
        }
        PrintWriter writer = new PrintWriter(new FileWriter(".\\tp4\\ovito\\yearOnly\\calendar.stats"));
        for (double d = 0; d < 365 * maxYears; d++) {
            PointDumper dayDumper = new PointDumper(".\\tp4\\ovito\\yearOnly\\stats_"+ (int)d + "_", PointDumper.FileMode.DYNAMIC, PointDumper.Dimensions._2D);
            List<MDParticle> daySystem = makeDaySystem(system, dayDumper, v, h);
            CraftStats stats = runSystemGPC(daySystem, dayDumper, v, h);
            System.out.println("/////////" );
            System.out.println("Ejection speed: " + stats.getV());
            System.out.println("Departure orbit: " + stats.getH());
            System.out.println("Closest distance to Jupiter: " + stats.getMinToJupiter());
            System.out.println("Years to Jupiter: " + stats.gettJupiter()/YEAR);
            System.out.println("Closest distance to Saturn: " + stats.getMinToSaturn());
            System.out.println("Years to Saturn: " + stats.gettSaturn()/YEAR);

            writer.println(String.format("%f", stats.getMinToJupiter() + stats.getMinToSaturn()));
            system.forEach(x -> x.rDelta(delta));
            system.forEach(MDParticle::resetForces);
            MDParticle.interact(system);
            system.forEach(x -> x.vDelta(delta));
            system.forEach(x-> dumper.print2D(x.x0/MDParticle.AU, x.y0/MDParticle.AU, x.vx0, x.vy0, x.mass, x.radius, x.id));
            System.out.println("One day more: " + d);
            dumper.dump(d);
        }

        writer.flush();
        writer.close();
    }

    public static List<MDParticle> makeDaySystem(List<MDParticle> system, PointDumper dumper, double v, double h) {
        List<MDParticle> daySystem = system.stream().map(p -> new GearPredictorCorrectorParticle(p.mass, p.radius, p.x0, p.y0, p.vx0, p.vy0, dumper)).collect(Collectors.toList());
        MDParticle earth = daySystem.get(1);

        Vector2D earthPos = new Vector2D(earth.x0, earth.y0);
        Vector2D earthNor = new Vector2D(earthPos).nor();

        Vector2D earthV = new Vector2D(earth.vx0, earth.vy0);
        Vector2D earthVNor = new Vector2D(earthV).nor();

        Vector2D craftPos = new Vector2D(earthNor).scl(earthPos.mod() + eRadius + h);
        Vector2D craftV = new Vector2D(earthVNor).scl(earthV.mod() + v);

        MDParticle voyager = new GearPredictorCorrectorParticle(
            craftMass, 0.01,
            craftPos.x, craftPos.y,
            craftV.x, craftV.y,
            dumper
        );
        daySystem.add(voyager);
        return daySystem;
    }
}
