package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class TipoUnidadMedidaDAOTest {
    @Test
    public void testConstructor() {
        TipoUnidadMedidaDAO tiipoUnidadMedidaDAO = new TipoUnidadMedidaDAO();
        assertNotNull(tiipoUnidadMedidaDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        TipoUnidadMedidaDAO tiipoUnidadMedidaDAO = new TipoUnidadMedidaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = TipoUnidadMedidaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(tiipoUnidadMedidaDAO, mockEM);

        assertNotNull(tiipoUnidadMedidaDAO.getEntityManager());
        assertEquals(mockEM, tiipoUnidadMedidaDAO.getEntityManager());
    }

    @Test
    public void testGetTipoUnidadMedidaPorCaracteristica_caracteristicaNull() {
        TipoUnidadMedidaDAO dao = new TipoUnidadMedidaDAO();
        dao.em = Mockito.mock(EntityManager.class);

        TipoUnidadMedida result = dao.getTipoUnidadMedidaPorCaracteristica(null);

        assertNull(result);
    }

    @Test
    public void testGetTipoUnidadMedidaPorCaracteristica_idNull() {
        TipoUnidadMedidaDAO dao = new TipoUnidadMedidaDAO();
        dao.em = Mockito.mock(EntityManager.class);

        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setId(null);

        TipoUnidadMedida result = dao.getTipoUnidadMedidaPorCaracteristica(caracteristica);

        assertNull(result);
    }

    @Test
    public void testGetTipoUnidadMedidaPorCaracteristica_noResultados() {
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setId(1);

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<TipoUnidadMedida> cq = Mockito.mock(CriteriaQuery.class);
        Root<Caracteristica> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<TipoUnidadMedida> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(TipoUnidadMedida.class)).thenReturn(cq);
        Mockito.when(cq.from(Caracteristica.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("id"), caracteristica.getId())).thenReturn(predicate);
        Mockito.when(cq.select(root.get("idTipoUnidadMedida"))).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        TipoUnidadMedidaDAO dao = new TipoUnidadMedidaDAO();
        dao.em = em;

        TipoUnidadMedida result = dao.getTipoUnidadMedidaPorCaracteristica(caracteristica);

        assertNull(result);
        Mockito.verify(typedQuery).getResultList();
    }

    @Test
    public void testGetTipoUnidadMedidaPorCaracteristica_unResultado() {
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setId(1);

        TipoUnidadMedida tipo = new TipoUnidadMedida();

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<TipoUnidadMedida> cq = Mockito.mock(CriteriaQuery.class);
        Root<Caracteristica> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<TipoUnidadMedida> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(TipoUnidadMedida.class)).thenReturn(cq);
        Mockito.when(cq.from(Caracteristica.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("id"), caracteristica.getId())).thenReturn(predicate);
        Mockito.when(cq.select(root.get("idTipoUnidadMedida"))).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(List.of(tipo));

        TipoUnidadMedidaDAO dao = new TipoUnidadMedidaDAO();
        dao.em = em;

        TipoUnidadMedida result = dao.getTipoUnidadMedidaPorCaracteristica(caracteristica);

        assertEquals(tipo, result);
        Mockito.verify(typedQuery).getResultList();
    }

    @Test
    public void testGetTipoUnidadMedidaPorCaracteristica_variosResultados() {
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setId(1);

        TipoUnidadMedida tipo1 = new TipoUnidadMedida();
        TipoUnidadMedida tipo2 = new TipoUnidadMedida();

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<TipoUnidadMedida> cq = Mockito.mock(CriteriaQuery.class);
        Root<Caracteristica> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<TipoUnidadMedida> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(TipoUnidadMedida.class)).thenReturn(cq);
        Mockito.when(cq.from(Caracteristica.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("id"), caracteristica.getId())).thenReturn(predicate);
        Mockito.when(cq.select(root.get("idTipoUnidadMedida"))).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(List.of(tipo1, tipo2));

        TipoUnidadMedidaDAO dao = new TipoUnidadMedidaDAO();
        dao.em = em;

        TipoUnidadMedida result = dao.getTipoUnidadMedidaPorCaracteristica(caracteristica);

        assertEquals(tipo1, result); // Siempre devuelve el primer elemento
        Mockito.verify(typedQuery).getResultList();
    }
}