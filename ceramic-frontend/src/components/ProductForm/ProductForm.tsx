import React, { useEffect, useState } from "react";
import type { ProductoOutputDTO, ProductoInputDTO } from "../../types/producto.types";
import type { Imagen } from "../../types/imagen.types";
import type { Categoria } from "../../types/categoria.types";
import ImageUploader from "./ImageUploader";
import { newImagen, deleteImagen, getImagenById } from "../../api/imagenes";
import { getAllCategorias, newCategoria } from "../../api/categorias";
import "./ProductForm.css"
import type { ProductoInputDTOWithImages } from "./ProductoInputDTOWithImages";
import { FaCheck, FaEdit, FaPlus, FaRegEnvelope, FaSave, FaTrash } from "react-icons/fa";

interface ProductFormProps {
  mode: "create" | "edit";
  isEditingDraft?: boolean;
  initialData?: ProductoInputDTOWithImages | ProductoOutputDTO;
  onSubmit?: (data: ProductoInputDTO) => void;
  onAddToList?: (data: ProductoInputDTOWithImages) => void;
  onCancel?: () => void;
  drafts?: ProductoInputDTOWithImages[];
  editingIndex?: number | null;
  onEditDraft?: (index: number) => void;
  onDeleteDraft?: (index: number) => void;
  onSaveAll?: () => void;
  onSaveAndNotify?: () => void;
  onGlobalCancel?: () => void;
  disableGlobalActions?: boolean;
}

