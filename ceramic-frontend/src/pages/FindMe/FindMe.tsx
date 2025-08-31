import { useEffect, useState } from "react";
import { getAllFindMePosts } from "../../api/findmeposts";
import type { FindMePostOutputDTO } from "../../types/findmepost.types";
import FindMeGrid from "../../components/FindMeGrid/FindMeGrid";

export default function FindMe() {
  const [posts, setPosts] = useState<FindMePostOutputDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const data = await getAllFindMePosts();
        setPosts(data);
      } catch (error) {
        console.error("Error al obtener los posts de Find Me:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, []);

  return (
    <div className="findme-page">
      <h2>Upcoming events</h2>
      <FindMeGrid posts={posts} isLoading={loading} />
    </div>
  );
}
