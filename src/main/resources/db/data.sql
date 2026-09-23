-- =====================================================================
--  SCRIPT DE DATOS DE EJEMPLO: 10 coches
--  Proyecto: MonolitoCoche
--
--  Ejecutar DESPUÉS de schema.sql.
--  INSERT IGNORE evita errores si los datos ya existen (la matrícula es
--  única), así el script puede ejecutarse en cada arranque sin duplicar.
-- =====================================================================

USE monolito_coche;

INSERT IGNORE INTO coches
    (marca, modelo, matricula, anio, color, precio, kilometraje, combustible, transmision)
VALUES
    ('Toyota',     'Corolla',   '1234KLM', 2021, 'Blanco',   21500.00,  35000, 'HIBRIDO',   'AUTOMATICA'),
    ('Seat',       'León',      '5678JKR', 2019, 'Rojo',     14990.00,  62000, 'GASOLINA',  'MANUAL'),
    ('Volkswagen', 'Golf',      '9012LBC', 2022, 'Gris',     24900.00,  18000, 'DIESEL',    'MANUAL'),
    ('Tesla',      'Model 3',   '3456MNP', 2023, 'Negro',    38900.00,   9500, 'ELECTRICO', 'AUTOMATICA'),
    ('Renault',    'Clio',      '7890HGT', 2018, 'Azul',      9800.00,  84000, 'GLP',       'MANUAL'),
    ('BMW',        'Serie 3',   '2468KZX', 2020, 'Negro',    29500.00,  54000, 'DIESEL',    'AUTOMATICA'),
    ('Kia',        'Niro',      '1357LTV', 2022, 'Verde',    23700.00,  27000, 'HIBRIDO',   'AUTOMATICA'),
    ('Peugeot',    '208',       '8642JFD', 2017, 'Amarillo',  8900.00,  98000, 'GASOLINA',  'MANUAL'),
    ('Hyundai',    'Kona',      '9753MBS', 2024, 'Blanco',   33400.00,   4200, 'ELECTRICO', 'AUTOMATICA'),
    ('Ford',       'Focus',     '1122HWN', 2016, 'Plata',     7500.00, 132000, 'DIESEL',    'MANUAL');
