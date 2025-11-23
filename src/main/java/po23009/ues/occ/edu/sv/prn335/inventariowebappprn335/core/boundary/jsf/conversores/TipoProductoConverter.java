package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;

@FacesConverter(value = "tipoProductoConverter")
public class TipoProductoConverter implements Converter<TipoProducto> {

    @Override
    public TipoProducto getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return (TipoProducto) comp.getAttributes().get(value);
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, TipoProducto value) {
        if (value == null) {
            return "";
        }

        String key = (value.getId() != null) ? value.getId().toString() : String.valueOf(value.hashCode());

        comp.getAttributes().put(key, value);

        return key;
    }
}
