package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ProductoTipoProductoDAOTest {
    @Test
    public void testConstructor() {
        ProductoTipoProductoDAO ptpDAO = new ProductoTipoProductoDAO();
        assertNotNull(ptpDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        ProductoTipoProductoDAO ptpDAO = new ProductoTipoProductoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = ProductoTipoProductoDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(ptpDAO, mockEM);

        assertNotNull(ptpDAO.getEntityManager());
        assertEquals(mockEM, ptpDAO.getEntityManager());
    }

}