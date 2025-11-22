package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.*;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class TipoProductoCaracteristicaFrm extends DefaultFrm<TipoProductoCaracteristica, Long> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    TipoProductoCaracteristicaDAO tpcDAO;

    @Inject
    CaracteristicaDAO caracteristicaDAO;

    @Inject
    TipoProductoFrm tpFrm;

    private Caracteristica caracteristicaSeleccionada;
    private List<Caracteristica> listaCaracteristicas;

    public TipoProductoCaracteristicaFrm() {

    }

    @PostConstruct
    public void init() {
        listaCaracteristicas = caracteristicaDAO.findRange(0, Integer.MAX_VALUE);
        tpFrm.cargarCaracteristicas();
        this.nombreBean = "Caracteristicas del Tipo de Producto";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<TipoProductoCaracteristica, Long> getDAO() {
        return tpcDAO;
    }

    @Override
    protected TipoProductoCaracteristica nuevoRegistro() {
        TipoProductoCaracteristica nuevoTPC = new TipoProductoCaracteristica();
        nuevoTPC.setIdTipoProducto(tpFrm.getRegistro());
        nuevoTPC.setIdCaracteristica(null);
        return nuevoTPC;
    }

    @Override
    protected String getIdAsText(TipoProductoCaracteristica r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected TipoProductoCaracteristica getIdByText(String id) {
        if (id != null && this.modelo != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                Long buscado = Long.valueOf(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId() != null && r.getId().equals(buscado))
                        .findFirst()
                        .orElse(null);
            } catch (NumberFormatException e) {
                System.err.println("ID no es un número válido: " + id);
                return null;
            }
        }
        return null;
    }

    @Override
    public void btnEliminarHandler(ActionEvent event) {
        getDAO().eliminar(this.registro);
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro eliminado", "El registro fue eliminado"));
        getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
        tpFrm.cargarCaracteristicas();
        inicializarRegistros();
    }

    @Override
    public void btnGuardarHandler(ActionEvent event) {
        if (caracteristicaSeleccionada == null || caracteristicaSeleccionada.getId() == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar una característica válida"));
            return;
        }

        getDAO().crear(this.registro);

        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado", "Caracteristica creada correctamente"));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);

        registro = null;
        caracteristicaSeleccionada = null;
        estado = ESTADO_CRUD.NADA;

        tpFrm.cargarCaracteristicas();
        modelo = null;
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        try {
            this.getDAO().modificar(this.registro);
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro modificado", "El registro fue modificado exitosamente"));
            getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
            this.estado = ESTADO_CRUD.NADA;
            this.modelo = null;
            inicializarRegistros();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar", e.getMessage()));
        }
    }

    @Override
    public void selectionHandler(SelectEvent<TipoProductoCaracteristica> r) {
        if (r != null) {
            this.registro = r.getObject();
            this.estado = ESTADO_CRUD.MODIFICAR;
        }
    }

    public void btnSeleccionarCaracteristica(ActionEvent actionEvent) {
        if (caracteristicaSeleccionada != null) {
            this.registro.setIdCaracteristica(caracteristicaSeleccionada);
        } else {
            System.out.println("No se seleccionó ningúna característica");
        }
    }

    public List<Caracteristica> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Caracteristica> caracteristicas = caracteristicaDAO.findRange(0, Integer.MAX_VALUE);
        return caracteristicas.stream()
                .filter(p -> p.getActivo() && p.getNombre().toLowerCase().contains(queryLowerCase))
                .collect(Collectors.toList());
    }

    public Caracteristica getCaracteristicaSeleccionada() {
        return caracteristicaSeleccionada;
    }

    public void setCaracteristicaSeleccionada(Caracteristica caracteristicaSeleccionada) {
        this.caracteristicaSeleccionada = caracteristicaSeleccionada;
    }

    public TipoProductoFrm getTpFrm() {
        return tpFrm;
    }

    public void setTpFrm(TipoProductoFrm tpFrm) {
        this.tpFrm = tpFrm;
    }

    public TipoProductoCaracteristicaDAO getTpcDAO() {
        return tpcDAO;
    }

    public void setTpcDAO(TipoProductoCaracteristicaDAO tpcDAO) {
        this.tpcDAO = tpcDAO;
    }

    public List<Caracteristica> getListaCaracteristicas() {
        return listaCaracteristicas;
    }

    public void setListaCaracteristicas(List<Caracteristica> listaCaracteristicas) {
        this.listaCaracteristicas = listaCaracteristicas;
    }
}
