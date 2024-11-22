package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.persistence.repository.AlmacenRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlmacenService {
    private final AlmacenRepository almacenRepository;

    @Transactional
    public List<Almacen> obtenerTodos() {
        return almacenRepository.findAllActive();
    }

    public Almacen obtenerPorId(Integer id) {
        Almacen almacen = almacenRepository.findActiveById(id);

        if (almacen == null) throw new EntityNotFoundException("No existe almacen con id: " + id);

        return almacen;
    }

    public Almacen crearAlmacen(AlmacenRequest request) {
        String nombre = request.getNombre();
        String direccion = request.getDireccion();

        if (almacenRepository.existsByNombre(nombre)) {
            throw new EntityExistsException("Ya existe un almacen con nombre: " + nombre);
        } else {
            Almacen almacen = Almacen.builder()
                    .nombre(nombre)
                    .direccion(direccion)
                    .build();
            return almacenRepository.save(almacen);
        }
    }

    public Almacen editarAlmacen(Integer id, AlmacenRequest request) {
        String nuevoNombre = request.getNombre();
        String nuevaDireccion = request.getDireccion();

        Almacen almacenParaActualizar = almacenRepository.findActiveById(id);

        if (almacenParaActualizar == null) throw new EntityNotFoundException("El almacen no existe.");

        if (almacenParaActualizar.getNombre().equals(nuevoNombre) &&
                almacenRepository.existsByNombre(nuevoNombre)) {
            throw new EntityExistsException("El almacen con el nombre: "
                    + nuevoNombre + " ya existe.");
        } else {
            almacenParaActualizar.setNombre(nuevoNombre);
            almacenParaActualizar.setDireccion(nuevaDireccion);

            return almacenRepository.save(almacenParaActualizar);
        }
    }

    public void eliminarAlmacen(Integer id) {
        Almacen almacenParaEliminar = almacenRepository.findActiveById(id);

        if (almacenParaEliminar == null) throw new EntityNotFoundException("El almacen no existe.");

        almacenParaEliminar.setFechaEliminado(LocalDateTime.now());
        almacenRepository.save(almacenParaEliminar);
    }

    public Almacen obtenerPorNombre(String nombre) {
        return almacenRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("El almacen con dicho nombre no existe."));
    }
}
