//Hansen Zhao

//TODO add number of passengers, speed

public class Airplane {
    private String m_name;
    private int m_numberPassengers;
    public double arr_time;
    // unit is mile/h
    private int m_speed;
    private int m_number;

    public Airplane(String name, double arrive_time, int capacity, int speed,int m_number) {
        // set the parameter for airplane
        m_name = name;
        arr_time = arrive_time;
        this.m_numberPassengers = capacity;
        this.m_speed = speed;
        this.m_number = m_number;
    }
    public int get_speed() {
        return m_speed;
    }
    public int getcapacity() {
        return m_numberPassengers;
    }
    public String getName() {
        return m_name;
    }
    public int getM_number() {return m_number;}

}
