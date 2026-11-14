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
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,name = "tipo_operacion")
    private String tipoOperacion;
    @Column(nullable = false)
    private java.time.LocalDate fecha;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;
    @Column(name = "entidad_afectada", nullable = false)
    private String entidadAfectada;
    @Column(name = "valores_anteriores")
    private String valoresAnteriores;
    @Column(name = "valores_nuevos")
    private String valoresNuevos;
}
