package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.CompraDAO;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.InventarioDefaultDataAccess;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control.NotificadorKardex;
import po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity.Compra;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class RecepcionKardexFrm extends DefaultFrm<Compra, Long> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    CompraDAO compraDAO;

    @Inject
    CompraFrm compraFrm;

    @Inject
    NotificadorKardex notificadorKardex;

    List<Compra> comprasPagadas;
    UUID uuidRandom;

    public RecepcionKardexFrm() {}

    @PostConstruct
    public void init() {
        this.nombreBean = "Recibir Productos";
        this.setComprasPagadas(compraDAO.comprasPagadas());
        this.setUuidRandom(UUID.randomUUID());
    }

    @Override
    protected FacesContext getFacesContext() {
        return this.facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess<Compra, Long> getDAO() {
        return compraDAO;
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra nuevaCompra = new Compra();
        return nuevaCompra;
    }

    @Override
    protected String getIdAsText(Compra r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Compra getIdByText(String id) {
        if (id != null && this.modelo != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                Long buscado = Long.valueOf(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId() != null && r.getId().equals(buscado))
                        .findFirst()
                        .orElse(null);
            } catch (NumberFormatException e) {
                System.err.println("ID no es un número válido: " + id);
                return null;
            }
        }
        return null;
    }

    public List<Compra> getComprasPagadas() {
        return comprasPagadas;
    }

    public void setComprasPagadas(List<Compra> comprasPagadas) {
        this.comprasPagadas = comprasPagadas;
    }

    public CompraDAO getCompraDAO() {
        return compraDAO;
    }

    public void setCompraDAO(CompraDAO compraDAO) {
        this.compraDAO = compraDAO;
    }

    public NotificadorKardex getNotificadorKardex() {
        return notificadorKardex;
    }

    public void setNotificadorKardex(NotificadorKardex notificadorKardex) {
        this.notificadorKardex = notificadorKardex;
    }

    public CompraFrm getCompraFrm() {
        return compraFrm;
    }

    public void setCompraFrm(CompraFrm compraFrm) {
        this.compraFrm = compraFrm;
    }

    public UUID getUuidRandom() {
        return uuidRandom;
    }

    public void setUuidRandom(UUID uuidRandom) {
        this.uuidRandom = uuidRandom;
    }

    public void actualizarTabla(ActionEvent actionEvent) {
        System.out.println("Actualizando tabla");
    }
}
