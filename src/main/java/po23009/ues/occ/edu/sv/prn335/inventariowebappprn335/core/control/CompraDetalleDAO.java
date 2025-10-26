package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;

import java.io.Serializable;
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

    public List<CompraDetalle> getByIdCompra(Compra compra) {
        if(compra != null) {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<CompraDetalle> cq = cb.createQuery(entityClass);
            Root<CompraDetalle> root = cq.from(entityClass);
            cq.select(root);

            TypedQuery<CompraDetalle> tq = em.createQuery(cq);

            Predicate predicadoId = cb.equal(root.get("idCompra"), compra.getId());

            cq.where(predicadoId);

            return tq.getResultList();
        }

        return List.of();
    }
}