package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;

import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class CaracteristicaDAO extends InventarioDefaultDataAccess<Caracteristica, Integer> implements Serializable {

    public CaracteristicaDAO() { super(Caracteristica.class); }

    @PersistenceContext(unitName="inventarioPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Caracteristica> findTodos() {
        return em.createQuery("SELECT c FROM Caracteristica c", Caracteristica.class)
                .getResultList();
    }
}
