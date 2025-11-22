package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.TipoProductoFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;

@FacesConverter("tipoProductoConverter")
public class TipoProductoConverter implements Converter<TipoProducto> {
    @Override
    public TipoProducto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        TipoProductoFrm frm = context.getApplication()
                .evaluateExpressionGet(context, "#{tipoProductoFrm}", TipoProductoFrm.class);

        try {
            Long id = Long.valueOf(value);
            return frm.getListaTiposProducto().stream()
                    .filter(p -> id.equals(p.getId()))
                    .findFirst()
                    .orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TipoProducto value) {
        return (value != null && value.getId() != null) ? value.getId().toString() : "";
    }
}
