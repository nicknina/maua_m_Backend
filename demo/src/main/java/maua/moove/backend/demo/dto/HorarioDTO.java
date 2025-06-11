package maua.moove.backend.demo.dto;


public class HorarioDTO {
    private Long id;
    private Long vanId;
    private String tipoVeiculo; // van ou micro_onibus
    private String saidaDa; // faculdade ou estacao
    private String horario; // formato "HH:MM"
    private String diaDaSemana; // segunda, terça, etc.
    private String placa; // informação extra
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getVanId() {
        return vanId;
    }
    
    public void setVanId(Long vanId) {
        this.vanId = vanId;
    }
    
    public String getTipoVeiculo() {
        return tipoVeiculo;
    }
    
    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
    
    public String getSaidaDa() {
        return saidaDa;
    }
    
    public void setSaidaDa(String saidaDa) {
        this.saidaDa = saidaDa;
    }
    
    public String getHorario() {
        return horario;
    }
    
    public void setHorario(String horario) {
        this.horario = horario;
    }
    
    public String getDiaDaSemana() {
        return diaDaSemana;
    }
    
    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
