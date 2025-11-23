package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CompraDAOTest {
    @Test
    public void testConstructor() {
        CompraDAO compraDAO = new CompraDAO();
        assertNotNull(compraDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        CompraDAO compraDAO = new CompraDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = CompraDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(compraDAO, mockEM);

        assertNotNull(compraDAO.getEntityManager());
        assertEquals(mockEM, compraDAO.getEntityManager());
    }
}