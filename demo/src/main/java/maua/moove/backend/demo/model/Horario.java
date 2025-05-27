package maua.moove.backend.demo.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "horarios")
public class Horario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "van_id", nullable = false)
    private Van van;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "saida_da", nullable = false)
    private SaidaDa saidaDa;
    
    @Column(name = "horario", nullable = false)
    private LocalTime horario;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_da_semana", nullable = false)
    private DiaDaSemana diaDaSemana;
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Van getVan() {
        return van;
    }
    
    public void setVan(Van van) {
        this.van = van;
    }
    
    public SaidaDa getSaidaDa() {
        return saidaDa;
    }
    
    public void setSaidaDa(SaidaDa saidaDa) {
        this.saidaDa = saidaDa;
    }
    
    public LocalTime getHorario() {
        return horario;
    }
    
    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }
    
    public DiaDaSemana getDiaDaSemana() {
        return diaDaSemana;
    }
    
    public void setDiaDaSemana(DiaDaSemana diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }
}
