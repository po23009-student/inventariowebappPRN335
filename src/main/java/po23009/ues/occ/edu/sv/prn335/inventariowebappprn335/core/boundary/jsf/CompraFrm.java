package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDetalleDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
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

    @Inject
    private CompraDetalleFrm cDetalleFrm;

    @Inject
    private ProveedorFrm proveedorFrm;

    private Proveedor proveedorSeleccionado;

    private List<CompraDetalle> detallesCompra;

    public CompraFrm() {
        this.nombreBean = "Compras";
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

    public void cargarDetallesCompra(Compra compraSeleccionada) {
        if (compraSeleccionada != null && compraSeleccionada.getId() != null) {
            this.detallesCompra = compraDetalleDAO.obtenerPorCompra(compraSeleccionada.getId());
        } else {
            this.detallesCompra = null;
        }
    }

    public void btnSeleccionarProv(ActionEvent actionEvent) {
        if (proveedorSeleccionado != null) {
            this.registro.setProveedor(proveedorSeleccionado);
            System.out.println("Proveedor seleccionado: " + proveedorSeleccionado.getNombre());
        } else {
            System.out.println("No se seleccionó ningún proveedor");
        }
    }

    public List<CompraDetalle> getDetallesCompra() {
        return detallesCompra;
    }

    public void setDetallesCompra(List<CompraDetalle> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }

    public CompraDetalleFrm getcDetalleFrm() {
        return cDetalleFrm;
    }

    public void setcDetalleFrm(CompraDetalleFrm cDetalleFrm) {
        this.cDetalleFrm = cDetalleFrm;
    }

    public ProveedorFrm getProveedorFrm() {
        return proveedorFrm;
    }

    public void setProveedorFrm(ProveedorFrm proveedorFrm) {
        this.proveedorFrm = proveedorFrm;
    }


    public Proveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

}