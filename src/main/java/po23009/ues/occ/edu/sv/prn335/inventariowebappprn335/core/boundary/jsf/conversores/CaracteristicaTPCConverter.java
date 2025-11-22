package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.TipoProductoCaracteristicaFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Caracteristica;

import java.util.UUID;

@FacesConverter("caracteristicaTPCConverter")
public class CaracteristicaTPCConverter implements Converter<Caracteristica> {

    @Override
    public Caracteristica getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        TipoProductoCaracteristicaFrm frm = context.getApplication()
                .evaluateExpressionGet(context, "#{tipoProductoCaracteristicaFrm}", TipoProductoCaracteristicaFrm.class);

        try {
            Integer id = Integer.valueOf(value);

            return frm.getListaCaracteristicas().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Caracteristica value) {
        return (value != null && value.getId() != null)
                ? value.getId().toString()
                : "";
    }
}