// pages/admin/AdminProductEdit.tsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ProductForm from "../../components/ProductForm/ProductForm";
import { getProductoById, updateProducto } from "../../api/productos";
import type { ProductoOutputDTO, ProductoInputDTO } from "../../types/producto.types";

export default function AdminProductEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState<ProductoOutputDTO | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (id) {
        const data = await getProductoById(Number(id));
        setProducto(data);
      }
    };
    fetchData();
  }, [id]);

  const handleUpdate = async (data: ProductoInputDTO) => {
    if (id) {
      try {
        await updateProducto(Number(id), data);
        navigate("/pieces/" + id); // Redirigir al detalle del producto
      } catch (error) {
        console.error("Error updating product:", error);
      }
    }
  };

  if (!producto) return <p>Loading...</p>;

  return (
    <div>
      <ProductForm mode="edit" initialData={producto} onSubmit={handleUpdate} onCancel={() => navigate(-1)} />
    </div>
  );
}
