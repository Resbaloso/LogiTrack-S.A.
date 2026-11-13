package com.logitracksa.logitracksaapi.model;

import jakarta.persistence.*;

public class Bodega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String ubicacion;
    @Column(nullable = false)
    private int capacidad;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;
}
