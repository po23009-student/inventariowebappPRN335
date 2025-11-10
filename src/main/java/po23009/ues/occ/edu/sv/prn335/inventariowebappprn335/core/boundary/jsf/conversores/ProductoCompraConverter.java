package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.CompraDetalleFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

import java.util.UUID;

@FacesConverter("productoCompraConverter")
public class ProductoCompraConverter implements Converter<Producto> {

    @Override
    public Producto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        CompraDetalleFrm frm = context.getApplication()
                .evaluateExpressionGet(context, "#{compraDetalleFrm}", CompraDetalleFrm.class);

        try {
            UUID id = UUID.fromString(value);
            return frm.getListaProductos().stream()
                    .filter(p -> id.equals(p.getId()))
                    .findFirst()
                    .orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Producto value) {
        return (value != null && value.getId() != null) ? value.getId().toString() : "";
    }
}