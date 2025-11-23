package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Cliente;

import static org.junit.Assert.*;

public class ClienteDAOTest {

    @Test
    public void testConstructor() {
        ClienteDAO clienteDAO = new ClienteDAO();
        assertNotNull(clienteDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        ClienteDAO clienteDAO = new ClienteDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = ClienteDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(clienteDAO, mockEM);

        assertNotNull(clienteDAO.getEntityManager());
        assertEquals(mockEM, clienteDAO.getEntityManager());
    }

}