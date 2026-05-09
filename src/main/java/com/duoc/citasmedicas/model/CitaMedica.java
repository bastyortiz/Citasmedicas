package com.duoc.citasmedicas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "citas_medicas")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_paciente", nullable = false, length = 100)
    private String nombrePaciente;

    @NotBlank
    @Size(max = 20)
    @Column(name = "rut_paciente", nullable = false, length = 20)
    private String rutPaciente;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_medico", nullable = false, length = 100)
    private String nombreMedico;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String especialidad;

    @NotNull
    @FutureOrPresent
    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm")
    @Column(name = "hora_cita", nullable = false, length = 5)
    private String horaCita;

    @NotBlank
    @Pattern(regexp = "PROGRAMADA|CANCELADA|REALIZADA|REAGENDADA", message = "Estado invalido")
    @Column(nullable = false, length = 30)
    private String estado;

    @NotBlank
    @Size(max = 255)
    @Column(name = "motivo_consulta", nullable = false, length = 255)
    private String motivoConsulta;

    public CitaMedica() {
    }

    public CitaMedica(Long id, String nombrePaciente, String rutPaciente, String nombreMedico,
                      String especialidad, LocalDate fechaCita, String horaCita, String estado,
                      String motivoConsulta) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.rutPaciente = rutPaciente;
        this.nombreMedico = nombreMedico;
        this.especialidad = especialidad;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.estado = estado;
        this.motivoConsulta = motivoConsulta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getRutPaciente() {
        return rutPaciente;
    }

    public void setRutPaciente(String rutPaciente) {
        this.rutPaciente = rutPaciente;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }
}
