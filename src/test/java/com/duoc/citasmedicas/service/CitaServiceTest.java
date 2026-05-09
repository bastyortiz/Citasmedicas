package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@ActiveProfiles("test")
class CitaServiceTest {

    @Autowired
    private CitaService citaService;

    @Autowired
    private CitaRepository citaRepository;

    @BeforeEach
    void setUp() {
        citaRepository.deleteAll();
    }

    @Test
    void creaUnaCitaEnBaseDeDatos() {
        CitaMedica cita = nuevaCita("PROGRAMADA", "09:30");

        CitaMedica creada = citaService.crear(cita);

        assertThat(creada.getId()).isNotNull();
        assertThat(citaRepository.findAll()).hasSize(1);
        assertThat(citaRepository.findAll().get(0).getNombrePaciente()).isEqualTo("Prueba Servicio");
    }

    @Test
    void actualizaYEliminaUnaCitaExistente() {
        CitaMedica guardada = citaRepository.save(nuevaCita("PROGRAMADA", "10:15"));
        CitaMedica actualizada = nuevaCita("REAGENDADA", "11:00");

        CitaMedica resultado = citaService.actualizar(guardada.getId(), actualizada);
        citaService.eliminar(guardada.getId());

        assertThat(resultado.getEstado()).isEqualTo("REAGENDADA");
        assertThat(resultado.getHoraCita()).isEqualTo("11:00");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> citaService.obtenerPorId(guardada.getId()));
        assertThat(exception.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private CitaMedica nuevaCita(String estado, String hora) {
        return new CitaMedica(
                null,
                "Prueba Servicio",
                "44444444-4",
                "Dra. Camila Torres",
                "Medicina General",
                LocalDate.now().plusDays(10),
                hora,
                estado,
                "Control preventivo"
        );
    }
}
