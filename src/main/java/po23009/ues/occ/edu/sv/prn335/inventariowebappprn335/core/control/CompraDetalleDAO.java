package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class CompraDetalleDAO extends InventarioDefaultDataAccess<CompraDetalle, UUID> implements Serializable {

    public CompraDetalleDAO() { super(CompraDetalle.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CompraDetalle> findByCompra(Long idCompra) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CompraDetalle> cq = cb.createQuery(CompraDetalle.class);
        Root<CompraDetalle> root = cq.from(CompraDetalle.class);
        Predicate predicate = cb.equal(root.get("idCompra").get("id"), idCompra);
        cq.select(root).where(predicate);

        return em.createQuery(cq).getResultList();
    }

    public BigDecimal calcularMontoByCompra(Long idcompra) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<CompraDetalle> detalle = cq.from(CompraDetalle.class);
        Expression<BigDecimal> subtotal = cb.prod(detalle.get("cantidad"), detalle.get("precio"));

        cq.select(cb.sum(subtotal));
        cq.where(cb.equal(detalle.get("idCompra").get("id"), idcompra));
        BigDecimal resultado = em.createQuery(cq).getSingleResult();

        return resultado!=null?resultado:BigDecimal.ZERO;
    }

}