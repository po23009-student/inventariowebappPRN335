package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProducto;

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
        return em.createQuery("SELECT ptp FROM ProductoTipoProducto ptp WHERE ptp.idProducto.id = :idProducto ORDER BY ptp.fechaCreacion DESC", ProductoTipoProducto.class)
                .setParameter("idProducto", idProducto)
                .getResultList();
    }






    public boolean existeAsociacion(UUID idProducto, Long idTipoProducto) {
        Long count = (Long) getEntityManager().createQuery(
                        "SELECT COUNT(p) FROM ProductoTipoProducto p " +
                                "WHERE p.idProducto.id = :idProd AND p.idTipoProducto.id = :idTipo"
                )
                .setParameter("idProd", idProducto)
                .setParameter("idTipo", idTipoProducto)
                .getSingleResult();

        return count > 0;
    }



    public int eliminarPorProducto(UUID idProducto) {

        List<UUID> idsAfectados = em.createQuery(
                        "SELECT ptp.id FROM ProductoTipoProducto ptp WHERE ptp.idProducto.id = :idProducto", UUID.class)
                .setParameter("idProducto", idProducto)
                .getResultList();

        int totalCaracteristicasEliminadas = 0;


        for (UUID id : idsAfectados) {

            int count = productoTipoProductoCaracteristicaDAO.eliminarPorProductoTipoProducto(id);
            totalCaracteristicasEliminadas += count;
        }

         int count = em.createQuery(
                        "DELETE FROM ProductoTipoProducto ptp WHERE ptp.idProducto.id = :idProducto")
                .setParameter("idProducto", idProducto)
                .executeUpdate();

        return count;
    }


}
