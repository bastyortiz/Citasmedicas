package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Transactional(readOnly = true)
    public List<CitaMedica> obtenerTodas() {
        return citaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CitaMedica obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
    }

    @Transactional
    public CitaMedica crear(CitaMedica citaMedica) {
        citaMedica.setId(null);
        return citaRepository.save(citaMedica);
    }

    @Transactional
    public CitaMedica actualizar(Long id, CitaMedica citaActualizada) {
        CitaMedica existente = obtenerPorId(id);
        existente.setNombrePaciente(citaActualizada.getNombrePaciente());
        existente.setRutPaciente(citaActualizada.getRutPaciente());
        existente.setNombreMedico(citaActualizada.getNombreMedico());
        existente.setEspecialidad(citaActualizada.getEspecialidad());
        existente.setFechaCita(citaActualizada.getFechaCita());
        existente.setHoraCita(citaActualizada.getHoraCita());
        existente.setEstado(citaActualizada.getEstado());
        existente.setMotivoConsulta(citaActualizada.getMotivoConsulta());
        return citaRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        CitaMedica existente = obtenerPorId(id);
        citaRepository.delete(existente);
    }
}
