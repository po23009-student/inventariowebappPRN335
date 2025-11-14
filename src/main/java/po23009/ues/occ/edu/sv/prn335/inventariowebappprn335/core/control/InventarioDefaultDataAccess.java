package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;

public abstract class InventarioDefaultDataAccess<T, ID> implements InventarioDAOInterface<T, ID> {
    final Class<T> entityClass;

    public InventarioDefaultDataAccess(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract EntityManager getEntityManager();

    public void crear(T registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }

        try {
            EntityManager em = getEntityManager();

            if (em == null) {
                throw new IllegalStateException("EntityManager no disponible");
            }

            em.persist(registro);

        } catch (Exception ex) {
            throw new IllegalStateException("Error al crear el registro", ex);
        }
    }

    public void modificar(T registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }

        try {
            EntityManager em = getEntityManager();

            if (em == null) {
                throw new IllegalStateException("EntityManager no disponible");
            }

            em.merge(registro);

        } catch (Exception ex) {
            throw new IllegalStateException("Error al crear el registro", ex);
        }
    }

    public int count() {
        try {
            EntityManager em = getEntityManager();

            if (em == null) {
                throw new IllegalStateException("EntityManager no disponible");
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<T> root = cq.from(entityClass);
            cq.select(cb.count(root));

            TypedQuery<Long> tq = em.createQuery(cq);

            return tq.getSingleResult().intValue();

        } catch (Exception ex) {
            throw new IllegalStateException("Error", ex);
        }
    }

    public void eliminar(T registro) {
        if(registro == null) {
            throw new IllegalArgumentException("Los parametros ingresados son invalidos");
        }

        try {
            EntityManager em = getEntityManager();

            if(em == null) {
                throw new IllegalStateException("EntityManager no disponible");
            }

            T registroEncontrado = em.merge(registro);
            em.remove(registroEncontrado);

        } catch(Exception ex) {
            throw new IllegalStateException("Ocurrió un error", ex);
        }
    }

    public T find(ID id)  {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException {
        EntityManager em = null;
        if (first < 0 || max <= 0) {
            throw new IllegalArgumentException("parametros no validos");
        }
        try {
            em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException();
            }
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> raiz = cq.from(entityClass);
            cq.select(raiz);
            TypedQuery<T> q = em.createQuery(cq);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException();
        }
    }
}