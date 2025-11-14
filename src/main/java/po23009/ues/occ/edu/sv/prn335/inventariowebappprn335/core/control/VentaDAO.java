package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Venta;

import java.io.Serializable;
import java.util.UUID;

@Stateless
@LocalBean
public class VentaDAO extends InventarioDefaultDataAccess<Venta, UUID> implements Serializable {

    public VentaDAO() { super(Venta.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
