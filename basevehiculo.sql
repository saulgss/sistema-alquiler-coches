-- MySQL Script generated by MySQL Workbench
-- 04/07/16 12:50:12
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema Vehiculos
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Vehiculos
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Vehiculos` DEFAULT CHARACTER SET utf8 ;
USE `Vehiculos` ;

-- -----------------------------------------------------
-- Table `Vehiculos`.`Vehiculo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`Vehiculo` (
  `idVehiculo` INT NOT NULL,
  `matricula` VARCHAR(7),
  `modelo` VARCHAR(45) NOT NULL,
  `caracteristicas` VARCHAR(250) NULL,
  `precio_diario` DECIMAL(7,2) NOT NULL,
  `estado` VARCHAR(15) NOT NULL,
  `tipo_vehiculo` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`matricula`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vehiculos`.`persona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`persona` (
  `dni` VARCHAR(11) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `apellidos` VARCHAR(70) NOT NULL,
  `direccion` VARCHAR(100) NULL,
  `telefono` INT(12) NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`dni`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `telefono_UNIQUE` (`telefono` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vehiculos`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`cliente` (
  `dni` VARCHAR(11) NOT NULL,
  PRIMARY KEY (`dni`),
  CONSTRAINT `fk_persona_cliente`
    FOREIGN KEY (`dni`)
    REFERENCES `Vehiculos`.`persona` (`dni`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vehiculos`.`trabajador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`trabajador` (
  `dni` VARCHAR(11) NOT NULL,
  `sueldo` DECIMAL(7,2) NOT NULL,
  `acceso` VARCHAR(45) NOT NULL,
  `usuario` VARCHAR(15) NOT NULL,
  `contrasena` VARCHAR(25) NOT NULL,
  `estado` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`dni`),
  CONSTRAINT `fk_persona_trabajador`
    FOREIGN KEY (`dni`)
    REFERENCES `Vehiculos`.`persona` (`dni`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vehiculos`.`extras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`extras` (
  `idextra` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(255) NULL,
  `precio_alquiler` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`idextra`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vehiculos`.`reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`reserva` (
  `idreserva` INT NOT NULL,
  `matricula_vehiculo` VARCHAR(7) NOT NULL,
  `dni_cliente` VARCHAR(11) NOT NULL,
  `dni_trabajador` VARCHAR(11) NOT NULL,
  `idextra` INT,
  `tipo_tarifa` varchar (11) NOT NULL,
  `fecha_reserva` DATE NOT NULL,
  `fecha_recogida` DATE,
  `fecha_devolucion` DATE,
  `costo_alquiler` DECIMAL(7,2) NOT NULL,
  `estado` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`idreserva`),
  INDEX `fk_reserva_vehiculo_idx` (`matricula_vehiculo` ASC),
  INDEX `fk_reserva_cliente_idx` (`dni_cliente` ASC),
  INDEX `fk_reserva_trabajador_idx` (`dni_trabajador` ASC),
  INDEX `fk_reserva_extra_idx` (`idextra` ASC),
  CONSTRAINT `fk_reserva_vehiculo`
    FOREIGN KEY (`matricula_vehiculo`)
    REFERENCES `Vehiculos`.`Vehiculo` (`matricula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reserva_cliente`
    FOREIGN KEY (`dni_cliente`)
    REFERENCES `Vehiculos`.`cliente` (`dni`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reserva_trabajador`
    FOREIGN KEY (`dni_trabajador`)
    REFERENCES `Vehiculos`.`trabajador` (`dni`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reserva_extra`
    FOREIGN KEY (`idextra`)
    REFERENCES `Vehiculos`.`Extras` (`idextra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Vehiculos`.`pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vehiculos`.`pago` (
  `numero_factura` VARCHAR(20) NOT NULL,
  `idreserva` INT NOT NULL,
  `tipo_pago` VARCHAR(20) NOT NULL,
  `iva` DECIMAL(4,2) NOT NULL,
  `total_pago` DECIMAL(7,2) NOT NULL,
  `fecha_emision` DATE NOT NULL,
  `fecha_pago` DATE NOT NULL,
  PRIMARY KEY (`numero_factura`),
  INDEX `fk_pago_reserva_idx` (`idreserva` ASC),
  CONSTRAINT `fk_pago_reserva`
    FOREIGN KEY (`idreserva`)
    REFERENCES `Vehiculos`.`reserva` (`idreserva`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Introducción de datos
-- -----------------------------------------------------

INSERT INTO `Vehiculos`.`extras` VALUES
(1,'Ninguno','Seleccionar cuando el cliente no elige extra',0);

INSERT INTO `Vehiculos`.`persona` (dni, nombre, apellidos) VALUES
('74321353G','admin','ad');

INSERT INTO `Vehiculos`.`trabajador` VALUES
('74321353G',0,'Administrador','admin','admin','A');