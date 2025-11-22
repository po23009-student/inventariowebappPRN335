package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.ws;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.websocket.Session;

import java.util.HashSet;
import java.util.Set;

@Named
@ApplicationScoped
public class SessionHandler {
    final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public Set<Session> getSessions() {
        return sessions;
    }
}
