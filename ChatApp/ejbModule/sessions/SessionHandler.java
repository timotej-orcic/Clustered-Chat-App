package sessions;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.jms.Session;
import javax.json.JsonObject;

//Rukovanje sesijama koje se konektuju na server
@ApplicationScoped
public class SessionHandler {
	private final Set<Session> sessions = new HashSet<>();
    //private final Set<Device> devices = new HashSet()<>();
	
	/*public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }*/
	
	public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void add() {
    }

    public void remove(int id) {
    }

    public void toggleDevice(int id) {
    }

    private Object getDeviceById(int id) {
        return null;
    }

    private Object createAddMessage() {
        return null;
    }

    private void sendToAllConnectedSessions(JsonObject message) {
    }

    private void sendToSession(Session session, JsonObject message) {
    }
}
