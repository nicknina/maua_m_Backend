package maua.moove.backend.demo.model;

public enum TipoVan {
    VAN("van"),
    MICRO_ONIBUS("micro_onibus");
    
    private String valor;
    
    TipoVan(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
}

