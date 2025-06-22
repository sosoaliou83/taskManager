export interface Task {
    id: string;
    title: string;
    description: string;
    priority: 'HIGH' | 'MEDIUM' | 'LOW';
    dueDate: string;
    completed: boolean;
    deleted: boolean;
    createdBy: string;
}