package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class AlmacenDAOTest {
    @Test
    public void testConstructor() {
        AlmacenDAO almacenDAO = new AlmacenDAO();
        assertNotNull(almacenDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        AlmacenDAO almacenDAO = new AlmacenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = AlmacenDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(almacenDAO, mockEM);

        assertNotNull(almacenDAO.getEntityManager());
        assertEquals(mockEM, almacenDAO.getEntityManager());
    }


}