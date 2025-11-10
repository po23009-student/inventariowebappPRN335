package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.CompraDetalle;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CompraFrm extends DefaultFrm<Compra> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    private CompraDAO compraDAO;

    @Inject
    private CompraDetalleDAO compraDetalleDAO;

    private List<CompraDetalle> detallesPorCompra;
    private Proveedor proveedorSeleccionado;

    public CompraFrm() {
        this.nombreBean = "Compras";
    }

    public List<CompraDetalle> getDetallesPorCompra() {
        return detallesPorCompra;
    }

    public void cargarDetallesCompra() {
        if (registro != null && registro.getId() != null) {
            detallesPorCompra = compraDetalleDAO.findByCompra(registro.getId());
        } else {
            detallesPorCompra = null;
        }
    }

    @Override
    public void selectionHandler(SelectEvent<Compra> r) {
        if (r != null) {
            this.registro = r.getObject();
            proveedorSeleccionado = this.registro.getProveedor();
            this.estado = ESTADO_CRUD.MODIFICAR;
            cargarDetallesCompra();
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Compra> getDAO() {
        return compraDAO;
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra nuevaCompra = new Compra();
        nuevaCompra.setId(compraDAO.obtenerSiguienteId());
        return nuevaCompra;
    }

    @Override
    protected String getIdAsText(Compra r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Compra getIdByText(String id) {
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
    public void btnCancelarHandler(ActionEvent event) {
        this.registro = null;
        this.proveedorSeleccionado = null;
        this.estado = ESTADO_CRUD.NADA;
    }

    public void btnSeleccionarProv(ActionEvent actionEvent) {
        if (proveedorSeleccionado != null) {
            this.registro.setProveedor(proveedorSeleccionado);
        } else {
            System.out.println("No se seleccionó ningún proveedor");
        }
    }

    public Proveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public CompraDetalleDAO getCompraDetalleDAO() {
        return compraDetalleDAO;
    }

    public void setCompraDetalleDAO(CompraDetalleDAO compraDetalleDAO) {
        this.compraDetalleDAO = compraDetalleDAO;
    }
}
