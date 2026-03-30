package com.duoc.citasmedicas.Controller;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.service.CitaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaController {
    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<CitaMedica> listar() { return citaService.obtenerTodas(); }

    @PostMapping
    public CitaMedica programar(@RequestBody CitaMedica cita) { return citaService.guardar(cita); }

    @DeleteMapping("/{id}")
    public void cancelarCita(@PathVariable Long id) { citaService.cancelar(id); }
}