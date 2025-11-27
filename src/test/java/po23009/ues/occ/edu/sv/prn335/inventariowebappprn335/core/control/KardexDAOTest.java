package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Kardex;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class KardexDAOTest {
    @Test
    public void testContarKardexPorProducto() {
        UUID idProducto = UUID.randomUUID();
        long expectedCount = 5L;

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> cq = Mockito.mock(CriteriaQuery.class);
        Root<Kardex> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<Long> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(Long.class)).thenReturn(cq);
        Mockito.when(cq.from(Kardex.class)).thenReturn(root);
        Mockito.when(cb.equal(root.get("idProducto").get("id"), idProducto)).thenReturn(predicate);
        Mockito.when(cq.select(cb.count(root))).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(expectedCount);

        KardexDAO dao = new KardexDAO();
        dao.em = em;

        long result = dao.contarKardexPorProducto(idProducto);

        assertEquals(expectedCount, result);
        Mockito.verify(typedQuery).getSingleResult();
    }

    @Test
    public void testCalcularPrecioActual_variosKardex() {
        UUID idProducto = UUID.randomUUID();
        BigDecimal expectedTotal = new BigDecimal("150.50");

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<BigDecimal> cq = Mockito.mock(CriteriaQuery.class);
        Root<Kardex> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Expression<BigDecimal> subtotalExpr = Mockito.mock(Expression.class);
        Expression<BigDecimal> sumExpr = Mockito.mock(Expression.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<BigDecimal> typedQuery = Mockito.mock(TypedQuery.class);

        // Mocks
        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(BigDecimal.class)).thenReturn(cq);
        Mockito.when(cq.from(Kardex.class)).thenReturn(root);

        Mockito.when(cb.sum(subtotalExpr)).thenReturn(sumExpr);

        Mockito.when(root.get("cantidad")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(root.get("precio")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(cb.equal(root.get("idProducto").get("id"), idProducto)).thenReturn(predicate);

        Mockito.when(cq.select(sumExpr)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(expectedTotal);

        KardexDAO dao = new KardexDAO();
        dao.em = em;

        BigDecimal result = dao.calcularPrecioActual(idProducto);

        assertEquals(expectedTotal, result);
        Mockito.verify(typedQuery).getSingleResult();
    }

    @Test
    public void testCalcularPrecioActual_sinKardex() {
        UUID idProducto = UUID.randomUUID();
        BigDecimal expectedTotal = BigDecimal.ZERO;

        EntityManager em = Mockito.mock(EntityManager.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<BigDecimal> cq = Mockito.mock(CriteriaQuery.class);
        Root<Kardex> root = Mockito.mock(Root.class, RETURNS_DEEP_STUBS);
        Expression<BigDecimal> subtotalExpr = Mockito.mock(Expression.class);
        Expression<BigDecimal> sumExpr = Mockito.mock(Expression.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        TypedQuery<BigDecimal> typedQuery = Mockito.mock(TypedQuery.class);

        // Mocks
        Mockito.when(em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(BigDecimal.class)).thenReturn(cq);
        Mockito.when(cq.from(Kardex.class)).thenReturn(root);

        Mockito.when(cb.sum(subtotalExpr)).thenReturn(sumExpr);

        Mockito.when(root.get("cantidad")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(root.get("precio")).thenReturn(Mockito.mock(Path.class));
        Mockito.when(cb.equal(root.get("idProducto").get("id"), idProducto)).thenReturn(predicate);

        Mockito.when(cq.select(sumExpr)).thenReturn(cq);
        Mockito.when(cq.where(predicate)).thenReturn(cq);
        Mockito.when(em.createQuery(cq)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(null);

        KardexDAO dao = new KardexDAO();
        dao.em = em;

        BigDecimal result = dao.calcularPrecioActual(idProducto);

        assertEquals(expectedTotal, result);
        Mockito.verify(typedQuery).getSingleResult();
    }
}