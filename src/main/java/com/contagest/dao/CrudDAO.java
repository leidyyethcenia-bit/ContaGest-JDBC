package com.contagest.dao;

import java.util.List;
import java.util.Optional;

/**
 * Operaciones basicas CRUD que debe ofrecer cualquier DAO del sistema.
 *
 * @param <T>  tipo de entidad
 * @param <ID> tipo de la llave primaria
 */
public interface CrudDAO<T, ID> {

    /** Inserta la entidad y retorna el identificador generado. */
    ID insertar(T entidad);

    /** Consulta una entidad por su llave primaria. */
    Optional<T> consultarPorId(ID id);

    /** Consulta todas las entidades registradas. */
    List<T> consultarTodos();

    /** Actualiza la entidad; retorna true si se modifico algun registro. */
    boolean actualizar(T entidad);

    /** Elimina la entidad; retorna true si se elimino algun registro. */
    boolean eliminar(ID id);
}
