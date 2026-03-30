package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.model.CitaMedica;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CitaService {
    private List<CitaMedica> citas = new ArrayList<>();

    public CitaService() {
        citas.add(new CitaMedica(1L, "Bastian Ortiz", "Dr. House", "Diagnóstico", "2026-04-10", "09:00", "Programada", "Sala A"));
        citas.add(new CitaMedica(2L, "Ana Rios", "Dra. Grey", "General", "2026-04-10", "10:30", "Programada", "Sala B"));
        citas.add(new CitaMedica(3L, "Carlos M.", "Dr. Strange", "Neurología", "2026-04-11", "11:00", "Cancelada", "Sala C"));
        citas.add(new CitaMedica(4L, "Lucia H.", "Dr. Shaun Murphy", "Pediatría", "2026-04-11", "15:00", "Programada", "Sala A"));
        citas.add(new CitaMedica(5L, "Roberto V.", "Dra. Foster", "Psicología", "2026-04-12", "08:30", "Completada", "Sala D"));
        citas.add(new CitaMedica(6L, "Elena P.", "Dr. Ross", "Cardiología", "2026-04-12", "12:00", "Programada", "Sala B"));
        citas.add(new CitaMedica(7L, "Mario T.", "Dra. Cuddy", "Endocrinología", "2026-04-13", "16:00", "Programada", "Sala C"));
        citas.add(new CitaMedica(8L, "Sofia L.", "Dr. Wilson", "Oncología", "2026-04-13", "17:30", "Programada", "Sala D"));
    }

    public List<CitaMedica> obtenerTodas() { return citas; }

    public CitaMedica guardar(CitaMedica cita) {
        citas.add(cita);
        return cita;
    }

    public void cancelar(Long id) {
        citas.stream().filter(c -> c.getId().equals(id)).findFirst()
             .ifPresent(c -> c.setEstadoCita("Cancelada"));
    }
}