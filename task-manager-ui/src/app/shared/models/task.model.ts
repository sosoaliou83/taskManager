export interface Task {
    id: string;
    title: string;
    description: string;
    priority: 'High' | 'Medium' | 'Low';
    dueDate: string;
    completed: boolean;
    deleted: boolean;
}