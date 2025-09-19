import { motion } from "framer-motion";
import FindMeCard from "../FindMeCard/FindMeCard";
import "./FindMeGrid.css"
import type { FindMePostOutputDTO } from "../../types/findmepost.types";

interface FindMeGridProps {
  posts: FindMePostOutputDTO[];
  isLoading?: boolean;
}

const containerVariants = {
  hidden: { opacity: 0 },
  visible: { opacity: 1, transition: { staggerChildren: 0.1 } },
};

export default function FindMeGrid({ posts, isLoading }: FindMeGridProps) {
  return (
    <motion.div
      className="findme-grid"
      variants={containerVariants}
      initial="hidden"
      animate="visible"
    >
      {posts.map((post) => (
        <FindMeCard
          key={post.id}
          post={post}
        />
      ))}

      {isLoading && (
        <>
          <div className="skeleton-card"></div>
        </>
      )}
    </motion.div>
  );
}
