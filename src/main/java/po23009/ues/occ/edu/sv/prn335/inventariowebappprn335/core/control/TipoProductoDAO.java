package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class TipoProductoDAO extends InventarioDefaultDataAccess<TipoProducto, Long> {

    @Inject
    private TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

    private static final Logger LOG = Logger.getLogger(TipoProductoDAO.class.getName());

    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager em;

    public TipoProductoDAO() {
        super(TipoProducto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public List<TipoProducto> findTodos() {
        return em.createQuery("SELECT t FROM TipoProducto t ORDER BY t.nombre", TipoProducto.class)
                .getResultList();
    }


    public List<TipoProducto> findTiposPadre(boolean incluirInactivos) {
        String jpql = "SELECT t FROM TipoProducto t WHERE t.idTipoProductoPadre IS NULL";
        if (!incluirInactivos) {
            jpql += " AND t.activo = true";
        }
        jpql += " ORDER BY t.nombre";
        return em.createQuery(jpql, TipoProducto.class).getResultList();
    }

    public List<TipoProducto> findHijosByPadre(Long idTipoProductoPadre, boolean incluirInactivos) {
        if (idTipoProductoPadre == null) {
            return Collections.emptyList();
        }
        String jpql = "SELECT t FROM TipoProducto t WHERE t.idTipoProductoPadre.id = :padreId";
        if (!incluirInactivos) {

            jpql += " AND t.activo = true";
        }
        jpql += " ORDER BY t.nombre";
        return em.createQuery(jpql, TipoProducto.class)
                .setParameter("padreId", idTipoProductoPadre)
                .getResultList();
    }


    public boolean existeNombre(String nombre, Long id) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        String queryStr = "SELECT COUNT(t) FROM TipoProducto t WHERE t.nombre = :nombre";
        if (id != null) {
            queryStr += " AND t.id != :id";
        }

        TypedQuery<Long> query = em.createQuery(queryStr, Long.class);
        query.setParameter("nombre", nombre.trim());

        if (id != null) {
            query.setParameter("id", id);
        }

        return query.getSingleResult() > 0;
    }


    public boolean tieneHijos(Long id) {
        if (id == null) return false;

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(t) FROM TipoProducto t WHERE t.idTipoProductoPadre.id = :padreId", Long.class)
                    .setParameter("padreId", id)
                    .getSingleResult();

            return count > 0;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al verificar si el tipo de producto tiene hijos: " + id, e);

            return true;
        }
    }


    @Override
    public void eliminar(TipoProducto entidad) throws IllegalStateException {

        try {
            int count = tipoProductoCaracteristicaDAO.eliminarPorTipoProducto(entidad.getId());
            LOG.log(Level.INFO, "Se eliminaron {0} TipoProductoCaracteristica asociados al TipoProducto ID: {1}",
                    new Object[]{count, entidad.getId()});

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar hijos de TipoProducto ID: " + entidad.getId(), e);

            throw new IllegalStateException("Fallo al eliminar características asociadas.", e);
        }

        super.eliminar(entidad);
    }

}
