// pages/admin/AdminFindMeEdit.tsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import FindMeForm from "../../components/FindMeForm/FindMeForm";
import { getFindMePostById, updateFindMePost } from "../../api/findmeposts";
import type { FindMePostInputDTO, FindMePostOutputDTO } from "../../types/findmepost.types";

export default function AdminFindMeEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState<FindMePostOutputDTO | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (id) {
        try {
          const data = await getFindMePostById(Number(id));
          setPost(data);
        } catch (error) {
          console.error("Error fetching FindMe post:", error);
        }
      }
    };
    fetchData();
  }, [id]);

  const handleUpdate = async (data: FindMePostInputDTO) => {
    if (id) {
      try {
        await updateFindMePost(Number(id), data);
        navigate(-1); // Redirigir al detalle del post
      } catch (error) {
        console.error("Error updating FindMe post:", error);
      }
    }
  };

  if (!post) return <p>Loading...</p>;

  return (
    <div>
      <FindMeForm
        mode="edit"
        initialData={post}
        onSave={handleUpdate}
      />
    </div>
  );
}
