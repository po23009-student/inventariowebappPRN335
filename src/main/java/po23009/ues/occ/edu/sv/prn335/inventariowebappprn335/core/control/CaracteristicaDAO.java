package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;

import java.io.Serializable;

@LocalBean
@Stateless
public class CaracteristicaDAO extends InventarioDefaultDataAccess<Caracteristica> implements Serializable {

    public CaracteristicaDAO() { super(Caracteristica.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
