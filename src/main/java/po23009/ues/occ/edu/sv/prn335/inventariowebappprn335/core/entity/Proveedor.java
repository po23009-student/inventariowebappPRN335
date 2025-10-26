package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer id;

    @Size(min=15, max = 155, message = "El nombre debe tener entre 15 y 155 caracteres")
    @Column(name = "nombre", length = 155)
    @NotNull(message = "El nombre es obligatorio")
    private String nombre;

    @Size(max = 155)
    @Column(name = "razon_social", length = 155)
    private String razonSocial;

    @Pattern(regexp = "(^$|\\d{14})", message = "El NIT solo debe contener números. Debe tener 14 dígitos")
    @Column(name = "nit", length = 14)
    private String nit;

    @Column(name = "activo")
    private Boolean activo;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}