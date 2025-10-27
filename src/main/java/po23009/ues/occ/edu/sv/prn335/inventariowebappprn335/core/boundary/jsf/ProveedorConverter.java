package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProveedorDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

@FacesConverter(value = "proveedorConverter", managed = true)
public class ProveedorConverter implements Converter<Proveedor> {

    @Inject
    private ProveedorDAO proveedorDAO;

    @Override
    public Proveedor getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Integer id = Integer.valueOf(value);
            return proveedorDAO.buscarPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Proveedor proveedor) {
        if (proveedor == null || proveedor.getId() == null) {
            return "";
        }
        return proveedor.getId().toString();
    }
}
