/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wackss
 */

import java.util.Vector;

/* this class will initialize and store all of the global data
 * (the shuttle info and route info)
 */
public class Data {
    
    private Vector shuttles;
    private Vector routes;
    
    public Data() {
        Position p1 = new Position(-73.67663, 42.73018, true);
        p1.setName("Union");
        Position p2 = new Position(-73.67719, 42.73069);
        p2.setName("Intersection of 15th & Sage");
        Position p3 = new Position(-73.67663, 42.73302);
        p3.setName("Intersection of 15th & Peoples");
        Position p4 = new Position(-73.67170, 42.73236);
        p4.setName("Intersection of Peoples & Burdett");
        Position p5 = new Position(-73.67045, 42.73664, true);
        p5.setName("Colonie Apartments");
        Position p6 = new Position(-73.67011, 42.73819);
        p6.setName("Intersection of Burdett & Hoosick");
        Position p7 = new Position(-73.66659, 42.73768);
        p7.setName("Intersection of Burdett & Georgian");
        Position p8 = new Position(-73.66706, 42.73595);
        p8.setName("Intersection of Georgian & Detroit");
        Position p9 = new Position(-73.66586, 42.73574, true);
        p9.setName("Intersection of Detroit & Edgehill");
        Position p10 = new Position(-73.66316, 42.73532);
        p10.setName("Intersection of Detroit & Sunset");
        Position p11 = new Position(-73.66339, 42.73445, true);
        p11.setName("Intersection of Sunset & Beman");
        Position p12 = new Position(-73.66498, 42.73281, true);
        p12.setName("Intersection of Sunset & Forsyth");
        Position p13 = new Position(-73.66726, 42.73086, true);
        p13.setName("Intersection of Sunset & Cook");
        Position p14 = new Position(-73.67000, 42.73163, true);
        p14.setName("Fieldhouse Stop");
        Position p15 = new Position(-73.67011, 42.73819);
        p15.setName("Intersection of Burdett & Hoosick");
        Position p16 = new Position(-73.68223, 42.73398);
        p16.setName("Intersection of Peoples & 9th");
        Position p17 = new Position(-73.68258, 42.73292, true);
        p17.setName("Intersection of 9th & Federal");
        Position p18 = new Position(-73.68148, 42.73155, true);
        p18.setName("West Hall");
        Position p19 = new Position(-73.67902, 42.73095, true);
        p19.setName("87 Field Stop");

        Position p20 = new Position(-73.67663, 42.73018, true);
        p20.setName("Union");
        Position p21 = new Position(-73.67829, 42.72682);
        p21.setName("Intersection of 15th & College");
        Position p22 = new Position(-73.67977, 42.72282, true);
        p22.setName("Intersection of 15th & Congress");
        Position p23 = new Position(-73.68060, 42.72332);
        p23.setName("Intersection of Congress & 14th");
        Position p24 = new Position(-73.68181, 42.72662);
        p24.setName("Congress Bend");
        Position p25 = new Position(-73.68708, 42.72835);
        p25.setName("Intersection of Congress & 6th");
        Position p26 = new Position(-73.68835, 42.72848);
        p26.setName("Intersection of Congress & 5th");
        Position p27 = new Position(-73.68764, 42.73155, true);
        p27.setName("Blitman Stop");
        Position p28 = new Position(-73.68683, 42.73407);
        p28.setName("Intersection of 5th & Federal");
        Position p29 = new Position(-73.68258, 42.73292, true);
        p29.setName("Intersection of 9th & Federal");
        Position p30 = new Position(-73.68148, 42.73155, true);
        p30.setName("West Hall");
        Position p31 = new Position(-73.67902, 42.73095, true);
        p31.setName("87 Field Stop");

        Route r1 = new Route();
        Route r2 = new Route();
        
        r1.addPosition(p1, 10);
        r1.addPosition(p2, 10);
        r1.addPosition(p3, 10);
        r1.addPosition(p4, 10);
        r1.addPosition(p5, 10);
        r1.addPosition(p6, 10);
        r1.addPosition(p7, 10);
        r1.addPosition(p8, 10);
        r1.addPosition(p9, 10);
        r1.addPosition(p10, 10);
        r1.addPosition(p11, 10);
        r1.addPosition(p12, 10);
        r1.addPosition(p13, 10);
        r1.addPosition(p14, 10);
        r1.addPosition(p15, 10);
        r1.addPosition(p16, 10);
        r1.addPosition(p17, 10);
        r1.addPosition(p18, 10);
        r1.addPosition(p19, 10);

        r2.addPosition(p20, 10);
        r2.addPosition(p21, 10);
        r2.addPosition(p22, 10);
        r2.addPosition(p23, 10);
        r2.addPosition(p24, 10);
        r2.addPosition(p25, 10);
        r2.addPosition(p26, 10);
        r2.addPosition(p27, 10);
        r2.addPosition(p28, 10);
        r2.addPosition(p29, 10);
        r2.addPosition(p30, 10);
        r2.addPosition(p31, 10);

        routes = new Vector();
        //east is in position 1, west is in position 2
        routes.addElement(r1);
        routes.addElement(r2);

        /* the shuttle data will be updated so we just need to initialize
         * the correct # of shuttles
         */
  /*      int numShuttles = 2;
        for (int i=0; i<numShuttles; i++) {
            shuttles.addElement(new Shuttle());
        }*/
    }

    public Vector getRoutes() {
        return routes;
    }

    public Vector getShuttles() {
        return shuttles;
    }

    public void setShuttles(Vector shuttles) {
        this.shuttles = shuttles;
    }

}
