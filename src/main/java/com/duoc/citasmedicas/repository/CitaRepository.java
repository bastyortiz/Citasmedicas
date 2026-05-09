package com.duoc.citasmedicas.repository;

import com.duoc.citasmedicas.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<CitaMedica, Long> {
}
