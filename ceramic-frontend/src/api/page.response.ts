export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // Página actual (0-based)
  first: boolean;
  last: boolean;
}
