import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  imports: [ReactiveFormsModule, RouterModule]
})
export class SignupComponent {
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {
    this.signupForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  onSignup(): void {
    const { username, password, confirmPassword } = this.signupForm.value;
    if (password !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }
    console.log('Sign-up attempt:', username, password);
    // TODO: Integrate with AuthService
    this.router.navigate(['/login']);
  }
}
