package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Kardex;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.VentaDetalle;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.TypedQuery;

@Stateless
@LocalBean
public class KardexDAO extends InventarioDefaultDataAccess<Kardex, UUID> {

    private static final Logger LOG = Logger.getLogger(KardexDAO.class.getName());

    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager em;

    public KardexDAO() {
        super(Kardex.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public BigDecimal getBalanceProducto(UUID idProducto) {
        try {
            BigDecimal balance = em.createQuery(
                            "SELECT k.cantidadActual FROM Kardex k WHERE k.idProducto.id = :idProducto ORDER BY k.fecha DESC, k.id DESC",
                            BigDecimal.class)
                    .setParameter("idProducto", idProducto)
                    .setMaxResults(1)
                    .setHint("javax.persistence.cache.storeMode", "BYPASS")
                    .getSingleResult();

            return balance != null ? balance : BigDecimal.ZERO;

        } catch (Exception ex) {
            LOG.log(Level.INFO, "No se encontró kardex previo para producto {0}, usando balance 0", idProducto);
            return BigDecimal.ZERO;
        }
    }

    public void registrarSalida(VentaDetalle detalle, BigDecimal balanceAntes) {
        try {
            BigDecimal nuevoBalance = balanceAntes.subtract(detalle.getCantidad());
            BigDecimal cantidadSalida = detalle.getCantidad().negate();

            String sql = "INSERT INTO kardex (" +
                    "id_kardex, id_producto, fecha, tipo_movimiento, " +
                    "cantidad, precio, cantidad_actual, precio_actual, " +
                    "id_venta_detalle, observaciones, referencia_externa" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int result = em.createNativeQuery(sql)
                    .setParameter(1, UUID.randomUUID())
                    .setParameter(2, detalle.getIdProducto().getId())
                    .setParameter(3, OffsetDateTime.now())
                    .setParameter(4, "SALIDA")
                    .setParameter(5, cantidadSalida)
                    .setParameter(6, detalle.getPrecio())
                    .setParameter(7, nuevoBalance)
                    .setParameter(8, detalle.getPrecio())
                    .setParameter(9, detalle.getId())
                    .setParameter(10, "Despacho de Venta ID: " + detalle.getIdVenta().getId().toString())
                    .setParameter(11, "VENTA_" + detalle.getIdVenta().getId().toString())
                    .executeUpdate();

            if (result == 1) {
                LOG.log(Level.INFO, "✅ Kardex registrado EXITOSAMENTE - Producto: {0}, Cantidad: {1}, Nuevo balance: {2}",
                        new Object[]{detalle.getIdProducto().getNombreProducto(), detalle.getCantidad(), nuevoBalance});
            } else {
                throw new IllegalStateException("No se pudo insertar el registro en kardex");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "❌ Error en registro de kardex para producto: " +
                    (detalle != null && detalle.getIdProducto() != null ?
                            detalle.getIdProducto().getNombreProducto() : "N/A"), e);
            throw new IllegalStateException("Error en registro de kardex", e);
        }
    }
}