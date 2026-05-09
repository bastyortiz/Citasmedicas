package com.duoc.citasmedicas.Controller;

import com.duoc.citasmedicas.assembler.CitaModelAssembler;
import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final CitaModelAssembler assembler;

    public CitaController(CitaService citaService, CitaModelAssembler assembler) {
        this.citaService = citaService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<CitaMedica>> listar() {
        List<EntityModel<CitaMedica>> citas = citaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(citas,
                linkTo(methodOn(CitaController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CitaMedica> obtenerPorId(@PathVariable Long id) {
        return assembler.toModel(citaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<CitaMedica>> crear(@Valid @RequestBody CitaMedica citaMedica) {
        CitaMedica creada = citaService.crear(citaMedica);
        EntityModel<CitaMedica> model = assembler.toModel(creada);
        return ResponseEntity.created(linkTo(methodOn(CitaController.class).obtenerPorId(creada.getId())).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CitaMedica>> actualizar(@PathVariable Long id,
                                                               @Valid @RequestBody CitaMedica citaMedica) {
        CitaMedica actualizada = citaService.actualizar(id, citaMedica);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
