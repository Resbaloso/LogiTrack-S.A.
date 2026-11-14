package com.logitracksa.logitracksaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private java.time.LocalDate fecha;
    @Column(nullable = false, name = "tipo_movimiento")
    private String tipoMovimiento;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "bodega_origen_id")
    private Bodega bodegaOrigen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "bodega_destino_id")
    private Bodega bodegaDestino;
}
