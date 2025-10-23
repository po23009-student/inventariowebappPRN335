package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "kardex_detalle")
public class KardexDetalle {
    @Id
    @Column(name = "id_kardex_detalle", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kardex")
    private Kardex idKardex;

    @Lob
    @Column(name = "lote")
    private String lote;

    @Column(name = "activo")
    private Boolean activo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Kardex getIdKardex() {
        return idKardex;
    }

    public void setIdKardex(Kardex idKardex) {
        this.idKardex = idKardex;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}