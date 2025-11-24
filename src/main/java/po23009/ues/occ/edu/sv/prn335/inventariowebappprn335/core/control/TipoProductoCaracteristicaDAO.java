package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;

import java.util.List;

@Stateless
public class TipoProductoCaracteristicaDAO extends InventarioDefaultDataAccess<TipoProductoCaracteristica,Long > {

    @PersistenceContext(unitName = "inventarioPU")
    private EntityManager em;

    public TipoProductoCaracteristicaDAO() {
        super(TipoProductoCaracteristica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<TipoProductoCaracteristica> findByTipoProducto(Long idTipoProducto) {
        return em.createQuery("SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :idTipoProducto ORDER BY tpc.idCaracteristica.nombre", TipoProductoCaracteristica.class)
                .setParameter("idTipoProducto", idTipoProducto)
                .getResultList();
    }

    public TipoProductoCaracteristica findByTipoAndCaracteristica(Long idTipoProducto, Integer idCaracteristica) {
        try {
            return em.createQuery("SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :idTipoProducto AND tpc.idCaracteristica.id = :idCaracteristica", TipoProductoCaracteristica.class)
                    .setParameter("idTipoProducto", idTipoProducto)
                    .setParameter("idCaracteristica", idCaracteristica)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }



    public int eliminarPorTipoProducto(Long idTipoProducto) {

        return em.createQuery(
                        "DELETE FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :id")
                .setParameter("id", idTipoProducto)
                .executeUpdate();
    }

}
