// src/app/task-form/task-form.component.ts
import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import type { Task } from '../../shared/models/task.model';
import { MatDatepickerModule, MatDatepickerInputEvent } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ], templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss']
})
export class TaskFormComponent implements OnChanges {
  /** If non-null, we’re editing an existing task */
  @Input() task: Partial<Task> | null = null;

  /** Emits when creating a new task */
  @Output() create = new EventEmitter<Partial<Task>>();
  /** Emits when updating an existing task */
  @Output() update = new EventEmitter<Partial<Task>>();
  /** Emits when the user closes the form */
  @Output() closed = new EventEmitter<void>();

  taskForm: FormGroup;

  // TODO Call API to have SG public holiday
  // Mocks
  holidays = [
    new Date(2025, 0, 1),  // Jan 1, 2025
    new Date(2025, 6, 14), // Jul 14, 2025
  ];

  /** Disable selection of holidays */
  dateFilter = (date: Date | null): boolean => {
    if (!date) return false;
    return !this.holidays.some(h => h.getTime() === date.setHours(0, 0, 0, 0));
  }

  /** Add custom class to holiday dates */
  dateClass = (date: Date): string => {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return this.holidays.some(h => h.getTime() === d.getTime()) ? 'holiday' : '';
  }

  constructor(private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      priority: ['Low', Validators.required],
      dueDate: ['', Validators.required]
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['task']) {
      if (this.task) {
        // Prefill when editing an existing task
        this.taskForm.patchValue({
          title: this.task.title,
          description: this.task.description,
          priority: this.task.priority,
          dueDate: this.task.dueDate
        });
      } else {
        // Reset form for creation
        this.taskForm.reset({
          title: '',
          description: '',
          priority: 'LOW',
          dueDate: ''
        });
      }
    }
  }

  onSubmit() {
    if (this.taskForm.invalid) return;

    const fv = this.taskForm.value;
    const due = new Date(fv.dueDate);
    const yyyy = due.getFullYear();
    const MM = String(due.getMonth() + 1).padStart(2, '0');
    const dd = String(due.getDate()).padStart(2, '0');
    const formattedDate = `${yyyy}-${MM}-${dd}`;

    const payload: Partial<Task> = {
      ...fv,
      dueDate: formattedDate  // only date now
    };

    if (this.task && this.task.id) {
      this.update.emit({ ...payload, id: this.task.id });
    } else {
      this.create.emit(payload);
    }
  }

  close() {
    this.closed.emit();
  }
}
