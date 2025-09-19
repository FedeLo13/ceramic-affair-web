package es.uca.tfg.ceramic_affair_web.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import es.uca.tfg.ceramic_affair_web.repositories.SuscriptorRepo;
import jakarta.transaction.Transactional;

/**
 * Servicio para la limpieza de suscriptores no verificados.
 * Este servicio se encarga de eliminar los suscriptores que no han sido verificados
 * y cuyo token de verificación ha expirado.
 * 
 * @version 1.0
 */
@Service
public class SuscriptorCleanupService {

    @Autowired
    private SuscriptorRepo suscriptorRepo;

    /**
     * Método para limpiar suscriptores no verificados.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Ejecuta diariamente a la medianoche
    @Transactional
    public void eliminarSuscriptoresNoVerificados() {
        suscriptorRepo.deleteByVerificadoFalseAndFechaExpiracionTokenBefore(LocalDateTime.now());
    }
}
