import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [ReactiveFormsModule, RouterModule, CommonModule]
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  errorMessage: string = '';

  onLogin(): void {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

      this.http.post('/api/auth/login', { username, password }, { headers }).subscribe({
        next: (res: any) => {
          console.log('✅ Login successful:', res);
          localStorage.setItem('username', username);
          this.errorMessage = ''; // ✅ Clear error if successful
          this.router.navigate(['/task-list']); // ✅ Redirect to task list page
        },
        error: (err) => {
          console.error('❌ Login failed:', err);
          this.errorMessage = 'Invalid username or password'; // ✅ Show error
        }
      });
    }
  }
}
