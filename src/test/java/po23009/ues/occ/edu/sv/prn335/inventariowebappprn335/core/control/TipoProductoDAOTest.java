package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class TipoProductoDAOTest {

    @Test
    public void testConstructor() {
        TipoProductoDAO tipoProductoDAO = new TipoProductoDAO();
        assertNotNull(tipoProductoDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        TipoProductoDAO tipoProductoDAO = new TipoProductoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = TipoProductoDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(tipoProductoDAO, mockEM);

        assertNotNull(tipoProductoDAO.getEntityManager());
        assertEquals(mockEM, tipoProductoDAO.getEntityManager());
    }

}