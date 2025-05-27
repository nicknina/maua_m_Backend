package maua.moove.backend.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vans")
public class Van {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String placa;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVan tipo;
    
    @Column(nullable = false)
    private Integer capacidade;
    
    @Column
    private Boolean ativo = true;
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public TipoVan getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoVan tipo) {
        this.tipo = tipo;
    }
    
    public Integer getCapacidade() {
        return capacidade;
    }
    
    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}