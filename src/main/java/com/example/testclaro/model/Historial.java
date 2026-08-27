package com.example.testclaro.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long idRegistro;

    @ManyToOne
    @JoinColumn(name= "fk_orden", nullable= false)
    private Orden orden;

    @ManyToOne
    @JoinColumn(name= "fk_estado_anterior")
    private Estado estadoAnterior;

    @ManyToOne
    @JoinColumn(name= "fk_estado_nuevo")
    private Estado estadoNuevo;

    @Column(name= "date_created", insertable= false, updatable= false)
    private LocalDateTime dateCreated;

    public Long getIdRegistro () { return idRegistro; }
    public void setIdRegistro (Long idRegistro) { this.idRegistro= idRegistro; }

    public Orden getOrden () { return orden; }
    public void setOrden ( Orden orden) { this.orden= orden; }

    public Estado getEstadoAnterior () { return estadoAnterior; }
    public void setEstadoAnterior (Estado estadoAnterior) { this.estadoAnterior= estadoAnterior;}

    public Estado getEstadoNuevo () { return estadoNuevo; }
    public void setEstadoNuevo (Estado estadoNuevo) { this.estadoNuevo= estadoNuevo;}

    public LocalDateTime getDateCreated () { return dateCreated; }
}

