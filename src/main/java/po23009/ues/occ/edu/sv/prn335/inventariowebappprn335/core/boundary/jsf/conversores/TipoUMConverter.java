package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.CaracteristicaFrm;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoUnidadMedida;

@FacesConverter(value="tipoUMConverter")
public class TipoUMConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        Integer id = Integer.valueOf(value);

        CaracteristicaFrm bean = context.getApplication()
                .evaluateExpressionGet(context, "#{caracteristicaFrm}", CaracteristicaFrm.class);

        return bean.getListaTiposUnidadMedida().stream()
                .filter(tu -> tu.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null) return "";
        return ((TipoUnidadMedida)value).getId().toString();
    }
}
