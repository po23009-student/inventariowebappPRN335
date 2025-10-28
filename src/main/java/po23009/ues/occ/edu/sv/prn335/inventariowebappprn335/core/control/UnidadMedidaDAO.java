package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.UnidadMedida;

import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class UnidadMedidaDAO extends InventarioDefaultDataAccess<UnidadMedida> implements Serializable {

    public UnidadMedidaDAO() { super(UnidadMedida.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<UnidadMedida> findByTipo(Integer idTipoUnidadMedida) {
        TypedQuery<UnidadMedida> q = em.createQuery(
                "SELECT u FROM UnidadMedida u WHERE u.idTipoUnidadMedida.id = :idTipo", UnidadMedida.class);
        q.setParameter("idTipo", idTipoUnidadMedida);
        return q.getResultList();
    }
}