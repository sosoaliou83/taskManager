import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { TaskListComponent } from './tasks/task-list/task-list.component';
import { TaskFormComponent } from './tasks/task-form/task-form.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, // redirect root to login
    { path: 'login', component: LoginComponent },
    { path: 'task-list', component: TaskListComponent },
    { path: 'task-form', component: TaskFormComponent },
    { path: '**', redirectTo: 'login' }
];
