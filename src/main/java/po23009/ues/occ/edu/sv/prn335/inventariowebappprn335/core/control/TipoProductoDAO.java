package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

    public List<TipoProducto> findTiposPadre(boolean incluirInactivos) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoProducto> cq = cb.createQuery(TipoProducto.class);
        Root<TipoProducto> t = cq.from(TipoProducto.class);
        if (incluirInactivos) {
            cq.where(cb.isNull(t.get("idTipoProductoPadre")));
        } else {
            cq.where(cb.and(
                    cb.isNull(t.get("idTipoProductoPadre")),
                    cb.isTrue(t.get("activo"))
            ));
        }
        cq.select(t)
                .orderBy(cb.asc(t.get("nombre")));
        return em.createQuery(cq).getResultList();
    }

    public List<TipoProducto> findHijosByPadre(Long idTipoProductoPadre, boolean incluirInactivos) {
        if (idTipoProductoPadre == null) {
            return Collections.emptyList();
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoProducto> cq = cb.createQuery(TipoProducto.class);
        Root<TipoProducto> t = cq.from(TipoProducto.class);
        if (incluirInactivos) {
            cq.where(cb.equal(t.get("idTipoProductoPadre").get("id"), idTipoProductoPadre));
        } else {
            cq.where(cb.and(
                    cb.equal(t.get("idTipoProductoPadre").get("id"), idTipoProductoPadre),
                    cb.isTrue(t.get("activo"))
            ));
        }
        cq.select(t)
                .orderBy(cb.asc(t.get("nombre")));
        return em.createQuery(cq).getResultList();
    }

    public boolean existeNombre(String nombre, Long id) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<TipoProducto> t = cq.from(TipoProducto.class);
        cq.select(cb.count(t));
        jakarta.persistence.criteria.Predicate nombrePredicate = cb.equal(t.get("nombre"), nombre.trim());
        if (id != null) {
            cq.where(cb.and(
                    nombrePredicate,
                    cb.notEqual(t.get("id"), id)
            ));
        } else {
            cq.where(nombrePredicate);
        }
        return em.createQuery(cq).getSingleResult() > 0;
    }

    public boolean tieneHijos(Long id) {
        if (id == null) return false;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<TipoProducto> t = cq.from(TipoProducto.class);
            cq.select(cb.count(t))
                    .where(cb.equal(t.get("idTipoProductoPadre").get("id"), id));
            Long count = em.createQuery(cq).getSingleResult();
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
            LOG.log(Level.SEVERE, "Error al eliminar características asociadas al TipoProducto ID: " + entidad.getId(), e);
            throw new IllegalStateException("Fallo al eliminar características asociadas.", e);
        }
        super.eliminar(entidad);
    }
}
