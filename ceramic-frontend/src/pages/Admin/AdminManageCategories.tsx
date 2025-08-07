import { useEffect, useState } from "react";
import { getAllCategorias, newCategoria, updateCategoria, deleteCategoria } from "../../api/categorias";
import type { Categoria, CategoriaInputDTO } from "../../types/categoria.types";

export default function AdminManageCategories() {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [newNombre, setNewNombre] = useState<string>('');
  const [editId, setEditId] = useState<number | null>(null);
  const [editNombre, setEditNombre] = useState<string>('');
  const [showNewInput, setShowNewInput] = useState<boolean>(false);

  const fetchCategorias = async () => {
    try {
      setLoading(true);
      const data = await getAllCategorias();
      setCategorias(data);
    } catch (error) {
      console.error(error);
      alert('Error al cargar categorías');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategorias();
  }, []);

  const handleCreate = async () => {
    if (!newNombre.trim()) return alert('El nombre no puede estar vacío');

    try {
      const dto: CategoriaInputDTO = { nombre: newNombre };
      await newCategoria(dto);
      setNewNombre('');
      setShowNewInput(false);
      await fetchCategorias();
    } catch (error) {
      console.error(error);
      alert('Error al crear categoría');
    }
  };

  const handleEdit = (id: number, nombre: string) => {
    setEditId(id);
    setEditNombre(nombre);
  };

  const handleUpdate = async () => {
    if (editId === null || !editNombre.trim()) return;

    try {
      const dto: CategoriaInputDTO = { nombre: editNombre };
      await updateCategoria(editId, dto);
      setEditId(null);
      setEditNombre('');
      await fetchCategorias();
    } catch (error) {
      console.error(error);
      alert('Error al actualizar categoría');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('¿Estás seguro de eliminar esta categoría?')) return;

    try {
      await deleteCategoria(id);
      await fetchCategorias();
    } catch (error) {
      console.error(error);
      alert('Error al eliminar categoría');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Categorías</h1>

      {loading ? (
        <p>Cargando...</p>
      ) : (
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {categorias.map((cat) => (
            <li key={cat.id} style={{ marginBottom: '1rem' }}>
              {editId === cat.id ? (
                <>
                  <input
                    type="text"
                    value={editNombre}
                    onChange={(e) => setEditNombre(e.target.value)}
                  />
                  <button onClick={handleUpdate}>Guardar</button>
                  <button onClick={() => setEditId(null)}>Cancelar</button>
                </>
              ) : (
                <>
                  <strong>{cat.nombre}</strong>
                  <button onClick={() => handleEdit(cat.id, cat.nombre)} style={{ marginLeft: '1rem' }}>
                    Editar
                  </button>
                  <button onClick={() => handleDelete(cat.id)} style={{ marginLeft: '0.5rem' }}>
                    Eliminar
                  </button>
                </>
              )}
            </li>
          ))}

          {/* Botón para añadir nueva categoría */}
          <li>
            {showNewInput ? (
              <>
                <input
                  type="text"
                  placeholder="Nombre de la nueva categoría"
                  value={newNombre}
                  onChange={(e) => setNewNombre(e.target.value)}
                />
                <button onClick={handleCreate}>Guardar</button>
                <button onClick={() => {
                  setShowNewInput(false);
                  setNewNombre('');
                }}>Cancelar</button>
              </>
            ) : (
              <button onClick={() => setShowNewInput(true)}>+ Añadir nueva categoría</button>
            )}
          </li>
        </ul>
      )}
    </div>
  );
}