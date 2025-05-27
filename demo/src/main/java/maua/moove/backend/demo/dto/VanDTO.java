package maua.moove.backend.demo.dto;

public class VanDTO {
    private Long id;
    private String placa;
    private String tipo;
    private Integer capacidade;
    private Boolean ativo;
    
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
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