export default function ProductForm({ mode, isEditingDraft, initialData, onSubmit, onAddToList, onCancel, drafts, editingIndex, onEditDraft, onDeleteDraft, onSaveAll, onSaveAndNotify, onGlobalCancel, disableGlobalActions }: ProductFormProps) {
  const [name, setName] = useState(initialData?.nombre || "");
  const [category, setCategory] = useState(initialData?.idCategoria?.toString() || "");
  const [description, setDescription] = useState(initialData?.descripcion || "");
  const [width, setWidth] = useState(initialData?.anchura || 0);
  const [height, setHeight] = useState(initialData?.altura || 0);
  const [diameter, setDiameter] = useState(initialData?.diametro || 0);
  const [price, setPrice] = useState(initialData?.precio || 0);
  const [soldOut, setSoldOut] = useState(initialData?.soldOut || false);

  const [categories, setCategories] = useState<Categoria[]>([]);
  const [existingImages, setExistingImages] = useState<Imagen[]>([]);
  const [newImages, setNewImages] = useState<File[]>([]);
  const [deletedImages, setDeletedImages] = useState<number[]>([]);

  const [showCategoryModal, setShowCategoryModal] = useState(false);
  const [newCategoryName, setNewCategoryName] = useState("");
  const [savingCategory, setSavingCategory] = useState(false);

  const [errors, setErrors] = useState<{ name?: string; category?: string; price?: string }>({});
  const [isFormValid, setIsFormValid] = useState(false);

  const [message, setMessage] = useState<string | null>(null);
  const [visibleMessage, setVisibleMessage] = useState(false);
  
  useEffect(() => {
    if (message) {
      setVisibleMessage(true);
      const timer = setTimeout(() => setVisibleMessage(false), 3000);
      return () => clearTimeout(timer);
    }
  }, [message]);

  const handleTransitionEnd = () => {
    if (!visibleMessage) setMessage(null);
  };

  // Obtener las categorías para el select
  useEffect(() => {
    const fetchCategorias = async () => {
      try {
        const cats = await getAllCategorias();
        setCategories(cats);
      } catch (error) {
        console.error("Error loading categories:", error);
      }
    };
    fetchCategorias();
  }, []);

  // Añadir nueva categoría
  const handleAddNewCategory = () => {
    setNewCategoryName("");
    setShowCategoryModal(true);
  };

  // Guardar la categoría añadida
  const saveNewCategory = async () => {
    if (!newCategoryName.trim()) {
      setMessage("Category name cannot be empty.");
    } else {
      setSavingCategory(true);
      try {
        const newCatId = await newCategoria({ nombre: newCategoryName.trim() });
        const cats = await getAllCategorias();
        setCategories(cats);
        setCategory(newCatId.toString());
        setShowCategoryModal(false);
      } catch (err) {
        console.error("Error creando categoría", err);
        setMessage("Error creating category. Please try again.");
      } finally {
        setSavingCategory(false);
      }
    }
  };

  // Cargar imágenes existentes y nuevas desde initialData
  useEffect(() => {
    const fetchImages = async () => {
      if (initialData && "idsImagenes" in initialData && initialData?.idsImagenes?.length) {
        const images = await Promise.all(
          initialData.idsImagenes.map((id) => getImagenById(id))
        );
        setExistingImages(images);
      }

      if (initialData && "localImages" in initialData && initialData.localImages?.length) {
        setNewImages(initialData.localImages);
      }
    };
    fetchImages();
  }, [initialData]);

  // Cargar datos iniciales en el formulario
  useEffect(() => {
    if (initialData) {
      setName(initialData.nombre || "");
      setCategory(initialData.idCategoria?.toString() || "");
      setDescription(initialData.descripcion || "");
      setWidth(initialData.anchura || 0);
      setHeight(initialData.altura || 0);
      setDiameter(initialData.diametro || 0);
      setPrice(initialData.precio || 0);
      setSoldOut(initialData.soldOut || false);

      if ("localImages" in initialData) {
        setNewImages(initialData.localImages);
      }

      setErrors({});
      setDeletedImages([]);
    }
  }, [initialData]);

  const getValidationErrors = () => {
    const newErrors: typeof errors = {};
    if (!name.trim()) newErrors.name = "Product name is required.";
    if (!category) newErrors.category = "Please select a category.";
    if (!price || price <= 0) newErrors.price = "Price must be greater than 0.";
    return newErrors;
  };

  const isValidForm = () => {
    const errs = getValidationErrors();
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };


  useEffect(() => {
    const errs = getValidationErrors();
    setErrors(errs);
    setIsFormValid(Object.keys(errs).length === 0);
  }, [name, category, price]);

  const handleRemoveExisting = (index: number) => {
    const removed = existingImages[index];
    if (removed) {
      setDeletedImages((prev) => [...prev, removed.id]);
      setExistingImages((prev) => prev.filter((_, i) => i !== index));
    }
  };

  const handleRemoveNew = (index: number) => {
    setNewImages((prev) => prev.filter((_, i) => i !== index));
  };

  const resetForm = () => {
    setName("");
    setCategory("");
    setDescription("");
    setWidth(0);
    setHeight(0);
    setDiameter(0);
    setPrice(0);
    setSoldOut(false);
    setExistingImages([]);
    setNewImages([]);
    setDeletedImages([]);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!isValidForm()) return;

    if (mode === "create" && onAddToList) {
      const data: ProductoInputDTOWithImages = {
        nombre: name,
        idCategoria: Number(category),
        descripcion: description,
        anchura: width,
        altura: height,
        diametro: diameter,
        precio: price,
        soldOut: soldOut,
        idsImagenes: [],
        localImages: [...newImages],
      };

      onAddToList(data);
      clearImages();
      return;
    }

    if (mode === "edit" && onSubmit) {
      const uploadImages: Imagen[] = [];
      for (const file of newImages) {
        const imagen = await newImagen(file);
        uploadImages.push(imagen);
      }

      for (const id of deletedImages) {
        await deleteImagen(id);
      }

      const finalImages = [...existingImages, ...uploadImages].map((img) => img.id);

      const data: ProductoInputDTO = {
        nombre: name,
        idCategoria: Number(category),
        descripcion: description,
        anchura: width,
        altura: height,
        diametro: diameter,
        precio: price,
        soldOut: soldOut,
        idsImagenes: finalImages,
      };
      onSubmit(data);
    }
  };

  const clearImages = () => {
    setNewImages([]);
    setExistingImages([]);
    setDeletedImages([]);
  };

  return (
    <form onSubmit={handleSubmit} className="product-form">
      <h2>{mode === "create" ? "Add a new Piece" : "Edit Product"}</h2>

      <div className="product-form-layout">
        <div className="product-form-left">
          <div className="product-form-row">
            <div className="product-form-group">
              <label>Product name</label>
              <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
              {errors.name && <p className="error-message">{errors.name}</p>}
            </div>

            <div className="product-form-group">
              <label>Category</label>
              <select value={category} onChange={
                (e) => {
                  if (e.target.value === "__add_new__") {
                    handleAddNewCategory();
                  } else {
                    setCategory(e.target.value);
                  }
                }
              }>
                <option value="" disabled>
                  -- Choose category --
                </option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.nombre}
                  </option>
                ))}
                <option value="__add_new__">Add new category...</option>
              </select>
              {errors.category && <p className="error-message">{errors.category}</p>}
            </div>
          </div>

          <div className="product-form-group">
            <label>Product description</label>
            <textarea value={description} onChange={(e) => setDescription(e.target.value)} rows={8} />
          </div>

          <div className="product-form-group">
            <label>Dimensions (cm)</label>
            <p className="info-text">(Enter 0 for any dimension to hide it from the product display)</p>
            <div className="product-form-row">
              <div>
                <label>Width</label>
                <input type="number" value={width} onChange={(e) => setWidth(Number(e.target.value))} />
              </div>
              <div>
                <label>Height</label>
                <input type="number" value={height} onChange={(e) => setHeight(Number(e.target.value))} />
              </div>
              <div>
                <label>Diameter</label>
                <input type="number" value={diameter} onChange={(e) => setDiameter(Number(e.target.value))} />
              </div>
            </div>
          </div>

          <div className="product-form-group">
            <div className="product-form-price-soldout">
              <div className="product-form-group">
                <div>
                  <label>Product price</label>
                  <input type="number" value={price} onChange={(e) => setPrice(Number(e.target.value))} />
                </div>
                {errors.price && <p className="error-message">{errors.price}</p>}
              </div>
              <div className="product-form-group">
                <label>Sold out</label>
                <div className="checkbox-inline">
                  <input type="checkbox" checked={soldOut} onChange={(e) => setSoldOut(e.target.checked)} />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="product-form-right">
          <ImageUploader
            existingImages={existingImages}
            onRemoveExisting={handleRemoveExisting}
            newImages={newImages}
            onAddNew={(files) => setNewImages((prev) => [...prev, ...files])}
            onRemoveNew={handleRemoveNew}
          />

          {mode === "create" && drafts && drafts.length > 0 && (
            <div className="added-pieces">
              <h3>Added pieces</h3>
              <div className="added-pieces-table">
                <div className="added-pieces-table-header">
                  <span className="col-actions"></span>
                  <span className="col-product">Product</span>
                  <span className="col-price">Category</span>
                  <span className="col-price">Price</span>
                </div>

                <ul className="added-pieces-body">
                  {drafts.map((draft, index) => {
                    const isEditing = editingIndex === index;
                    return (
                      <li key={index} className={`pieces-row ${isEditing ? "editing" : ""}`}>
                        <div className="col-actions">
                          <button
                            type="button"
                            onClick={() => onEditDraft?.(index)}
                            disabled={isEditing}
                            title="Edit"
                          >
                            <FaEdit size={16} />
                          </button>
                          <button
                            type="button"
                            onClick={() => onDeleteDraft?.(index)}
                            disabled={isEditing}
                            title="Delete"
                          >
                            <FaTrash size={16} />
                          </button>
                        </div>

                        <div className="col-product">
                          <div className="added-product-info">
                            {draft.localImages?.[0] ? (
                              <img
                                src={URL.createObjectURL(draft.localImages[0])}
                                alt={draft.nombre}
                                className="preview-image"
                              />
                            ) : (
                              <img
                                src="/images/1068302.png"
                                alt="No preview"
                                className="preview-image"
                              />
                            )}
                            <span>{draft.nombre}</span>
                          </div>
                        </div>

                        <div className="col-category">{categories.find(cat => cat.id === draft.idCategoria)?.nombre}</div>
                        <div className="col-price">{draft.precio?.toFixed(2)} €</div>
                      </li>
                    );
                  })}
                </ul>
              </div>
            </div>
          )}
          
        </div>
      </div>

      <div className={`product-form-button-group ${mode === 'create' ? 'create-mode' : ''}`}>
        <div className= "form-actions">
          {mode === "create" && !isEditingDraft && (
            <button type="submit" disabled={!isFormValid}>
              <FaPlus size={16}/> Add Piece
            </button>
          )}
          {mode === "create" && isEditingDraft && (
            <>
              <button type="submit" disabled={!isFormValid}>
                <FaSave size={16} /> Update
              </button>
              <button type="button" onClick={() => {clearImages(); if (onCancel) onCancel()}}>Cancel</button>
            </>
          )}
          {mode === "edit" && (
            <>
              <button type="submit" disabled={!isFormValid}>
                Save Product
              </button>
              {onCancel && (
                <button type="button" onClick={onCancel}>Cancel</button>
              )}
            </>
          )}
          <button type="button" onClick={resetForm}>
            Clear Form
          </button>
        </div>

        {mode === "create" && (
          <div className="global-actions">
            {onSaveAll && (
              <button type="button" onClick={() => setTimeout(() => { onSaveAll() }, 0)} disabled={disableGlobalActions}>
                <FaCheck size={16} /> Save All Pieces
              </button>
            )}
            {onSaveAndNotify && (
              <button type="button" onClick={onSaveAndNotify} disabled={disableGlobalActions}>
                <FaRegEnvelope size={16} /> Save & Notify Subscribers
              </button>
            )}
            {onGlobalCancel && (
              <button type="button" onClick={onGlobalCancel}>
                  Cancel All & Return
                </button>
              )}
            </div>
          )}
      </div>

      {/* Modal para nueva categoría */}
      {showCategoryModal && (
        <div className="modal-backdrop">
          <div className="modal">
            <h2>New Category</h2>
            <label>
              Category Name:
              <input
                type="text"
                value={newCategoryName}
                onChange={(e) => setNewCategoryName(e.target.value)}
              />
            </label>
            <div className="modal-actions">
              <button type="button" onClick={saveNewCategory} disabled={savingCategory}>
                {savingCategory ? "Saving..." : "Save"}
              </button>
              <button onClick={() => setShowCategoryModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {/* Toast */}
      {message && (
        <div
          className={`toast-message ${visibleMessage ? "show" : "hide"}`}
          onTransitionEnd={handleTransitionEnd}
        >
          {message}
          <button
            className="toast-close-btn"
            onClick={() => setVisibleMessage(false)}
          >
            ×
          </button>
        </div>
      )}
    </form>
  );
}
