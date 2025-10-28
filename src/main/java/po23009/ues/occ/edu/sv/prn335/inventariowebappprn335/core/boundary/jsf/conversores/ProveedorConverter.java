package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ProveedorDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

@FacesConverter("proveedorConverter")
public class ProveedorConverter implements Converter {

    private ProveedorDAO proveedorDAO;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        if (proveedorDAO == null) {
            proveedorDAO = CDI.current().select(ProveedorDAO.class).get();
        }

        try {
            Integer id = Integer.valueOf(value);
            return proveedorDAO.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        Proveedor prov = (Proveedor) value;
        return prov.getId() != null ? prov.getId().toString() : "";
    }
}
