package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProductoCaracteristica;

import java.util.List;
import java.util.UUID;


@Stateless
@LocalBean
public class ProductoTipoProductoCaracteristicaDAO {


    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager entityManager;


    public void crear(ProductoTipoProductoCaracteristica entity) {
        entityManager.persist(entity);
    }

    public void modificar(ProductoTipoProductoCaracteristica entity) {
        entityManager.merge(entity);
    }

    public void eliminar(ProductoTipoProductoCaracteristica entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }



    public ProductoTipoProductoCaracteristica find(UUID id) {
        if (id == null) {
            return null;
        }
        return entityManager.find(ProductoTipoProductoCaracteristica.class, id);
    }




    public List<ProductoTipoProductoCaracteristica> findByProductoTipoProducto(UUID idProductoTipoProducto) {
        try {
            TypedQuery<ProductoTipoProductoCaracteristica> query = entityManager.createQuery(
                    "SELECT ptpc FROM ProductoTipoProductoCaracteristica ptpc WHERE ptpc.idProductoTipoProducto.id = :idProductoTipoProducto",
                    ProductoTipoProductoCaracteristica.class);

            query.setParameter("idProductoTipoProducto", idProductoTipoProducto);
            return query.getResultList();
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    public int eliminarPorProductoTipoProducto(UUID idProductoTipoProducto) {
        return entityManager.createQuery(
                        "DELETE FROM ProductoTipoProductoCaracteristica ptpc WHERE ptpc.idProductoTipoProducto.id = :id")
                .setParameter("id", idProductoTipoProducto)
                .executeUpdate();
    }

}