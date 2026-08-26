package com.example.testclaro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_ser")
    private Long idTipoSer;

    @Column(name = "nombre_servicio", nullable = false, length = 100)
    private String nombreServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_estado", nullable = false)
    private Estado estado;

    public Long getIdTipoSer() {
        return idTipoSer;
    }

    public void setIdTipoSer(Long idTipoSer) {
        this.idTipoSer = idTipoSer;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
