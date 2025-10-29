package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Stateless
@LocalBean
public class CompraDetalleDAO extends InventarioDefaultDataAccess<CompraDetalle> implements Serializable {

    public CompraDetalleDAO() { super(CompraDetalle.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CompraDetalle> findByCompra(Long idCompra) {
        TypedQuery<CompraDetalle> q = em.createQuery(
                "SELECT cd FROM CompraDetalle cd WHERE cd.idCompra.id = :idCompra", CompraDetalle.class);
        q.setParameter("idCompra", idCompra);
        return q.getResultList();
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