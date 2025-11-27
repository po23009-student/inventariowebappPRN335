package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Venta;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.VentaDetalle;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class VentaDAO extends InventarioDefaultDataAccess<Venta, UUID> implements Serializable {

    public VentaDAO() { super(Venta.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Venta> buscarPendientesParaDespacho(int first, int pageSize) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Venta> cq = cb.createQuery(Venta.class);
            Root<Venta> venta = cq.from(Venta.class); // Define la entidad raíz

            Predicate pendiente = cb.equal(venta.get("estado"), "PENDIENTE");
            cq.where(pendiente);

            cq.orderBy(cb.asc(venta.get("fecha")));

            TypedQuery<Venta> query = em.createQuery(cq);
            return query.setFirstResult(first)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al cargar ventas pendientes usando Criteria API", ex);
        }
    }

    public Long contarPendientesParaDespacho() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Venta> venta = cq.from(Venta.class);
            cq.select(cb.count(venta));

            Predicate pendiente = cb.equal(venta.get("estado"), "PENDIENTE");
            cq.where(pendiente);
            return em.createQuery(cq).getSingleResult();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al contar ventas pendientes usando Criteria API", ex);
        }
    }

    public void cambiarEstado(UUID idVenta, String nuevoEstado) {
        Venta venta = find(idVenta);
        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada con ID: " + idVenta);
        }

        venta.setEstado(nuevoEstado);
        modificar(venta);
    }


    public List<VentaDetalle> obtenerDetallesVenta(UUID idVenta) {
        try {
            return em.createQuery(
                            "SELECT d FROM VentaDetalle d WHERE d.idVenta.id = :idVenta",
                            VentaDetalle.class
                    )
                    .setParameter("idVenta", idVenta)
                    .getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al cargar detalles de venta: " + idVenta, ex);
        }
    }


    public boolean puedeEliminar(UUID idVenta) {
        try {
            Venta venta = find(idVenta);
            if (venta == null) {
                return false;
            }

            return !"DESPACHADA".equals(venta.getEstado());

        } catch (Exception ex) {
            throw new IllegalStateException("Error al verificar si la venta puede eliminarse: " + idVenta, ex);
        }
    }

    @Override
    public void eliminar(Venta venta) throws IllegalStateException {
        if (venta == null || venta.getId() == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }

        if (!puedeEliminar(venta.getId())) {
            throw new IllegalStateException("No se puede eliminar una venta que ya ha sido DESPACHADA");
        }
        super.eliminar(venta);
    }
}