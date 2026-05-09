package com.duoc.citasmedicas.controller;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CitaRepository citaRepository;

    @BeforeEach
    void setUp() {
        citaRepository.deleteAll();
    }

    @Test
    void getCitasDevuelveColeccionConHateoas() throws Exception {
        CitaMedica guardada = citaRepository.save(new CitaMedica(
                null,
                "Bastian Ortiz",
                "11111111-1",
                "Dra. Ana Lopez",
                "Medicina General",
                LocalDate.now().plusDays(5),
                "10:30",
                "PROGRAMADA",
                "Control general"
        ));

        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.citaMedicaList[0].nombrePaciente").value("Bastian Ortiz"))
                .andExpect(jsonPath("$._embedded.citaMedicaList[0]._links.self.href", endsWith("/citas/" + guardada.getId())))
                .andExpect(jsonPath("$._links.self.href", endsWith("/citas")));
    }

    @Test
    void postCitasCreaRegistroYDevuelveLinks() throws Exception {
        String body = """
                {
                  \"nombrePaciente\": \"Prueba Video\",
                  \"rutPaciente\": \"44444444-4\",
                  \"nombreMedico\": \"Dra. Camila Torres\",
                  \"especialidad\": \"Medicina General\",
                  \"fechaCita\": \"%s\",
                  \"horaCita\": \"09:30\",
                  \"estado\": \"PROGRAMADA\",
                  \"motivoConsulta\": \"Consulta de prueba\"
                }
                """.formatted(LocalDate.now().plusDays(7));

        mockMvc.perform(post("/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombrePaciente").value("Prueba Video"))
                .andExpect(jsonPath("$._links.self.href", matchesPattern(".*/citas/\\d+$")))
                .andExpect(jsonPath("$._links.collection.href", endsWith("/citas")));
    }
}
