package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class TipoProductoCaracteristicaDAO extends InventarioDefaultDataAccess<TipoProductoCaracteristica, Long> implements Serializable {

    public TipoProductoCaracteristicaDAO() {
        super(TipoProductoCaracteristica.class);
    }

    @PersistenceContext(unitName = "inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<TipoProductoCaracteristica> findByTipoProducto(Long idTipoProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoProductoCaracteristica> cq = cb.createQuery(TipoProductoCaracteristica.class);
        Root<TipoProductoCaracteristica> root = cq.from(TipoProductoCaracteristica.class);
        Predicate predicate = cb.equal(root.get("idTipoProducto").get("id"), idTipoProducto);
        cq.select(root).where(predicate);

        return em.createQuery(cq).getResultList();
    }
}