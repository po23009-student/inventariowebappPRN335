package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProducto;

import java.io.Serializable;
import java.util.UUID;

@Stateless
@LocalBean
public class ProductoTipoProductoDAO extends InventarioDefaultDataAccess<ProductoTipoProducto, UUID> implements Serializable {

    public ProductoTipoProductoDAO() { super(ProductoTipoProducto.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}