package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

import java.io.Serializable;

@LocalBean
@Stateless
public class TipoUnidadMedidaDAO extends InventarioDefaultDataAccess<TipoUnidadMedida> implements Serializable {

    public TipoUnidadMedidaDAO() { super(TipoUnidadMedida.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}