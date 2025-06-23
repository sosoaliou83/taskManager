import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  OnChanges,
  SimpleChanges
} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule, MatCalendar } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import type { Task } from '../../shared/models/task.model';
import type { PublicHoliday } from '../../shared/models/public-holiday.model';

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
  ],
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss']
})
export class TaskFormComponent implements OnInit, OnChanges {
  @Input() task: Partial<Task> | null = null;
  @Output() create = new EventEmitter<Partial<Task>>();
  @Output() update = new EventEmitter<Partial<Task>>();
  @Output() closed = new EventEmitter<void>();

  taskForm: FormGroup;
  holidays: Date[] = [];
  private loadedHolidayYears = new Set<number>();

  constructor(
    private fb: FormBuilder,
    private http: HttpClient
  ) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      priority: ['LOW', Validators.required],
      dueDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // pre-load near 10 years range public holiday
    const currentYear = new Date().getFullYear();
    const startYear = currentYear - 5;
    const endYear = currentYear + 5;
    for (let year = startYear; year <= endYear; year++) {
      this.loadHolidays(year);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['task']) {
      if (this.task) {
        this.taskForm.patchValue(this.task);
      } else {
        this.taskForm.reset({ title: '', description: '', priority: 'LOW', dueDate: '' });
      }
    }
  }

  private loadHolidays(year: number) {
    if (this.loadedHolidayYears.has(year)) return;
    this.loadedHolidayYears.add(year);

    const params = new HttpParams().set('year', year.toString());
    this.http
      .get<{ statusCode: number; message: string; data: PublicHoliday[] }>(
        '/api/holidays',
        { params }
      )
      .subscribe(resp => {
        const dates = resp.data.map(h => {
          const [y, m, d] = h.date.split('-').map(Number);
          return new Date(y, m - 1, d);
        });
        // merge into our array
        this.holidays = [...this.holidays, ...dates];
      },
        err => console.error('Failed to load holidays', err));
  }

  dateFilter = (date: Date | null): boolean => {
    if (!date) return false;
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return !this.holidays.some(h => h.getTime() === d.getTime());
  };

  dateClass = (date: Date): string => {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return this.holidays.some(h => h.getTime() === d.getTime()) ? 'holiday' : '';
  };

  onYearSelected(selected: Date): void {
    this.loadHolidays(selected.getFullYear());
  }

  onSubmit(): void {
    if (this.taskForm.invalid) return;
    const fv = this.taskForm.value;
    const d = new Date(fv.dueDate);
    d.setHours(0, 0, 0, 0);
    const yyyy = d.getFullYear();
    const MM = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    const formattedDate = `${yyyy}-${MM}-${dd}`;

    const payload: Partial<Task> = { ...fv, dueDate: formattedDate };
    if (this.task?.id) {
      this.update.emit({ ...payload, id: this.task.id });
    } else {
      this.create.emit(payload);
    }
  }

  close(): void {
    this.closed.emit();
  }
}
