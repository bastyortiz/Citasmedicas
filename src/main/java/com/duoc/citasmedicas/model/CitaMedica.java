package com.duoc.citasmedicas.model;

public class CitaMedica {
    private Long id;
    private String paciente;
    private String doctor;
    private String especialidad;
    private String fecha;
    private String hora;
    private String estadoCita; // Programada, Cancelada, Completada
    private String sala;

    public CitaMedica() {}

    public CitaMedica(Long id, String paciente, String doctor, String especialidad, 
                      String fecha, String hora, String estadoCita, String sala) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.hora = hora;
        this.estadoCita = estadoCita;
        this.sala = sala;
    }

    // Getters y Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }
    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getEstadoCita() { return estadoCita; }
    public void setEstadoCita(String estadoCita) { this.estadoCita = estadoCita; }
    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }
}