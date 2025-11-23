package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CaracteristicaDAOTest {
    @Test
    public void testConstructor() {
        CaracteristicaDAO caracteristicaDAO = new CaracteristicaDAO();
        assertNotNull(caracteristicaDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        CaracteristicaDAO caracteristicaDAO = new CaracteristicaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = CaracteristicaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(caracteristicaDAO, mockEM);

        assertNotNull(caracteristicaDAO.getEntityManager());
        assertEquals(mockEM, caracteristicaDAO.getEntityManager());
    }

}