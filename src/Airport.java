//Hansen Zhao
import java.text.DecimalFormat;
import java.util.*;

public class Airport implements EventHandler {

    //TODO add landing and takeoff queues, random variables
    private boolean m_runwayfree;
    private double [] m_flightTime;
    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;
    private double m_takeoffTime;
    private int number;
    private int depart_people = 0;
    private int arriv_people = 0;
    private double current;
    private double circling_time;
    private String m_airportName;
    // use priority queue to handle this airport
    private PriorityQueue<AirportEvent> eventqueue = new PriorityQueue<>();
    // give airport parameters needed
    public Airport(String name, double runwayTimeToLand, double requiredTimeOnGround, double m_takeoffTime, double[]flightTime, int num, int arriv_people,
                   int depart_people, double circling_time) {
        m_airportName = name;
        m_runwayfree = true;
        m_runwayTimeToLand = runwayTimeToLand;
        m_requiredTimeOnGround = requiredTimeOnGround;
        this.m_takeoffTime = m_takeoffTime;
        m_flightTime = flightTime;
        number = num;
        this.arriv_people = arriv_people;
        this.depart_people = depart_people;
        this.circling_time = circling_time;
    }
    public String getName() {
        return m_airportName;
    }
    public int getArriv_people(){
        return arriv_people;
    }
    public int getDepart_people(){
        return depart_people;
    }
    public double getCirclingtime(){
        return circling_time;
    }
    public void handle(Event event) {
        AirportEvent airEvent = (AirportEvent) event;
        // get current passengers on the plane
        int cur_passenger = airEvent.passengers;
        Airplane airplane1 = airEvent.current_plane;
        // get the capacity of plane
        int passenger1 = airplane1.getcapacity();
        Airport[] port = airEvent.port_matrix;
        switch (airEvent.getType()) {
            case AirportEvent.PLANE_ARRIVES:
                //if the initial case is arrive we should random generate the passengers
                if (arriv_people == 0) {
                    Random rand = new Random();
                    cur_passenger = rand.nextInt(passenger1 + 1);
                }
                // check if this plane is able to land or not
                if (m_runwayfree) {
                    arriv_people += cur_passenger;
                    // get current time
                    airplane1.arr_time = Simulator.getCurrentTime();
                    // set the print format
                    DecimalFormat format = new DecimalFormat("0.00");
                    System.out.println(format.format(Simulator.getCurrentTime()) + ":Plane " + airplane1.getName() + " " +
                            airplane1.getM_number()+ " arrived at airport " + this.getName());
                    m_runwayfree = false;
                    // if runway is free put next event in the eventqueue
                    AirportEvent landingEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, airplane1, port, cur_passenger);
                    eventqueue.add(landingEvent);
                } else {
                    // if runway is not free put this event in the queue and wait
                    AirportEvent circlingEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_ARRIVES, airplane1, port, cur_passenger);
                    eventqueue.add(circlingEvent);
                }
                break;
            case AirportEvent.PLANE_LANDED:
                // landed event occurs
                m_runwayfree = true;
                DecimalFormat format1 = new DecimalFormat("0.00");
                System.out.println(format1.format(Simulator.getCurrentTime()) + ": Plane " + airplane1.getName() + " " +
                        airplane1.getM_number() + " lands at airport " + this.getName());
                // put next event in the queue
                AirportEvent departureEvent = new AirportEvent(m_requiredTimeOnGround, this, AirportEvent.TAKE_OFF, airplane1, port, cur_passenger);
                eventqueue.add(departureEvent);
                break;
            case AirportEvent.TAKE_OFF:
                if (m_runwayfree) {
                    // if runway is free this plane will take off
                    m_runwayfree = false;
                    Random rand = new Random();
                    cur_passenger = rand.nextInt(passenger1 + 1);
                    DecimalFormat format = new DecimalFormat("0.00");
                    System.out.println(format.format(Simulator.getCurrentTime()) + ": Plane " + airplane1.getName() + " "
                            + airplane1.getM_number() + " take off from airport " + this.getName());
                    // put next event in the queue
                    AirportEvent flyEvent = new AirportEvent(m_takeoffTime, this, AirportEvent.PLANE_DEPARTS, airplane1, port, cur_passenger);
                    eventqueue.add(flyEvent);
                } else {
                    // if runway is not free put this event in the queue and wait
                    AirportEvent departEvent = new AirportEvent(m_takeoffTime, this, AirportEvent.TAKE_OFF, airplane1, port, cur_passenger);
                    eventqueue.add(departEvent);
                }
                break;
            case AirportEvent.PLANE_DEPARTS:
                // departure event occur
                m_runwayfree = true;
                depart_people += cur_passenger;
                Random rand = new Random();
                int next_port_num = rand.nextInt(5);
                // make sure this plane will not depart and arrive at the same airport
                while (next_port_num == this.number) {
                    // generate next airport
                    next_port_num = rand.nextInt(5);
                }
                DecimalFormat format2 = new DecimalFormat("0.00");
                System.out.println(format2.format(Simulator.getCurrentTime()) + ": Plane " + airplane1.getName() + " " +
                        airplane1.getM_number() + " departs from airport to " + port[next_port_num].getName());
                AirportEvent departevent = new AirportEvent(m_flightTime[next_port_num] / airplane1.get_speed() * 60,
                        port[next_port_num], AirportEvent.PLANE_ARRIVES, airplane1, port, cur_passenger);
                // put next event in the queue
                eventqueue.add(departevent);
                break;
        }
        // pull next event in the queue
        AirportEvent next_event = eventqueue.poll();
        Simulator.schedule(next_event);
        // calculate the circling time
        if(next_event.getType() == AirportEvent.PLANE_ARRIVES && !m_runwayfree){
            current = Simulator.getCurrentTime();
            circling_time +=  current - airplane1.arr_time - m_runwayTimeToLand;
        }
    }
}

