import React, { useEffect, useState } from "react";
import type { ProductoOutputDTO, ProductoInputDTO } from "../../types/producto.types";
import type { Imagen } from "../../types/imagen.types";
import type { Categoria } from "../../types/categoria.types";
import ImageUploader from "./ImageUploader";
import { newImagen, deleteImagen, getImagenById } from "../../api/imagenes";
import { getAllCategorias, newCategoria } from "../../api/categorias";
import "./ProductForm.css"
import type { ProductoInputDTOWithImages } from "./ProductoInputDTOWithImages";

interface ProductFormProps {
  mode: "create" | "edit";
  isEditingDraft?: boolean;
  initialData?: ProductoInputDTOWithImages | ProductoOutputDTO;
  onSubmit?: (data: ProductoInputDTO) => void;
  onAddToList?: (data: ProductoInputDTOWithImages) => void;
  onCancel?: () => void;
}

export default function ProductForm({ mode, isEditingDraft, initialData, onSubmit, onAddToList, onCancel }: ProductFormProps) {
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
      alert("El nombre de la categoría no puede estar vacío.");
      return;
    }
    setSavingCategory(true);
    try {
      const newCatId = await newCategoria({ nombre: newCategoryName.trim() });
      const cats = await getAllCategorias();
      setCategories(cats);
      setCategory(newCatId.toString());
      setShowCategoryModal(false);
    } catch (err) {
      console.error("Error creando categoría", err);
      alert("Error al crear la categoría");
    } finally {
      setSavingCategory(false);
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

      <div className="form-group">
        <label>Product name</label>
        <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        {errors.name && <p className="error-message">{errors.name}</p>}
      </div>

      <div className="form-group">
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

      <div className="form-group">
        <label>Product description</label>
        <textarea value={description} onChange={(e) => setDescription(e.target.value)} />
      </div>

      <div className="form-row">
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

      <div className="form-group">
        <label>Product price</label>
        <input type="number" value={price} onChange={(e) => setPrice(Number(e.target.value))} />
        {errors.price && <p className="error-message">{errors.price}</p>}
      </div>

      <div className="form-group">
        <label>Sold out</label>
        <input type="checkbox" checked={soldOut} onChange={(e) => setSoldOut(e.target.checked)} />
      </div>

      <ImageUploader
        existingImages={existingImages}
        onRemoveExisting={handleRemoveExisting}
        newImages={newImages}
        onAddNew={(files) => setNewImages((prev) => [...prev, ...files])}
        onRemoveNew={handleRemoveNew}
      />

      <div className="button-group">
        {mode === "create" && !isEditingDraft && (
          <button type="submit" disabled={!isFormValid}>
            Add Piece
          </button>
        )}
        {mode === "create" && isEditingDraft && (
          <>
            <button type="submit" disabled={!isFormValid}>
              Update
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

      {/* Modal para nueva categoría */}
      {showCategoryModal && (
        <div className="modal-backdrop" onClick={(e) => {
          if (e.target === e.currentTarget) setShowCategoryModal(false);
        }}>
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
              <button onClick={saveNewCategory} disabled={savingCategory}>
                {savingCategory ? "Saving..." : "Save"}
              </button>
              <button onClick={() => setShowCategoryModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </form>
  );
}
