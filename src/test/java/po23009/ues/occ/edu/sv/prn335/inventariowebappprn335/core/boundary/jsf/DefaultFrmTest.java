package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DefaultFrmTest {

    static class TestFrm extends DefaultFrm<String, Long> {
        InventarioDefaultDataAccess<String, Long> dao;

        @Override
        protected FacesContext getFacesContext() {
            return facesContext;
        }

        @Override
        protected InventarioDefaultDataAccess<String, Long> getDAO() {
            return dao;
        }

        @Override
        protected String getIdAsText(String r) {
            return r;
        }

        @Override
        protected String getIdByText(String id) {
            return id;
        }

        @Override
        protected String nuevoRegistro() {
            return "nuevo";
        }

        FacesContext facesContext;
    }

    TestFrm frm;
    FacesContext facesContext;
    ExternalContext externalContext;
    Flash flash;
    InventarioDefaultDataAccess<String, Long> dao;

    @BeforeEach
    void setup() {
        frm = new TestFrm();
        dao = mock(InventarioDefaultDataAccess.class);
        frm.dao = dao;

        facesContext = mock(FacesContext.class);
        externalContext = mock(ExternalContext.class);
        flash = mock(Flash.class);

        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getFlash()).thenReturn(flash);
        frm.facesContext = facesContext;

        frm.inicializarRegistros();
    }

    @Test
    void testSelectionHandler() {
        SelectEvent<String> event = mock(SelectEvent.class);
        when(event.getObject()).thenReturn("reg");

        frm.selectionHandler(event);

        assertEquals("reg", frm.getRegistro());
        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());
    }

    @Test
    void testBtnGuardarHandler() {
        frm.registro = "reg";

        ActionEvent event = mock(ActionEvent.class);
        frm.btnGuardarHandler(event);

        verify(dao).crear("reg");
        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        verify(flash).setKeepMessages(true);
        assertNotNull(frm.getModelo());
    }

    @Test
    void testBtnCancelarHandler() {
        frm.registro = "reg";
        frm.estado = ESTADO_CRUD.MODIFICAR;

        ActionEvent event = mock(ActionEvent.class);
        frm.btnCancelarHandler(event);

        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
        assertNotNull(frm.getModelo());
    }

    @Test
    void testBtnEliminarHandler() {
        frm.registro = "reg";

        ActionEvent event = mock(ActionEvent.class);
        frm.btnEliminarHandler(event);

        verify(dao).eliminar("reg");
        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        verify(flash).setKeepMessages(true);
        assertNotNull(frm.getModelo());
    }

    @Test
    void testBtnNuevoHandler() {
        ActionEvent event = mock(ActionEvent.class);
        frm.btnNuevoHandler(event);

        assertEquals("nuevo", frm.getRegistro());
        assertEquals(ESTADO_CRUD.CREAR, frm.getEstado());
    }

    @Test
    void testBtnModificarHandler() {
        frm.registro = "reg";

        ActionEvent event = mock(ActionEvent.class);
        frm.btnModificarHandler(event);

        verify(dao).modificar("reg");
        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        verify(flash).setKeepMessages(true);
        assertNotNull(frm.getModelo());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testCargarDatos_devuelveListaDelDAO() {
        when(dao.findRange(0, 2)).thenReturn(List.of("x","y"));
        List<String> result = frm.cargarDatos(0,2);
        assertEquals(List.of("x","y"), result);
        verify(dao).findRange(0,2);
    }

    @Test
    void testCargarDatos_excepcionDevuelveListaVacia() {
        when(dao.findRange(anyInt(), anyInt())).thenThrow(new RuntimeException("error"));
        List<String> result = frm.cargarDatos(0,2);
        assertTrue(result.isEmpty());
        verify(dao).findRange(0,2);
    }

    @Test
    void testContarDatos_devuelveValorDelDAO() {
        when(dao.count()).thenReturn(5);
        int count = frm.contarDatos();
        assertEquals(5, count);
        verify(dao).count();
    }

    @Test
    void testContarDatos_excepcionDevuelve0() {
        when(dao.count()).thenThrow(new RuntimeException("error"));
        int count = frm.contarDatos();
        assertEquals(0, count);
        verify(dao).count();
    }

    @Test
    void testInicializarRegistrosYModelo() {
        when(dao.findRange(anyInt(), anyInt())).thenReturn(List.of("a","b"));
        when(dao.count()).thenReturn(42);

        frm.inicializarRegistros();
        LazyDataModel<String> model = frm.getModelo();

        assertNotNull(model);
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());

        // getRowKey y getRowData
        String key = model.getRowKey("abc");   // "key-abc"
        String rowData = model.getRowData("abc"); // debe devolver "val-abc" según TestFrm

        // count y load
        assertEquals(42, model.count(Collections.emptyMap()));
        assertEquals(List.of("a","b"), model.load(0, 2, Collections.emptyMap(), Collections.emptyMap()));

        // Verificamos llamadas al DAO
        verify(dao).count();
        verify(dao, atLeastOnce()).findRange(anyInt(), anyInt());
    }


}