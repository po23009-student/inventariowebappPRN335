package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ProductoDAOTest {
    @Test
    public void testConstructor() {
        ProductoDAO productoDAO = new ProductoDAO();
        assertNotNull(productoDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        ProductoDAO productoDAO = new ProductoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = ProductoDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(productoDAO, mockEM);

        assertNotNull(productoDAO.getEntityManager());
        assertEquals(mockEM, productoDAO.getEntityManager());
    }
}