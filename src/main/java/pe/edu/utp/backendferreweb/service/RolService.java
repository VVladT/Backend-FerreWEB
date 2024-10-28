package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.model.Rol;
import pe.edu.utp.backendferreweb.repository.RolRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository repository;

    public List<Rol> findAll() {
        return repository.findAll();
    }
}
