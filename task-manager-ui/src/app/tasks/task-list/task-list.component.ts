import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Task } from '../../shared/models/task.model'; // adjust path if needed
import { ApiResponse } from '../../shared/models/api-response.model';


@Component({
  standalone: true,
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  imports: [ReactiveFormsModule, RouterModule, CommonModule, FormsModule]
})
export class TaskListComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  username = localStorage.getItem('username') || 'Guest';
  currentTab: 'current' | 'deleted' = 'current';
  tasks: Task[] = [];
  totalTasks = 0;
  pageStart = 0;
  itemsPerPage = 10;

  get pageEnd() {
    return Math.min(this.pageStart + this.itemsPerPage, this.totalTasks);
  }

  switchTab(tab: 'current' | 'deleted') {
    this.currentTab = tab;
    this.loadTasks();
  }

  createTask() {

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

  // TODO need to test
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

  sortBy(column: string) {
    // TODO: implement sorting logic
    console.log('Sort by', column);
  }

  updateTask(task: any) {
    // TODO: need to create edit form first
  }

  //TODO : fix that function 
  previousPage() {
    this.pageStart = Math.max(0, this.pageStart - this.itemsPerPage);
    this.loadTasks();
  }

  //TODO : fix that function 
  nextPage() {
    if (this.pageEnd < this.totalTasks) {
      this.pageStart += this.itemsPerPage;
      this.loadTasks();
    }
  }

  onItemsPerPageChange() {
    this.pageStart = 0;
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