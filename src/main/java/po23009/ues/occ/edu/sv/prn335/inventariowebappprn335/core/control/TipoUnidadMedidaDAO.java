package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class TipoUnidadMedidaDAO extends InventarioDefaultDataAccess<TipoUnidadMedida, Integer> implements Serializable {

    public TipoUnidadMedidaDAO() { super(TipoUnidadMedida.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TipoUnidadMedida getTipoUnidadMedidaPorCaracteristica(Caracteristica caracteristica) {
        if (caracteristica == null || caracteristica.getId() == null) {
            return null;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoUnidadMedida> cq = cb.createQuery(TipoUnidadMedida.class);
        Root<Caracteristica> caracteristicaRoot = cq.from(Caracteristica.class);


        cq.select(caracteristicaRoot.get("idTipoUnidadMedida")).where(cb.equal(caracteristicaRoot.get("id"), caracteristica.getId()));
        TypedQuery<TipoUnidadMedida> query = em.createQuery(cq);


        List<TipoUnidadMedida> resultados = query.getResultList();

        if (resultados.isEmpty()) {
            return null;
        }

        return resultados.get(0);
    }

}