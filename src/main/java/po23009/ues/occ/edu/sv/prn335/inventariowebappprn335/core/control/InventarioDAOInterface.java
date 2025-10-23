package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import java.util.List;

public interface InventarioDAOInterface<T> {
    void crear(T registro) throws IllegalArgumentException, IllegalAccessException;

    void modificar(T registro) throws IllegalArgumentException;

    T leer(int id) throws IllegalArgumentException, IllegalAccessException;

    void eliminar(T registro) throws IllegalArgumentException, IllegalAccessException;

    List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException;

    int count() throws IllegalArgumentException;
}
