package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;

import java.io.Serializable;

@Stateless
@LocalBean
public class TipoAlmacenDAO extends InventarioDefaultDataAccess<TipoAlmacen> implements Serializable {

    public TipoAlmacenDAO() { super(TipoAlmacen.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}