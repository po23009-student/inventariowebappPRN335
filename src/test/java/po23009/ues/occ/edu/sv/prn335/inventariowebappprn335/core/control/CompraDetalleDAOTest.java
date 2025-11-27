package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class CompraDetalleDAOTest {
    @Test
    public void testConstructor() {
        CompraDetalleDAO compraDetalleDAO = new CompraDetalleDAO();
        assertNotNull(compraDetalleDAO);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        CompraDetalleDAO compraDetalleDAO = new CompraDetalleDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        var field = CompraDetalleDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(compraDetalleDAO, mockEM);

        assertNotNull(compraDetalleDAO.getEntityManager());
        assertEquals(mockEM, compraDetalleDAO.getEntityManager());
    }

    @Test
    public void testFindByCompra() {
        Long idCompra = 1L;
        CompraDetalle detalle1 = new CompraDetalle();
        CompraDetalle detalle2 = new CompraDetalle();
        List<CompraDetalle> expectedList = Arrays.asList(detalle1, detalle2);

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<CompraDetalle> cq = Mockito.mock(CriteriaQuery.class);
        Root<CompraDetalle> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<CompraDetalle> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(CompraDetalle.class)).thenReturn(cq);
        Mockito.when(cq.from(CompraDetalle.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idCompra").get("id"), idCompra)).thenReturn(predicate);
        Mockito.when(cq.select(root)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedList);

        CompraDetalleDAO compraDetalleDAO = new CompraDetalleDAO();
        compraDetalleDAO.em = em;

        List<CompraDetalle> result = compraDetalleDAO.findByCompra(idCompra);

        assertEquals(expectedList, result);
        Mockito.verify(typedQuery).getResultList();
    }

    @Test
    public void testCalcularMontoByCompra_variosDetalles() {
        Long idCompra = 1L;
        BigDecimal expectedTotal = new BigDecimal("65");

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<BigDecimal> cq = Mockito.mock(CriteriaQuery.class);
        Root<CompraDetalle> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Expression<BigDecimal> subtotalExpr = Mockito.mock(Expression.class);
        Expression<BigDecimal> sumExpr = Mockito.mock(Expression.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<BigDecimal> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(BigDecimal.class)).thenReturn(cq);
        Mockito.when(cq.from(CompraDetalle.class)).thenReturn(root);

        Mockito.when(cb.sum(subtotalExpr)).thenReturn(sumExpr);

        Mockito.when(root.get("cantidad")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(root.get("precio")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(cb.equal(root.get("idCompra").get("id"), idCompra)).thenReturn(predicate);

        Mockito.when(cq.select(sumExpr)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(expectedTotal);

        CompraDetalleDAO compraDetalleDAO = new CompraDetalleDAO();
        compraDetalleDAO.em = em;

        BigDecimal result = compraDetalleDAO.calcularMontoByCompra(idCompra);

        assertEquals(expectedTotal, result);
        Mockito.verify(typedQuery).getSingleResult();
    }





}