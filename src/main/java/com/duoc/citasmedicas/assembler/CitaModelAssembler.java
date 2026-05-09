package com.duoc.citasmedicas.assembler;

import com.duoc.citasmedicas.Controller.CitaController;
import com.duoc.citasmedicas.model.CitaMedica;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CitaModelAssembler implements RepresentationModelAssembler<CitaMedica, EntityModel<CitaMedica>> {

    @Override
    public EntityModel<CitaMedica> toModel(CitaMedica citaMedica) {
        return EntityModel.of(citaMedica,
                linkTo(methodOn(CitaController.class).obtenerPorId(citaMedica.getId())).withSelfRel(),
                linkTo(methodOn(CitaController.class).listar()).withRel("collection"));
    }
}
