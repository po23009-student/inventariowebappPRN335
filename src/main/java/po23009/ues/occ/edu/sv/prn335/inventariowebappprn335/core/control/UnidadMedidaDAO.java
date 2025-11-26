package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.UnidadMedida;

import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class UnidadMedidaDAO extends InventarioDefaultDataAccess<UnidadMedida, Integer> implements Serializable {

    public UnidadMedidaDAO() { super(UnidadMedida.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<UnidadMedida> findByTipo(Integer idTipoUnidadMedida) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UnidadMedida> cq = cb.createQuery(UnidadMedida.class);
        Root<UnidadMedida> unidad = cq.from(UnidadMedida.class);
        cq.select(unidad).where(cb.equal(unidad.get("idTipoUnidadMedida").get("id"), idTipoUnidadMedida));
        TypedQuery<UnidadMedida> query = em.createQuery(cq);

        return query.getResultList();
    }
}