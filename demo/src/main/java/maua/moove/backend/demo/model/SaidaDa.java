package maua.moove.backend.demo.model;

public enum SaidaDa {
    FACULDADE("faculdade"),
    ESTACAO("estacao");
    
    private String valor;
    
    SaidaDa(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
}