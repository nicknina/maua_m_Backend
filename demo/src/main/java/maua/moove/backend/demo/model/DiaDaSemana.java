package maua.moove.backend.demo.model;

public enum DiaDaSemana {
    SEGUNDA("segunda"),
    TERCA("terça"),
    QUARTA("quarta"),
    QUINTA("quinta"),
    SEXTA("sexta"),
    SABADO("sábado"),
    DOMINGO("domingo");
    
    private String valor;
    
    DiaDaSemana(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
}
