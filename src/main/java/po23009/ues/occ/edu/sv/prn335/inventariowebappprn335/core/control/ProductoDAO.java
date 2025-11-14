package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

import java.io.Serializable;
import java.util.UUID;

@LocalBean
@Stateless
public class ProductoDAO extends InventarioDefaultDataAccess<Producto, UUID> implements Serializable {

    public ProductoDAO() { super(Producto.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}