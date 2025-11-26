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
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class CompraDAO extends InventarioDefaultDataAccess<Compra, Long> implements Serializable {

    public CompraDAO() { super(Compra.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public List<Compra> comprasPagadas() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Compra> cq = cb.createQuery(Compra.class);
        Root<Compra> compraRoot = cq.from(Compra.class);
        
        cq.select(compraRoot).where(cb.equal(compraRoot.get("estado"), "PAGADA"));

        return em.createQuery(cq).getResultList();
    }


}