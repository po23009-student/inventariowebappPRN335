package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ProductoTipoProductoCaracteristicaDAOTest {
    @Test
    public void testConstructor() {
        ProductoTipoProductoCaracteristicaDAO ptpcDAO = new ProductoTipoProductoCaracteristicaDAO();
        assertNotNull(ptpcDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        ProductoTipoProductoCaracteristicaDAO ptpcDAO = new ProductoTipoProductoCaracteristicaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = ProductoTipoProductoCaracteristicaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(ptpcDAO, mockEM);

        assertNotNull(ptpcDAO.getEntityManager());
        assertEquals(mockEM, ptpcDAO.getEntityManager());
    }

}