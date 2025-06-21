export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    number: number; // current page number (zero-based)
    size: number;
    first: boolean;
    last: boolean;
}