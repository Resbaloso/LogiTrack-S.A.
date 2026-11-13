package com.logitracksa.logitracksaapi.model;

import jakarta.persistence.*;

public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "bodega_id")
    private Bodega bodega;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "producto_id")
    private Producto producto;
    @Column(nullable = false)
    private int stock;
}
