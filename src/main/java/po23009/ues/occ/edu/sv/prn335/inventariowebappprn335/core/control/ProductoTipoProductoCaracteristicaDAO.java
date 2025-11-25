package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.CriteriaDelete;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProductoCaracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProducto;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class ProductoTipoProductoCaracteristicaDAO extends InventarioDefaultDataAccess<ProductoTipoProductoCaracteristica, UUID> {

    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager entityManager;

    public ProductoTipoProductoCaracteristicaDAO() {
        super(ProductoTipoProductoCaracteristica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<ProductoTipoProductoCaracteristica> findByProductoTipoProducto(UUID idProductoTipoProducto) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProductoTipoProductoCaracteristica> cq = cb.createQuery(ProductoTipoProductoCaracteristica.class);
            Root<ProductoTipoProductoCaracteristica> ptpc = cq.from(ProductoTipoProductoCaracteristica.class);
            Join<ProductoTipoProductoCaracteristica, ProductoTipoProducto> productoTP = ptpc.join("idProductoTipoProducto");
            cq.select(ptpc)
                    .where(cb.equal(productoTP.get("id"), idProductoTipoProducto));
            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    public int eliminarPorProductoTipoProducto(UUID idProductoTipoProducto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<ProductoTipoProductoCaracteristica> delete = cb.createCriteriaDelete(ProductoTipoProductoCaracteristica.class);
        Root<ProductoTipoProductoCaracteristica> ptpc = delete.from(ProductoTipoProductoCaracteristica.class);
        Join<ProductoTipoProductoCaracteristica, ProductoTipoProducto> productoTP = ptpc.join("idProductoTipoProducto");
        delete.where(cb.equal(productoTP.get("id"), idProductoTipoProducto));
        return entityManager.createQuery(delete).executeUpdate();
    }
}