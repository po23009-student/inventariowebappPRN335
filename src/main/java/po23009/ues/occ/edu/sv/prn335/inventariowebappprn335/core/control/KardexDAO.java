package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;


import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Kardex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Stateless
@LocalBean
public class KardexDAO extends InventarioDefaultDataAccess<Kardex, UUID> implements Serializable {

    public KardexDAO() { super(Kardex.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public long contarKardexPorProducto(UUID idProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Kardex> root = cq.from(Kardex.class);
        Predicate predicate = cb.equal(root.get("idProducto").get("id"), idProducto);

        cq.select(cb.count(root)).where(predicate);

        return em.createQuery(cq).getSingleResult();
    }

    public BigDecimal calcularPrecioActual(UUID idProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<Kardex> kardexRoot = cq.from(Kardex.class);
        Expression<BigDecimal> subtotal = cb.prod(kardexRoot.get("cantidad"), kardexRoot.get("precio"));

        cq.select(cb.sum(subtotal));
        cq.where(cb.equal(kardexRoot.get("idProducto").get("id"), idProducto));
        BigDecimal resultado = em.createQuery(cq).getSingleResult();

        return resultado!=null?resultado:BigDecimal.ZERO;
    }

}
