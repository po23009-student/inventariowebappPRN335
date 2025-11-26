package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProductoTipoProductoCaracteristicaDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.ProductoTipoProductoCaracteristica;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ProductoTipoProductoCaracteristicaFrm extends DefaultFrm<ProductoTipoProductoCaracteristica, UUID>  implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    ProductoTipoProductoCaracteristicaDAO ptpcDAO;

    @Inject
    ProductoFrm productoFrm;

    @Inject
    TipoProductoDAO tipoProductoDAO;

    private TipoProducto tipoProductoSeleccionado;
    private List<TipoProducto> listaTiposProducto;

    public ProductoTipoProductoCaracteristicaFrm() {}

    @PostConstruct
    public void init() {
        listaTiposProducto = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        this.nombreBean = "Caracteristicas por Tipo de Producto";
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<ProductoTipoProductoCaracteristica, UUID> getDAO() {
        return ptpcDAO;
    }

    @Override
    protected ProductoTipoProductoCaracteristica nuevoRegistro() {
        return new ProductoTipoProductoCaracteristica();
    }

    @Override
    protected String getIdAsText(ProductoTipoProductoCaracteristica r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected ProductoTipoProductoCaracteristica getIdByText(String id) {
        if (id != null && this.modelo != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                UUID buscado = UUID.fromString(id);
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

    public List<TipoProducto> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<TipoProducto> tipoProductos = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        return tipoProductos.stream()
                .filter(p -> p.getActivo() && p.getNombre().toLowerCase().contains(queryLowerCase))
                .collect(Collectors.toList());
    }

    public ProductoFrm getProductoFrm() {
        return productoFrm;
    }

    public void setProductoFrm(ProductoFrm productoFrm) {
        this.productoFrm = productoFrm;
    }

    public TipoProducto getTipoProductoSeleccionado() {
        return tipoProductoSeleccionado;
    }

    public void setTipoProductoSeleccionado(TipoProducto tipoProductoSeleccionado) {
        this.tipoProductoSeleccionado = tipoProductoSeleccionado;
    }

    public List<TipoProducto> getListaTiposProducto() {
        return listaTiposProducto;
    }

    public void setListaTiposProducto(List<TipoProducto> listaTiposProducto) {
        this.listaTiposProducto = listaTiposProducto;
    }
}
