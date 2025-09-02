import { useEffect, useState } from "react";
import { getAllCategorias, newCategoria, updateCategoria, deleteCategoria } from "../../api/categorias";
import type { Categoria, CategoriaInputDTO } from "../../types/categoria.types";
import "./AdminManageCategories.css";
import { FaEdit, FaPlus, FaTrash } from "react-icons/fa";
import { ValidationError } from "../../api/utils";

export default function AdminManageCategories() {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [newNombre, setNewNombre] = useState<string>('');
  const [editId, setEditId] = useState<number | null>(null);
  const [editNombre, setEditNombre] = useState<string>('');
  const [showNewInput, setShowNewInput] = useState<boolean>(false);

  const [message, setMessage] = useState<string | null>(null);
  const [confirmDeleteId, setConfirmDeleteId] = useState<number | null>(null);
  const [visibleMessage, setVisibleMessage] = useState(false);

  useEffect(() => {
    if (message) {
      setVisibleMessage(true);

      // Auto-cierre después de 3 segundos
      const timer = setTimeout(() => setVisibleMessage(false), 3000);

      return () => clearTimeout(timer);
    }
  }, [message]);

  // Cuando termina la animación, limpia el message
  const handleTransitionEnd = () => {
    if (!visibleMessage) setMessage(null);
  };

  const fetchCategorias = async () => {
    try {
      setLoading(true);
      const data = await getAllCategorias();
      setCategorias(data);
    } catch (error) {
      console.error(error);
      setMessage('Error fetching categories, please contact support');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategorias();
  }, []);

  const handleCreate = async () => {
    if (!newNombre.trim()) {
      setMessage('Category name cannot be empty');
      return;
    }

    try {
      const dto: CategoriaInputDTO = { nombre: newNombre };
      await newCategoria(dto);
      setNewNombre('');
      setShowNewInput(false);
      await fetchCategorias();
    } catch (error) {
      console.error(error);
      if (error instanceof ValidationError) {
        setMessage(error.message);
      } else if (error instanceof Error) {
        setMessage(error.message);
      } else {
        setMessage('Unknown error while creating category, please contact support');
      }
    }
  };

  const handleEdit = (id: number, nombre: string) => {
    setEditId(id);
    setEditNombre(nombre);
  };

  const handleUpdate = async () => {
    if (editId === null || !editNombre.trim()) {
      setMessage('Category name cannot be empty');
      return;
    }

    try {
      const dto: CategoriaInputDTO = { nombre: editNombre };
      await updateCategoria(editId, dto);
      setEditId(null);
      setEditNombre('');
      await fetchCategorias();
    } catch (error) {
      console.error(error);
      if (error instanceof ValidationError) {
        setMessage(error.message);
      } else if (error instanceof Error) {
        setMessage(error.message);
      } else {
        setMessage('Unknown error while updating category, please contact support');
      }
    }
  };

  const handleDeleteClick = async (id: number) => {
    setConfirmDeleteId(id);
  };

  const handleDeleteConfirm = async (id: number) => {
    try {
      await deleteCategoria(id);
      setConfirmDeleteId(null);
      await fetchCategorias();
      setMessage('Deleted category');
    } catch (error) {
      console.error(error);
      if (error instanceof ValidationError) {
        setMessage(error.message);
      } else if (error instanceof Error) {
        setMessage(error.message);
      } else {
        setMessage('Unknown error while deleting category, please contact support');
      }
    }
  };

  return (
    <>
      <div className="admin-categories">
        <div className="admin-categories-title">Categories</div>
        {loading ? (
          <p className="admin-categories-loading">Loading...</p>
        ) : (
          <ul className="admin-categories-list">
            {categorias.map((cat) => (
              <li key={cat.id} className="admin-categories-list-item">
                {editId === cat.id ? (
                  <div className="admin-categories-edit">
                    <input
                      type="text"
                      value={editNombre}
                      onChange={(e) => setEditNombre(e.target.value)}
                      className="admin-categories-edit-input"
                    />
                    <button className="admin-categories-btn admin-categories-btn-save" onClick={handleUpdate}>Save</button>
                    <button className="admin-categories-btn admin-categories-btn-cancel" onClick={() => setEditId(null)}>Cancel</button>
                  </div>
                ) : (
                  <div className="admin-category-row">
                    <span className="admin-category-name">{cat.nombre}</span>
                    <div className="admin-categories-actions">
                      {confirmDeleteId === cat.id ? (
                        <>
                          <button
                            className="admin-categories-btn admin-categories-btn-delete"
                            onClick={() => handleDeleteConfirm(cat.id)}
                          >
                            Confirm Delete
                          </button>
                          <button
                            className="admin-categories-btn admin-categories-btn-cancel"
                            onClick={() => setConfirmDeleteId(null)}
                          >
                            Cancel
                          </button>
                        </>
                      ) : (
                        <>
                          <button
                            className="admin-categories-btn admin-categories-btn-edit"
                            onClick={() => handleEdit(cat.id, cat.nombre)}
                          >
                            <FaEdit /> Edit
                          </button>
                          <button
                            className="admin-categories-btn admin-categories-btn-delete"
                            onClick={() => handleDeleteClick(cat.id)}
                          >
                            <FaTrash /> Delete
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                )}
              </li>
            ))}

            {/* Botón para añadir nueva categoría */}
            <li>
              {showNewInput ? (
                <div className="admin-categories-edit">
                  <input
                    type="text"
                    placeholder="New category name"
                    value={newNombre}
                    onChange={(e) => setNewNombre(e.target.value)}
                    className="admin-categories-edit-input"
                  />
                  <button className="admin-categories-btn admin-categories-btn-save" onClick={handleCreate}>Save</button>
                  <button className="admin-categories-btn admin-categories-btn-cancel" onClick={() => {
                    setShowNewInput(false);
                    setNewNombre('');
                  }}>Cancel</button>
                </div>
              ) : (
                <button className="admin-categories-btn-add" onClick={() => setShowNewInput(true)}>
                  <FaPlus size={20}/>  Add new Category
                </button>
              )}
            </li>
          </ul>
        )}
      </div>
      {message && (
        <div
          className={`toast-message ${visibleMessage ? 'show' : 'hide'}`}
          onTransitionEnd={handleTransitionEnd}
        >
          {message}
          <button className="toast-close-btn" onClick={() => setVisibleMessage(false)}>×</button>
        </div>
      )}
  </>
  );
}