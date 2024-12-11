package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.AuditoriaLog;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.repository.AuditoriaRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditoriaService {
    private final AuditoriaRepository auditoriaRepository;

    public void registerAudit(Integer idEntidad,
                              String entidad,
                              String accion,
                              String motivo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (usuario == null)
            throw new IllegalStateException("No hay usuario autenticado, imposible continuar la operación.");

        AuditoriaLog registro = AuditoriaLog.builder()
                .idEntidad(idEntidad)
                .nombreEntidad(entidad)
                .accion(accion)
                .motivo(motivo)
                .fechaCambio(LocalDateTime.now())
                .usuario(usuario)
                .build();

        auditoriaRepository.save(registro);
    }
}
