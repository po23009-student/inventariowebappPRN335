package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335.core.control;

import java.util.List;

public interface InventarioDAOInterface<T, ID> {
    void crear(T registro) throws IllegalArgumentException, IllegalAccessException;

    void modificar(T registro) throws IllegalArgumentException;

    void eliminar(T registro) throws IllegalArgumentException, IllegalAccessException;

    List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException;

    T find(ID id);

    int count() throws IllegalArgumentException;
}
