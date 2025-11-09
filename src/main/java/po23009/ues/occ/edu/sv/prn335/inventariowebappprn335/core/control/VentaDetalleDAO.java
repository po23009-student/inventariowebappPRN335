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
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.VentaDetalle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class VentaDetalleDAO extends InventarioDefaultDataAccess<VentaDetalle> implements Serializable {

    public VentaDetalleDAO() { super(VentaDetalle.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<VentaDetalle> findByVenta(UUID idVenta) {
        TypedQuery<VentaDetalle> q = em.createQuery(
                "SELECT vd FROM VentaDetalle vd WHERE vd.idVenta.id = :idVenta", VentaDetalle.class);
        q.setParameter("idVenta", idVenta);
        return q.getResultList();
    }

    public BigDecimal calcularMontoByVenta(UUID idVenta) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<VentaDetalle> detalle = cq.from(VentaDetalle.class);
        Expression<BigDecimal> subtotal = cb.prod(detalle.get("cantidad"), detalle.get("precio"));

        cq.select(cb.sum(subtotal));
        cq.where(cb.equal(detalle.get("idVenta").get("id"), idVenta));
        BigDecimal resultado = em.createQuery(cq).getSingleResult();

        return resultado!=null?resultado:BigDecimal.ZERO;
    }

}
