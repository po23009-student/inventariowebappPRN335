package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.KardexDetalle;

import java.io.Serializable;
import java.util.UUID;

@Stateless
@LocalBean
public class KardexDetalleDAO extends InventarioDefaultDataAccess<KardexDetalle, UUID> implements Serializable {

    public KardexDetalleDAO() { super(KardexDetalle.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
