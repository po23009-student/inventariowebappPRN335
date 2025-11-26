package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ProveedorDAOTest {

    @Test
    public void crearProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setActivo(true);
        proveedor.setNombre("proveedor nuevo");
        proveedor.setNit("12345678912578");

        EntityManager mockEm = Mockito.mock(EntityManager.class);

        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEm).when(spyCut).getEntityManager();

        spyCut.crear(proveedor);

        Mockito.verify(mockEm).persist(proveedor);
    }

    @Test
    void crearTestRegistroNull() {
        ProveedorDAO cut = new ProveedorDAO();

        assertThrows(IllegalArgumentException.class, () -> cut.crear(null));
    }

    @Test
    void crearTestEntityManagerNull() {
        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(null).when(spyCut).getEntityManager();

        Proveedor proveedor = new Proveedor();
        assertThrows(IllegalStateException.class, () -> spyCut.crear(proveedor));
    }

    @Test
    void crearTestIllegalStateException() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        Mockito.doThrow(new RuntimeException("Fallo interno")).when(mockEM).persist(Mockito.any());
        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(spyCut).getEntityManager();

        Proveedor proveedor = new Proveedor();
        assertThrows(IllegalStateException.class, () -> spyCut.crear(proveedor));
        Mockito.verify(mockEM).persist(proveedor);
    }

    @Test
    void modificarProveedor() {
        Proveedor proveedorExistente = new Proveedor();
        proveedorExistente.setNombre("Proveedor Modificado");
        proveedorExistente.setActivo(false);
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(spyCut).getEntityManager();
        spyCut.modificar(proveedorExistente);
        Mockito.verify(mockEM).merge(proveedorExistente);
    }

    @Test
    void modificarProveedorInvalido() {
        ProveedorDAO cut = new ProveedorDAO();

        assertThrows(IllegalArgumentException.class, () -> {
            cut.modificar(null);
        });

        assertThrows(IllegalStateException.class, () -> {
            cut.modificar(new Proveedor());
        });
    }

    @Test
    void countProveedores() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        CriteriaBuilder mockCb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> mockCq = Mockito.mock(CriteriaQuery.class);
        Root<Proveedor> mockRoot = Mockito.mock(Root.class);
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.getCriteriaBuilder()).thenReturn(mockCb);
        Mockito.when(mockCb.createQuery(Long.class)).thenReturn(mockCq);
        Mockito.when(mockCq.from(Proveedor.class)).thenReturn(mockRoot);
        Mockito.when(mockCq.select(Mockito.any())).thenReturn(mockCq);
        Mockito.when(mockEM.createQuery(mockCq)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(10L);

        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(spyCut).getEntityManager();

        int resultado = spyCut.count();

        assertTrue(resultado >= 0);
        Mockito.verify(mockEM).getCriteriaBuilder();
        Mockito.verify(mockEM).createQuery(mockCq);
        Mockito.verify(mockQuery).getSingleResult();

    }

    @Test
    void countException() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        ProveedorDAO cut = new ProveedorDAO();
        Mockito.when(mockEM.getCriteriaBuilder()).thenThrow(new RuntimeException("BOOM"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                cut::count
        );

        assertEquals("Error", ex.getMessage());
    }

    @Test
    void countEntityManagerNulo() {
        ProveedorDAO cut = new ProveedorDAO();

        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(null).when(spyCut).getEntityManager();

        assertThrows(IllegalStateException.class, () -> {
            spyCut.count();
        });
    }

    @Test
    void findTest() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        Proveedor esperado = new Proveedor();
        esperado.setId(10);

        Mockito.when(mockEM.find(Proveedor.class, 10)).thenReturn(esperado);

        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(spyCut).getEntityManager();

        Proveedor resultado = spyCut.find(10);

        assertNotNull(resultado);
        assertEquals(10, resultado.getId().intValue());

        Mockito.verify(mockEM).find(Proveedor.class, 10);
    }

    @Test
    void eliminarTest() {
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(spyCut).getEntityManager();
        Proveedor p = new Proveedor();
        Proveedor pMerged = new Proveedor();
        Mockito.when(mockEM.merge(p)).thenReturn(pMerged);
        spyCut.eliminar(p);
        Mockito.verify(mockEM).remove(pMerged);
    }

    @Test
    void eliminarTestRegistroNull() {
        ProveedorDAO dao = new ProveedorDAO();
        assertThrows(IllegalArgumentException.class, () -> dao.eliminar(null));
    }

    @Test
    void eliminarTestEntityManagerNull() {
        ProveedorDAO dao = new ProveedorDAO();
        ProveedorDAO spyDao = Mockito.spy(dao);
        Mockito.doReturn(null).when(spyDao).getEntityManager();
        Proveedor proveedor = new Proveedor();

        assertThrows(IllegalStateException.class, () -> spyDao.eliminar(proveedor));
    }


    @Test
    void findRangeTest() {
        Proveedor proveedor1 = new Proveedor();
        Proveedor proveedor2 = new Proveedor();
        Proveedor proveedor3 = new Proveedor();

        List<Proveedor> listaProveedores = Arrays.asList(proveedor1, proveedor2, proveedor3);

        EntityManager mockEm = Mockito.mock(EntityManager.class);
        CriteriaBuilder mockCb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Proveedor> mockCq = Mockito.mock(CriteriaQuery.class);
        Root<Proveedor> mockRoot = Mockito.mock(Root.class);
        TypedQuery<Proveedor> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEm.getCriteriaBuilder()).thenReturn(mockCb);
        Mockito.when(mockCb.createQuery(Proveedor.class)).thenReturn(mockCq);
        Mockito.when(mockCq.from(Proveedor.class)).thenReturn(mockRoot);
        Mockito.when(mockCq.select(mockRoot)).thenReturn(mockCq);
        Mockito.when(mockEm.createQuery(mockCq)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(listaProveedores);

        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);
        Mockito.doReturn(mockEm).when(spyCut).getEntityManager();

        List<Proveedor> resultado = spyCut.findRange(0, 2);

        assertEquals(3, resultado.size());
        assertEquals(listaProveedores, resultado);
        Mockito.verify(mockQuery).setFirstResult(0);
        Mockito.verify(mockQuery).setMaxResults(2);
        Mockito.verify(mockQuery).getResultList();
    }

    @Test
    void findRangeTestEntityManagerNulo() {
        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);

        Mockito.doReturn(null).when(spyCut).getEntityManager();

        assertThrows(IllegalStateException.class, () -> spyCut.findRange(0, 10));
    }

    @Test
    void findRangeParametrosInvalidos() {
        ProveedorDAO cut = new ProveedorDAO();
        ProveedorDAO spyCut = Mockito.spy(cut);

        assertThrows(IllegalArgumentException.class, () -> spyCut.findRange(-1, 10));
    }

}