package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.VentaDetalleFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Producto;

import java.util.UUID;

@FacesConverter("productoVentaConverter")
public class ProductoVentaConverter implements Converter<Producto> {

    @Override
    public Producto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        VentaDetalleFrm frm = context.getApplication()
                .evaluateExpressionGet(context, "#{ventaDetalleFrm}", VentaDetalleFrm.class);

        try {
            UUID id = UUID.fromString(value);

            return frm.getListaProductos().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Producto value) {
        return (value != null && value.getId() != null)
                ? value.getId().toString()
                : "";
    }
}
