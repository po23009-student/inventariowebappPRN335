package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

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

    public ProductoDAO() {
        super(Producto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public Producto leer(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return em.find(Producto.class, id);
    }


    @Override
    public Producto find(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return em.find(Producto.class, id);
    }

    @Override
    public int count() {
        Long total = em.createQuery("SELECT COUNT(p) FROM Producto p", Long.class)
                .getSingleResult();
        return total.intValue();
    }


    @Override
    public List<Producto> findRange(int first, int pageSize) {
        return em.createQuery("SELECT p FROM Producto p ORDER BY p.nombreProducto ASC", Producto.class)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList();
    }





    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Producto> findByTipoProducto(Long idTipoProducto) {
        if (idTipoProducto == null) {
            return java.util.Collections.emptyList();
        }

        return em.createQuery(
                         "SELECT ptp.idProducto FROM ProductoTipoProducto ptp " +
                                 "WHERE ptp.idTipoProducto.id = :idTipo AND ptp.activo = TRUE " +
                                "ORDER BY ptp.idProducto.nombreProducto",
                        Producto.class)
                .setParameter("idTipo", idTipoProducto)
                .getResultList();
    }


    @Override
    public void eliminar(Producto entidad) throws IllegalStateException {

         try {
            int count = productoTipoProductoDAO.eliminarPorProducto(entidad.getId());
            LOG.log(Level.INFO, "Se eliminaron {0} ProductoTipoProducto asociados al Producto ID: {1}",
                    new Object[]{count, entidad.getId()});

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar tipos de producto asociados al Producto ID: " + entidad.getId(), e);
            throw new IllegalStateException("Fallo al eliminar tipos de producto asociados.", e);
        }


        super.eliminar(entidad);
    }
}
