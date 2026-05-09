INSERT INTO citas_medicas (
    nombre_paciente,
    rut_paciente,
    nombre_medico,
    especialidad,
    fecha_cita,
    hora_cita,
    estado,
    motivo_consulta
) VALUES (
    'Bastian Ortiz',
    '11111111-1',
    'Dra. Ana Lopez',
    'Medicina General',
    DATEADD('DAY', 5, CURRENT_DATE),
    '10:30',
    'PROGRAMADA',
    'Control general'
);

INSERT INTO citas_medicas (
    nombre_paciente,
    rut_paciente,
    nombre_medico,
    especialidad,
    fecha_cita,
    hora_cita,
    estado,
    motivo_consulta
) VALUES (
    'Carla Henriquez',
    '22222222-2',
    'Dr. Pedro Rojas',
    'Traumatologia',
    DATEADD('DAY', 6, CURRENT_DATE),
    '12:00',
    'PROGRAMADA',
    'Dolor de rodilla'
);

INSERT INTO citas_medicas (
    nombre_paciente,
    rut_paciente,
    nombre_medico,
    especialidad,
    fecha_cita,
    hora_cita,
    estado,
    motivo_consulta
) VALUES (
    'Juan Perez',
    '33333333-3',
    'Dra. Maria Soto',
    'Dermatologia',
    DATEADD('DAY', 7, CURRENT_DATE),
    '15:30',
    'CANCELADA',
    'Control de piel'
);
