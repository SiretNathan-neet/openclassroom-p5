import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect, jest } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    id: 1,
    name: 'Yoga session',
    description: 'A relaxing yoga session',
    date: new Date('2026-01-01'),
    teacher_id: 1,
    users: []
  }

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSessionService)),
    create: jest.fn().mockReturnValue(of(mockSessionService)),
    update: jest.fn().mockReturnValue(of(mockSessionService)),
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([]))
  }

  const mockMatSnackBar = {
    open: jest.fn()
  }

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  }

  // Router mockable changeable en .url selon le test
  const mockRouter = {
    navigate: jest.fn(),
    url: '/sessions/create'
  }

  const configureTestBed = (sessionServiceMock: any) => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock},
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  };

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('when user is admin', () => {
    const mockSessionService = { sessionInformation: { admin: true } };

    it('should create in "create" mode and not redirect', () => {
      mockRouter.url = '/sessions/create';
      configureTestBed(mockSessionService);
      fixture.detectChanges();

      expect(component).toBeTruthy();
      expect(component.onUpdate).toBe(false);
      expect(mockRouter.navigate).not.toHaveBeenCalledWith(['/sessions']);
      expect(component.sessionForm?.value.name).toBe('');
    });

    it('should load session in "update" mode', () => {
      mockRouter.url = '/sessions/update/1';
      configureTestBed(mockSessionService);
      fixture.detectChanges();

      expect(component.onUpdate).toBe(true);
      expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
      expect(component.sessionForm?.value.name).toBe('Yoga session');
    });

    it('should call create() and exit page on submit (create mode)', () => {
      mockRouter.url = '/sessions/create';
      configureTestBed(mockSessionService);
      fixture.detectChanges();

      component.sessionForm?.setValue({
        name: 'New session',
        date: '2026-02-01',
        teacher_id: 1,
        description: 'desc'
      });
      component.submit();

      expect(mockSessionApiService.create).toHaveBeenCalled();
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should call update() and exit page on submit (update mode)', () => {
      mockRouter.url = '/sessions/update/1';
      configureTestBed(mockSessionService);
      fixture.detectChanges();

      component.submit();

      expect(mockSessionApiService.update).toHaveBeenCalledWith('1', expect.any(Object));
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('when user is not admin', () => {
    it('should redirect to /sessions', () => {
      const mockSessionServiceNotAdmin = { sessionInformation: { admin: false } };
      mockRouter.url = '/sessions/create';
      configureTestBed(mockSessionServiceNotAdmin);
      fixture.detectChanges();

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });
  });
});
