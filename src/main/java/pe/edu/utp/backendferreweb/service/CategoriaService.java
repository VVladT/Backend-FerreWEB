package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.model.Categoria;
import pe.edu.utp.backendferreweb.repository.CategoriaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository repository;

    public List<Categoria> listar() {
        return repository.findAll();
    }
}
