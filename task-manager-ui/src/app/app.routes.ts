import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import { TaskListComponent } from './tasks/task-list/task-list.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, // redirect root to login
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'task-list', component: TaskListComponent },
    { path: '**', redirectTo: 'login' }
];
