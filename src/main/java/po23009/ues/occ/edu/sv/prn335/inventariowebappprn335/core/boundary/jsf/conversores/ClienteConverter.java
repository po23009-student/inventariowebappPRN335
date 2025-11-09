package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf.conversores;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.ClienteDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Cliente;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Proveedor;

@FacesConverter("clienteConverter")
public class ClienteConverter implements Converter {

    private ClienteDAO clienteDAO;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;

        if (clienteDAO == null) {
            clienteDAO = CDI.current().select(ClienteDAO.class).get();
        }

        try {
            Integer id = Integer.valueOf(value);
            return clienteDAO.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        Cliente cliente = (Cliente) value;
        return cliente.getId() != null ? cliente.getId().toString() : "";
    }

}
