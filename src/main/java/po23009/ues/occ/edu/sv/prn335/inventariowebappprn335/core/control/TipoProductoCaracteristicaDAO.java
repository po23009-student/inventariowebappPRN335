package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoProductoCaracteristica> cq = cb.createQuery(TipoProductoCaracteristica.class);
        Root<TipoProductoCaracteristica> tpc = cq.from(TipoProductoCaracteristica.class);
        Join<TipoProductoCaracteristica, Caracteristica> caracteristica = tpc.join("idCaracteristica");
        Join<TipoProductoCaracteristica, TipoProducto> tipoProducto = tpc.join("idTipoProducto");
        cq.select(tpc)
                .where(cb.equal(tipoProducto.get("id"), idTipoProducto))
                .orderBy(cb.asc(caracteristica.get("nombre")));
        return em.createQuery(cq).getResultList();
    }

    public TipoProductoCaracteristica findByTipoAndCaracteristica(Long idTipoProducto, Integer idCaracteristica) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TipoProductoCaracteristica> cq = cb.createQuery(TipoProductoCaracteristica.class);
            Root<TipoProductoCaracteristica> tpc = cq.from(TipoProductoCaracteristica.class);
            Join<TipoProductoCaracteristica, TipoProducto> tipoProducto = tpc.join("idTipoProducto");
            Join<TipoProductoCaracteristica, Caracteristica> caracteristica = tpc.join("idCaracteristica");
            cq.select(tpc)
                    .where(cb.and(
                            cb.equal(tipoProducto.get("id"), idTipoProducto),
                            cb.equal(caracteristica.get("id"), idCaracteristica)
                    ));
            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public int eliminarPorTipoProducto(Long idTipoProducto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<TipoProductoCaracteristica> delete = cb.createCriteriaDelete(TipoProductoCaracteristica.class);
        Root<TipoProductoCaracteristica> tpc = delete.from(TipoProductoCaracteristica.class);
        Join<TipoProductoCaracteristica, TipoProducto> tipoProducto = tpc.join("idTipoProducto");
        delete.where(cb.equal(tipoProducto.get("id"), idTipoProducto));
        return em.createQuery(delete).executeUpdate();
    }
}
