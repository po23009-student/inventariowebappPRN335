package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.VentaDetalle;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class VentaDetalleDAOTest {
    @Test
    public void testConstructor() {
        VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO();
        assertNotNull(ventaDetalleDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = VentaDetalleDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(ventaDetalleDAO, mockEM);

        assertNotNull(ventaDetalleDAO.getEntityManager());
        assertEquals(mockEM, ventaDetalleDAO.getEntityManager());
    }

    @Test
    public void testFindByVenta_variosResultados() {
        UUID idVenta = UUID.randomUUID();
        VentaDetalle v1 = new VentaDetalle();
        VentaDetalle v2 = new VentaDetalle();
        List<VentaDetalle> expectedList = Arrays.asList(v1, v2);

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<VentaDetalle> cq = Mockito.mock(CriteriaQuery.class);
        Root<VentaDetalle> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<VentaDetalle> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(VentaDetalle.class)).thenReturn(cq);
        Mockito.when(cq.from(VentaDetalle.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idVenta").get("id"), idVenta)).thenReturn(predicate);
        Mockito.when(cq.select(root)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedList);

        VentaDetalleDAO dao = new VentaDetalleDAO();
        dao.em = em;

        List<VentaDetalle> result = dao.findByVenta(idVenta);

        assertEquals(expectedList, result);
        Mockito.verify(typedQuery).getResultList();
    }

    @Test
    public void testFindByVenta_listaVacia() {
        UUID idVenta = UUID.randomUUID();
        List<VentaDetalle> expectedList = Collections.emptyList();

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<VentaDetalle> cq = Mockito.mock(CriteriaQuery.class);
        Root<VentaDetalle> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<VentaDetalle> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(VentaDetalle.class)).thenReturn(cq);
        Mockito.when(cq.from(VentaDetalle.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idVenta").get("id"), idVenta)).thenReturn(predicate);
        Mockito.when(cq.select(root)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedList);

        VentaDetalleDAO dao = new VentaDetalleDAO();
        dao.em = em;

        List<VentaDetalle> result = dao.findByVenta(idVenta);

        assertEquals(expectedList, result);
        Mockito.verify(typedQuery).getResultList();
    }
    
    @Test
    public void testCalcularMontoByVenta_variosDetalles() {
        UUID idVenta = UUID.randomUUID();
        BigDecimal expectedTotal = new BigDecimal("100");

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<BigDecimal> cq = Mockito.mock(CriteriaQuery.class);
        Root<VentaDetalle> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Expression<BigDecimal> subtotalExpr = Mockito.mock(Expression.class);
        Expression<BigDecimal> sumExpr = Mockito.mock(Expression.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<BigDecimal> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(BigDecimal.class)).thenReturn(cq);
        Mockito.when(cq.from(VentaDetalle.class)).thenReturn(root);
        Mockito.when(cb.sum(subtotalExpr)).thenReturn(sumExpr);
        Mockito.when(root.get("cantidad")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(root.get("precio")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(cb.equal(root.get("idVenta").get("id"), idVenta)).thenReturn(predicate);
        Mockito.when(cq.select(sumExpr)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(expectedTotal);

        VentaDetalleDAO dao = new VentaDetalleDAO();
        dao.em = em;

        BigDecimal result = dao.calcularMontoByVenta(idVenta);

        assertEquals(expectedTotal, result);
        Mockito.verify(typedQuery).getSingleResult();
    }

    @Test
    public void testCalcularMontoByVenta_sinDetalles() {
        UUID idVenta = UUID.randomUUID();
        BigDecimal expectedTotal = BigDecimal.ZERO;

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<BigDecimal> cq = Mockito.mock(CriteriaQuery.class);
        Root<VentaDetalle> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Expression<BigDecimal> subtotalExpr = Mockito.mock(Expression.class);
        Expression<BigDecimal> sumExpr = Mockito.mock(Expression.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<BigDecimal> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(BigDecimal.class)).thenReturn(cq);
        Mockito.when(cq.from(VentaDetalle.class)).thenReturn(root);
        Mockito.when(cb.sum(subtotalExpr)).thenReturn(sumExpr);
        Mockito.when(root.get("cantidad")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(root.get("precio")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(cb.equal(root.get("idVenta").get("id"), idVenta)).thenReturn(predicate);
        Mockito.when(cq.select(sumExpr)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(null);

        VentaDetalleDAO dao = new VentaDetalleDAO();
        dao.em = em;

        BigDecimal result = dao.calcularMontoByVenta(idVenta);

        assertEquals(expectedTotal, result);
        Mockito.verify(typedQuery).getSingleResult();
    }
}