package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Almacen;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;

import java.io.Serializable;

@Stateless
@LocalBean
public class TipoAlmacenDAO extends InventarioDefaultDataAccess<TipoAlmacen, Integer> implements Serializable {

    public TipoAlmacenDAO() { super(TipoAlmacen.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TipoAlmacen getTipoAlmacenPorAlmacen(Almacen almacen) {
        if (almacen == null || almacen.getId() == null) {
            return null;
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TipoAlmacen> cq = cb.createQuery(TipoAlmacen.class);
        Root<Almacen> almacenRoot = cq.from(Almacen.class);
        cq.select(almacenRoot.get("idTipoAlmacen")).where(cb.equal(almacenRoot.get("id"), almacen.getId()));
        TypedQuery<TipoAlmacen> query = getEntityManager().createQuery(cq);

        return query.getSingleResult();
    }

}