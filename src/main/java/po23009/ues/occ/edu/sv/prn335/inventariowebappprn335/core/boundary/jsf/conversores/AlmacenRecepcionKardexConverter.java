package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.KardexDetalleFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Almacen;

@FacesConverter("almacenRKConverter")
public class AlmacenRecepcionKardexConverter implements Converter<Almacen> {

    @Override
    public Almacen getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        KardexDetalleFrm frm = context.getApplication().evaluateExpressionGet(context, "#{kardexDetalleFrm}", KardexDetalleFrm.class);

        try {
            Integer id = Integer.valueOf(value);

            return frm.getListaAlmacenes().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Almacen value) {
        return (value != null && value.getId() != null)
                ? value.getId().toString()
                : "";
    }
}