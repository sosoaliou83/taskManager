import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Task } from '../../shared/models/task.model';
import { ApiResponse } from '../../shared/models/api-response.model';
import { TaskFormComponent } from '../task-form/task-form.component';
import { Page } from '../../shared/models/page.model';
import { HardDeleteConfirmationComponent } from '../hard-delete-confirmation/hard-delete-confirmation.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';

@Component({
  standalone: true,
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  imports: [ReactiveFormsModule, RouterModule, CommonModule, FormsModule, TaskFormComponent, HardDeleteConfirmationComponent]
})
export class TaskListComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  username = localStorage.getItem('username') || 'Guest';
  userRole = '';
  currentTab: 'current' | 'deleted' = 'current';
  isDeleted = false;

  tasks: Task[] = [];
  totalTasks = 0;

  showCreateForm = false;
  selectedTask: Partial<Task> | null = null;
  showHardDeleteConfirm = false;
  confirmedTask: Task | null = null;

  // Pagination
  currentPage = 0;
  itemsPerPage = 10;
  get pageStart() { return this.currentPage * this.itemsPerPage; }
  get pageEnd() { return Math.min((this.currentPage + 1) * this.itemsPerPage, this.totalTasks); }

  // Filters
  filterPriority: '' | 'LOW' | 'MEDIUM' | 'HIGH' = '';
  filterDueDate: string = '';

  ngOnInit() {
    this.loadCurrentUserRole();
    this.loadTasks();
  }

  loadCurrentUserRole() {
    this.http.get<ApiResponse<string>>(
      '/api/users/role',
      { params: new HttpParams().set('username', this.username) }
    ).subscribe({
      next: res => this.userRole = res.data,
      error: () => this.userRole = 'USER'
    });
  }

  switchTab(tab: 'current' | 'deleted') {
    this.currentTab = tab;
    this.isDeleted = (tab === 'deleted');
    this.currentPage = 0;
    this.loadTasks();
  }

  openCreateForm() {
    this.selectedTask = null;
    this.showCreateForm = true;
  }

  openUpdateForm(task: Task) {
    this.selectedTask = { ...task };
    this.showCreateForm = true;
  }

  closeCreateForm() {
    this.showCreateForm = false;
  }

  onTaskCreated(data: Partial<Task>) {
    this.showCreateForm = false;
    const payload = { ...data, createdBy: this.username };
    this.http.post<ApiResponse<Task>>('/api/tasks/create', payload)
      .subscribe(() => this.loadTasks());
  }

  onTaskUpdated(data: Partial<Task>) {
    this.showCreateForm = false;
    this.http.put<ApiResponse<Task>>(`/api/tasks/update/${data.id}`, data)
      .subscribe(() => this.loadTasks());
  }

  openDeleteConfirmation(task: Task) {
    this.confirmedTask = task;
    this.showHardDeleteConfirm = true;
  }

  closeDeleteConfirmation() {
    this.showHardDeleteConfirm = false;
    this.confirmedTask = null;
  }

  hardDeleteTask(task: Task) {
    this.showHardDeleteConfirm = false;
    this.http.delete(`/api/tasks/delete/${task.id}`)
      .subscribe(() => this.loadTasks());
  }

  toggleCompletion(task: Task) {
    this.http.patch(`/api/tasks/toggle-completion/${task.id}`, null)
      .subscribe((res: any) => {
        task.completed = res.data.completed;
      });
  }

  toggleDeletion(task: Task) {
    this.http.patch(`/api/tasks/toggle-delete/${task.id}`, null)
      .subscribe(() => this.loadTasks());
  }

  // ---- Filters ----
  applyFilters() {
    this.currentPage = 0;
    this.loadTasks();
  }

  clearFilters() {
    this.filterPriority = '';
    this.filterDueDate = '';
    this.applyFilters();
  }

  // ---- Loading Tasks ----
  loadTasks() {
    const params = new HttpParams({
      fromObject: {
        createdBy: this.username,
        deleted: this.isDeleted.toString(),
        page: this.currentPage.toString(),
        size: this.itemsPerPage.toString(),
        priority: this.filterPriority,
        dueDate: this.filterDueDate
      }
    });
    this.http.get<ApiResponse<Page<Task>>>('/api/tasks/getData', { params })
      .subscribe(res => {
        this.tasks = res.data.content;
        this.totalTasks = res.data.totalElements;
      });
  }

  // ---- Pagination & Sorting ----
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

  sortBy(column: string) {
    // implement if needed
  }

  // ---- User Menu & Logout ----
  dropdownOpen = false;
  toggleDropdown() { this.dropdownOpen = !this.dropdownOpen; }
  hideDropdown() { setTimeout(() => this.dropdownOpen = false, 150); }
  logout() {
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }

  get canHardDelete() {
    return this.userRole === 'VALIDATOR' || this.userRole === 'ADMIN';
  }
}
