package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.AlmacenFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoAlmacen;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

@FacesConverter(value="tipoAlmacenConverter")
public class TipoAlmacenConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        Integer id = Integer.valueOf(value);

        AlmacenFrm bean = context.getApplication()
                .evaluateExpressionGet(context, "#{almacenFrm}", AlmacenFrm.class);

        return bean.getListaTiposAlmacen().stream()
                .filter(tu -> tu.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null) return "";
        return ((TipoAlmacen)value).getId().toString();
    }
}
