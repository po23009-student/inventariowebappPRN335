package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.CriteriaDelete;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProducto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;

import java.util.List;
import java.util.UUID;

@Stateless
public class ProductoTipoProductoDAO extends InventarioDefaultDataAccess<ProductoTipoProducto, UUID> {

    @Inject
    private ProductoTipoProductoCaracteristicaDAO productoTipoProductoCaracteristicaDAO;

    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager em;

    public ProductoTipoProductoDAO() {
        super(ProductoTipoProducto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProductoTipoProducto> findByProducto(UUID idProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductoTipoProducto> cq = cb.createQuery(ProductoTipoProducto.class);
        Root<ProductoTipoProducto> ptp = cq.from(ProductoTipoProducto.class);
        Join<ProductoTipoProducto, Producto> producto = ptp.join("idProducto");
        cq.select(ptp)
                .where(cb.equal(producto.get("id"), idProducto))
                .orderBy(cb.desc(ptp.get("fechaCreacion")));
        return em.createQuery(cq).getResultList();
    }

    public boolean existeAsociacion(UUID idProducto, Long idTipoProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProductoTipoProducto> ptp = cq.from(ProductoTipoProducto.class);
        Join<ProductoTipoProducto, Producto> producto = ptp.join("idProducto");
        Join<ProductoTipoProducto, TipoProducto> tipoProducto = ptp.join("idTipoProducto");
        cq.select(cb.count(ptp))
                .where(cb.and(
                        cb.equal(producto.get("id"), idProducto),
                        cb.equal(tipoProducto.get("id"), idTipoProducto)
                ));
        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }

    public int eliminarPorProducto(UUID idProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UUID> cqIds = cb.createQuery(UUID.class);
        Root<ProductoTipoProducto> ptp = cqIds.from(ProductoTipoProducto.class);
        Join<ProductoTipoProducto, Producto> producto = ptp.join("idProducto");
        cqIds.select(ptp.get("id"))
                .where(cb.equal(producto.get("id"), idProducto));
        List<UUID> idsAfectados = em.createQuery(cqIds).getResultList();
        int totalCaracteristicasEliminadas = 0;
        for (UUID id : idsAfectados) {
            int count = productoTipoProductoCaracteristicaDAO.eliminarPorProductoTipoProducto(id);
            totalCaracteristicasEliminadas += count;
        }
        CriteriaDelete<ProductoTipoProducto> delete = cb.createCriteriaDelete(ProductoTipoProducto.class);
        Root<ProductoTipoProducto> ptpDelete = delete.from(ProductoTipoProducto.class);
        Join<ProductoTipoProducto, Producto> productoDelete = ptpDelete.join("idProducto");
        delete.where(cb.equal(productoDelete.get("id"), idProducto));
        int count = em.createQuery(delete).executeUpdate();
        return count;
    }
}
