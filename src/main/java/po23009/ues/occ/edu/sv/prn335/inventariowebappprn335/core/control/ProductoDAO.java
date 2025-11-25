package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProducto;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ProductoDAO extends InventarioDefaultDataAccess<Producto, UUID> {
    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());
    @Inject
    private ProductoTipoProductoDAO productoTipoProductoDAO;
    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager em;
    public ProductoDAO() { super(Producto.class); }
    @Override
    public EntityManager getEntityManager() { return em; }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Producto> findByTipoProducto(Long idTipoProducto) {
        if (idTipoProducto == null) return java.util.Collections.emptyList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
        Root<ProductoTipoProducto> ptp = cq.from(ProductoTipoProducto.class);
        Join<ProductoTipoProducto, Producto> producto = ptp.join("idProducto");
        cq.select(producto).where(cb.and(
                cb.equal(ptp.get("idTipoProducto").get("id"), idTipoProducto),
                cb.isTrue(ptp.get("activo"))
        )).orderBy(cb.asc(producto.get("nombreProducto")));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public void eliminar(Producto entidad) throws IllegalStateException {
        try {
            int count = productoTipoProductoDAO.eliminarPorProducto(entidad.getId());
            LOG.log(Level.INFO, "Se eliminaron {0} ProductoTipoProducto asociados al Producto ID: {1}", new Object[]{count, entidad.getId()});
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar tipos de producto asociados al Producto ID: " + entidad.getId(), e);
            throw new IllegalStateException("Fallo al eliminar tipos de producto asociados.", e);
        }
        super.eliminar(entidad);
    }
}
