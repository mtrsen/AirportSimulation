//Hansen Zhao

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    public static final int TAKE_OFF = 3;
    public static Airport [] port_matrix;
    public Airplane current_plane;
    public int passengers;

    public AirportEvent(double delay, EventHandler handler, int eventType, Airplane curr_plane, Airport [] port_matrix, int curr_pass) {
        super(delay, handler, eventType);
        // set the parameter for airport event
        current_plane = curr_plane;
        this.port_matrix = port_matrix;
        passengers = curr_pass;

    }
}