package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Almacen;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;

import static org.junit.Assert.*;

public class TipoAlmacenDAOTest {
    @Test
    public void testConstructor() {
        TipoAlmacenDAO tipoAlmacenDAO = new TipoAlmacenDAO();
        assertNotNull(tipoAlmacenDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        TipoAlmacenDAO tipoAlmacenDAO = new TipoAlmacenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = TipoAlmacenDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(tipoAlmacenDAO, mockEM);

        assertNotNull(tipoAlmacenDAO.getEntityManager());
        assertEquals(mockEM, tipoAlmacenDAO.getEntityManager());
    }


    @Test
    public void getTipoAlmacenPorAlmacen() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        CriteriaBuilder mockCb = Mockito.mock(CriteriaBuilder.class);
        Mockito.when(mockEM.getCriteriaBuilder()).thenReturn(mockCb);
        CriteriaQuery<TipoAlmacen> mockCq = Mockito.mock(CriteriaQuery.class);
        Mockito.when(mockCb.createQuery(TipoAlmacen.class)).thenReturn(mockCq);
        Root<Almacen> mockRoot = Mockito.mock(Root.class);
        Mockito.when(mockCq.from(Almacen.class)).thenReturn(mockRoot);
        TypedQuery<TipoAlmacen> mockQuery = Mockito.mock(TypedQuery.class);

        TipoAlmacen objEsperado = new TipoAlmacen();

        Mockito.when(mockCq.select(Mockito.any())).thenReturn(mockCq);
        Mockito.when(mockCq.where(Mockito.<Predicate>any())).thenReturn(mockCq);

        Mockito.when(mockEM.createQuery(mockCq)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(objEsperado);

        TipoAlmacenDAO tipoAlmacenDAO = new TipoAlmacenDAO();
        TipoAlmacenDAO spyDao = Mockito.spy(tipoAlmacenDAO);
        Mockito.doReturn(mockEM).when(spyDao).getEntityManager();

        Almacen almacen = new Almacen();
        almacen.setId(10);

        TipoAlmacen resultado = spyDao.getTipoAlmacenPorAlmacen(almacen);

        assertNotNull(resultado);
        assertEquals(objEsperado, resultado);
        Mockito.verify(mockEM).getCriteriaBuilder();
        Mockito.verify(mockCb).createQuery(TipoAlmacen.class);
        Mockito.verify(mockCq).from(Almacen.class);
        Mockito.verify(mockCq).select(Mockito.any());
        Mockito.verify(mockCq).where(Mockito.<Predicate>any());
        Mockito.verify(mockEM).createQuery(mockCq);
        Mockito.verify(mockQuery).getSingleResult();
    }

    @Test
    public void getTipoAlmacenPorAlmacenTestAlmacenNull() {
        TipoAlmacenDAO tipoAlmacenDAO = new TipoAlmacenDAO();
        assertNull(tipoAlmacenDAO.getTipoAlmacenPorAlmacen(null));
    }
}