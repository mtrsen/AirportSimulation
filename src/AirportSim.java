///Hansen Zhao
import java.text.DecimalFormat;


public class AirportSim {
    public static void main(String[] args) {
        double distance [][] =  {{0, 1942.95, 2356.59, 2301.09, 923.19},
                                {1942.95, 0, 607.97, 538.49, 2200.96},
                                {2356.59, 607.97, 0, 917.07, 2755.33},
                                {2301.09, 538.49, 917.07, 0, 2354.06},
                                {923.19, 2200.96, 2755.33, 2354.06}};
        // create 5 different airport

        Airport lax = new Airport("LAX",15,30,15, distance[0],
                0, 0,0, 0, true, 10, 17);
        Airport atl = new Airport("ATL",15,30,15, distance[1],
                1,0,0,0, true, 10, 20);
        Airport mia = new Airport("MIA",13,27,13, distance[2],
                2,0,0,0, false, 10, 16);
        Airport was = new Airport("WAS",12,25,12, distance[3],
                3,0,0,0, false, 10, 15);
        Airport sea = new Airport("SEA",13,27,13, distance[4],
                4,0,0,0,true, 10, 15);
        // put all the airport in one array
        Airport port[] = new Airport[]{lax, atl, mia, was, sea};
        // generate 16 airplanes
        for(int i = 0; i < 8; i++) {
            Airplane airplane1 = new Airplane("A380", 0, 853, 587, i, true);
            Airplane airplane2 = new Airplane("B777", 0, 550, 558, i, false);
            AirportEvent landingEvent1 = new AirportEvent(0, port[i % 5], AirportEvent.PLANE_ARRIVES, airplane1, port, 0);
            AirportEvent landingEvent2 = new AirportEvent(0, port[i % 5], AirportEvent.PLANE_ARRIVES, airplane2, port, 0);
            Simulator.schedule(landingEvent1);
            Simulator.schedule(landingEvent2);
        }

        Simulator.stopAt(200);
        Simulator.run();
        // print the circling time depart people and arrive people
        for(int i = 0; i < port.length; i++){
            DecimalFormat format = new DecimalFormat("0.00");
            System.out.println(port[i].getArriv_people() + " passengers arrives in " + port[i].getName());
            System.out.println(port[i].getDepart_people() + " passengers departs in " + port[i].getName());
            System.out.println(port[i].getName() + " circling time is " + format.format(port[i].getCirclingtime()));
        }
    }
}
