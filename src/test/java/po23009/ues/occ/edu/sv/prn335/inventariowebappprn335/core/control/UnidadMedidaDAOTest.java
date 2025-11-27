package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.UnidadMedida;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class UnidadMedidaDAOTest {
    @Test
    public void testConstructor() {
        UnidadMedidaDAO unidadMedidaDAO = new UnidadMedidaDAO();
        assertNotNull(unidadMedidaDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        UnidadMedidaDAO unidadMedidaDAO = new UnidadMedidaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = UnidadMedidaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(unidadMedidaDAO, mockEM);

        assertNotNull(unidadMedidaDAO.getEntityManager());
        assertEquals(mockEM, unidadMedidaDAO.getEntityManager());
    }

    @Test
    public void testFindByTipo_variosResultados() {
        Integer idTipo = 1;
        UnidadMedida u1 = new UnidadMedida();
        UnidadMedida u2 = new UnidadMedida();
        List<UnidadMedida> expectedList = Arrays.asList(u1, u2);

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<UnidadMedida> cq = Mockito.mock(CriteriaQuery.class);
        Root<UnidadMedida> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<UnidadMedida> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(UnidadMedida.class)).thenReturn(cq);
        Mockito.when(cq.from(UnidadMedida.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idTipoUnidadMedida").get("id"), idTipo)).thenReturn(predicate);
        Mockito.when(cq.select(root)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedList);

        UnidadMedidaDAO dao = new UnidadMedidaDAO();
        dao.em = em;

        List<UnidadMedida> result = dao.findByTipo(idTipo);

        assertEquals(expectedList, result);
        Mockito.verify(typedQuery).getResultList();
    }

    @Test
    public void testFindByTipo_listaVacia() {
        Integer idTipo = 1;
        List<UnidadMedida> expectedList = Collections.emptyList();

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<UnidadMedida> cq = Mockito.mock(CriteriaQuery.class);
        Root<UnidadMedida> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<UnidadMedida> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(UnidadMedida.class)).thenReturn(cq);
        Mockito.when(cq.from(UnidadMedida.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idTipoUnidadMedida").get("id"), idTipo)).thenReturn(predicate);
        Mockito.when(cq.select(root)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedList);

        UnidadMedidaDAO dao = new UnidadMedidaDAO();
        dao.em = em;

        List<UnidadMedida> result = dao.findByTipo(idTipo);

        assertEquals(expectedList, result);
        Mockito.verify(typedQuery).getResultList();
    }

    @Test
    public void testFindByTipo_idTipoNull() {
        Integer idTipo = null;
        List<UnidadMedida> expectedList = Collections.emptyList();

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<UnidadMedida> cq = Mockito.mock(CriteriaQuery.class);
        Root<UnidadMedida> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<UnidadMedida> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(UnidadMedida.class)).thenReturn(cq);
        Mockito.when(cq.from(UnidadMedida.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idTipoUnidadMedida").get("id"), idTipo)).thenReturn(predicate);
        Mockito.when(cq.select(root)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedList);

        UnidadMedidaDAO dao = new UnidadMedidaDAO();
        dao.em = em;

        List<UnidadMedida> result = dao.findByTipo(idTipo);

        assertEquals(expectedList, result);
        Mockito.verify(typedQuery).getResultList();
    }
}