package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class KardexDetalleDAOTest {
    @Test
    public void testConstructor() {
        KardexDetalleDAO kardexDetalleDAO = new KardexDetalleDAO();
        assertNotNull(kardexDetalleDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        KardexDetalleDAO kardexDetalleDAO = new KardexDetalleDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = KardexDetalleDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(kardexDetalleDAO, mockEM);

        assertNotNull(kardexDetalleDAO.getEntityManager());
        assertEquals(mockEM, kardexDetalleDAO.getEntityManager());
    }
}