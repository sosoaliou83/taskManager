import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hard-delete-confirmation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hard-delete-confirmation.component.html',
  styleUrls: ['./hard-delete-confirmation.component.scss']
})
export class HardDeleteConfirmationComponent {
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  confirm() {
    this.confirmed.emit();
  }

  cancel() {
    this.cancelled.emit();
  }
}
