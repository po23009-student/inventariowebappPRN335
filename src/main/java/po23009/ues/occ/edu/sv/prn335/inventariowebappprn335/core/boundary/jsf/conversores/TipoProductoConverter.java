package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.TipoProductoDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.TipoProducto;


@FacesConverter(value = "tipoProductoConverter", managed = true)
public class TipoProductoConverter implements Converter<TipoProducto> {


    @Inject
    private TipoProductoDAO tipoProductoDAO;

    @Override
    public TipoProducto getAsObject(FacesContext context, UIComponent component, String submittedValue) {


        if (submittedValue == null || submittedValue.isEmpty() || submittedValue.equals("NINGUNO")) {
            return null;
        }

        try {
            Long id = Long.valueOf(submittedValue);


            TipoProducto resultado = tipoProductoDAO.find(id);

            if (resultado == null) {

                throw new RuntimeException("Registro no encontrado en DB.");
            }
            return resultado;

        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error de Conversión", "ID no válido."));
        } catch (Exception e) {

            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error de Búsqueda", "No se pudo encontrar el Tipo Producto con ID: " + submittedValue));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TipoProducto modelValue) {
        if (modelValue == null || modelValue.getId() == null) {
            return "";
        }
        return String.valueOf(modelValue.getId());
    }
}
