package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TipoProductoCaracteristicaDAOTest {
    @Test
    public void testConstructor() {
        TipoProductoCaracteristicaDAO tpcDAO = new TipoProductoCaracteristicaDAO();
        assertNotNull(tpcDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        TipoProductoCaracteristicaDAO tpcDAO = new TipoProductoCaracteristicaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = TipoProductoCaracteristicaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(tpcDAO, mockEM);

        assertNotNull(tpcDAO.getEntityManager());
        assertEquals(mockEM, tpcDAO.getEntityManager());
    }


    @Test
    public void findByTipoProductoTest() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        CriteriaBuilder mockCB = Mockito.mock(CriteriaBuilder.class);
        Mockito.when(mockEM.getCriteriaBuilder()).thenReturn(mockCB);
        CriteriaQuery<TipoProductoCaracteristica> mockCQ = Mockito.mock(CriteriaQuery.class);
        Mockito.when(mockCB.createQuery(TipoProductoCaracteristica.class)).thenReturn(mockCQ);
        Root<TipoProductoCaracteristica> mockRoot = Mockito.mock(Root.class);
        Mockito.when(mockCQ.from(TipoProductoCaracteristica.class)).thenReturn(mockRoot);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<TipoProductoCaracteristica> mockQuery = Mockito.mock(TypedQuery.class);

        List<TipoProductoCaracteristica> listaEsperada = new ArrayList<>();
        listaEsperada.add(new TipoProductoCaracteristica());

        Path pathTipoProducto = Mockito.mock(Path.class);
        Path pathId = Mockito.mock(Path.class);

        Mockito.when(mockRoot.get("idTipoProducto")).thenReturn(pathTipoProducto);
        Mockito.when(pathTipoProducto.get("id")).thenReturn(pathId);

        Mockito.when(mockCB.equal(pathId, 10L)).thenReturn(predicate);
        Mockito.when(mockCQ.select(mockRoot)).thenReturn(mockCQ);
        Mockito.when(mockCQ.where(predicate)).thenReturn(mockCQ);

        Mockito.when(mockEM.createQuery(mockCQ)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(listaEsperada);

        TipoProductoCaracteristicaDAO dao = Mockito.spy(new TipoProductoCaracteristicaDAO());
        Mockito.doReturn(mockEM).when(dao).getEntityManager();

        List<TipoProductoCaracteristica> resultado = dao.findByTipoProducto(10L);

        assertNotNull(resultado);
        assertEquals(listaEsperada, resultado);

        Mockito.verify(mockEM).getCriteriaBuilder();
        Mockito.verify(mockCB).createQuery(TipoProductoCaracteristica.class);
        Mockito.verify(mockRoot).get("idTipoProducto");
        Mockito.verify(mockCQ).select(mockRoot);
        Mockito.verify(mockCQ).where(predicate);
        Mockito.verify(mockQuery).getResultList();
    }


    @Test
    public void findByTipoProductoTestListaVacia() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        CriteriaBuilder mockCB = Mockito.mock(CriteriaBuilder.class);
        Mockito.when(mockEM.getCriteriaBuilder()).thenReturn(mockCB);
        CriteriaQuery<TipoProductoCaracteristica> mockCQ = Mockito.mock(CriteriaQuery.class);
        Mockito.when(mockCB.createQuery(TipoProductoCaracteristica.class)).thenReturn(mockCQ);
        Root<TipoProductoCaracteristica> mockRoot = Mockito.mock(Root.class);
        Mockito.when(mockCQ.from(TipoProductoCaracteristica.class)).thenReturn(mockRoot);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<TipoProductoCaracteristica> mockQuery = Mockito.mock(TypedQuery.class);

        List<TipoProductoCaracteristica> listaVacia = new ArrayList<>();

        Path pathTipoProducto = Mockito.mock(Path.class);
        Path pathId = Mockito.mock(Path.class);

        Mockito.when(mockRoot.get("idTipoProducto")).thenReturn(pathTipoProducto);
        Mockito.when(pathTipoProducto.get("id")).thenReturn(pathId);
        Mockito.when(mockCB.equal(pathId, 5L)).thenReturn(predicate);

        Mockito.when(mockCQ.select(mockRoot)).thenReturn(mockCQ);
        Mockito.when(mockCQ.where(predicate)).thenReturn(mockCQ);

        Mockito.when(mockEM.createQuery(mockCQ)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(listaVacia);

        TipoProductoCaracteristicaDAO dao = Mockito.spy(new TipoProductoCaracteristicaDAO());
        Mockito.doReturn(mockEM).when(dao).getEntityManager();

        List<TipoProductoCaracteristica> resultado = dao.findByTipoProducto(5L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

}