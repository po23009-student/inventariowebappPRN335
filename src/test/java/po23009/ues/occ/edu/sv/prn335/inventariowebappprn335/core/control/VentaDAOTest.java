package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class VentaDAOTest {
    @Test
    public void testConstructor() {
        VentaDAO ventaDAO = new VentaDAO();
        assertNotNull(ventaDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        VentaDAO ventaDAO = new VentaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = VentaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(ventaDAO, mockEM);

        assertNotNull(ventaDAO.getEntityManager());
        assertEquals(mockEM, ventaDAO.getEntityManager());
    }
}