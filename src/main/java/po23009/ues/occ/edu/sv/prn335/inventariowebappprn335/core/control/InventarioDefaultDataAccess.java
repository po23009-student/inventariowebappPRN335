package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;
public abstract class InventarioDefaultDataAccess<T> implements InventarioDAOInterface<T> {
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

    public T leer(int id) {
        if(id < 1) {
            throw new IllegalArgumentException("Los parametros ingresados son invalidos");
        }

        try {
            EntityManager em = getEntityManager();

            if(em == null) {
                throw new IllegalStateException("EntityManager no disponible");
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);

            TypedQuery<T> tq = em.createQuery(cq);

            Predicate predicadoId = cb.equal(root.get("id"), id);

            cq.where(predicadoId);

            return tq.getSingleResult();

        } catch(Exception ex) {
            throw new IllegalStateException("Ocurrió un error", ex);
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

    public List<T> findRange(int first, int max) throws IllegalArgumentException {
        if(first < 0 || max < 1) {
            throw new IllegalArgumentException();
        }

        try {
            EntityManager em = getEntityManager();

            if(em != null) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<T> cq = cb.createQuery(entityClass);
                Root<T> rootEntry = cq.from(entityClass);
                cq.select(rootEntry);

                TypedQuery<T> allQuery = em.createQuery(cq);
                allQuery.setFirstResult(first);
                allQuery.setMaxResults(max);
                return allQuery.getResultList();
            }

        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }

        throw new IllegalStateException("No se puede acceder al repositorio");
    }
}