import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Task } from '../../shared/models/task.model'; // adjust path if needed
import { ApiResponse } from '../../shared/models/api-response.model';
import { TaskFormComponent } from '../task-form/task-form.component';


@Component({
  standalone: true,
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  imports: [ReactiveFormsModule, RouterModule, CommonModule, FormsModule, TaskFormComponent]
})
export class TaskListComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  username = localStorage.getItem('username') || 'Guest';
  currentTab: 'current' | 'deleted' = 'current';
  tasks: Task[] = [];
  totalTasks = 0;
  showCreateForm = false;
  selectedTask: Partial<Task> | null = null;

  // === pagination state ===
  currentPage = 0;
  itemsPerPage = 10;

  get pageStart(): number {
    return this.currentPage * this.itemsPerPage;
  }
  get pageEnd(): number {
    return Math.min((this.currentPage + 1) * this.itemsPerPage, this.totalTasks);
  }

  ngOnInit() {
    this.loadTasks();
  }

  switchTab(tab: 'current' | 'deleted') {
    this.currentTab = tab;
    this.loadTasks();
  }

  openCreateForm() {
    this.selectedTask = null;
    this.showCreateForm = true;
  }

  openUpdateForm(task?: Task) {
    this.selectedTask = task ? { ...task } : null;
    this.showCreateForm = true;
  }

  closeCreateForm() {
    this.showCreateForm = false;
  }

  onTaskCreated(taskData: Partial<Task>) {
    this.showCreateForm = false;
    const payload: Partial<Task> = { ...taskData, createdBy: this.username };
    this.http
      .post<ApiResponse<Task>>('/api/tasks/create', payload)
      .subscribe({
        next: () => this.loadTasks(),
        error: (err) => console.error('❌ Failed to create task', err)
      });
  }

  onTaskUpdated(updatedTask: Partial<Task>) {
    this.showCreateForm = false;
    this.http
      .put<ApiResponse<Task>>(
        `/api/tasks/update/${updatedTask.id}`,
        updatedTask
      )
      .subscribe({
        next: () => this.loadTasks(),
        error: err => console.error('❌ Failed to update task', err)
      });
  }

  loadTasks() {
    const params = new HttpParams()
      .set('createdBy', this.username)
      .set('page', (this.pageStart / this.itemsPerPage).toString())
      .set('size', this.itemsPerPage.toString());

    this.http.get<ApiResponse<any>>(`/api/tasks/getData`, { params }).subscribe({
      next: (res) => {
        this.tasks = res.data.content;
        this.totalTasks = res.data.totalElements;
      },
      error: (err) => {
        console.error('❌ Failed to load tasks', err);
      }
    });
  }

  toggleCompletion(task: Task) {
    this.http.patch(`/api/tasks/toggle-completion/${task.id}`, null).subscribe({
      next: (res: any) => {
        console.log('✅ Completion toggled', res.data);
        task.completed = res.data.completed;
      },
      error: (err) => {
        console.error('❌ Error toggling completion', err);
      }
    });
  }

  deleteTask(task: Task) {
    this.http.patch(`/toggle-delete/${task.id}`, null).subscribe({
      next: () => {
        console.log('🗑️ Task soft-deleted');
        this.loadTasks(); // refresh
      },
      error: (err) => {
        console.error('❌ Error deleting task', err);
      }
    });
  }

  onFormSaved(taskData: Partial<Task>) {
    this.showCreateForm = false;
    const payload: Partial<Task> = { ...taskData, createdBy: this.username };
    const request$ = taskData.id
      ? this.http.put<ApiResponse<Task>>(`/api/tasks/update/${taskData.id}`, payload)
      : this.http.post<ApiResponse<Task>>('/api/tasks/create', payload);
    request$.subscribe({
      next: () => this.loadTasks(),
      error: (err) => console.error('❌ Failed to save task', err)
    });
  }

  sortBy(column: string) {
    // TODO: implement sorting logic
    console.log('Sort by', column);
  }


  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadTasks();
    }
  }

  nextPage() {
    if ((this.currentPage + 1) * this.itemsPerPage < this.totalTasks) {
      this.currentPage++;
      this.loadTasks();
    }
  }

  onItemsPerPageChange() {
    this.currentPage = 0;
    this.loadTasks();
  }

  dropdownOpen = false;

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  hideDropdown() {
    setTimeout(() => this.dropdownOpen = false, 150); // delay to allow click
  }

  logout() {
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }
}