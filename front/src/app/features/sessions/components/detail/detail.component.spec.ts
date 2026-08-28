import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { expect, jest } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSession = {
    id: 1,
    name: 'Yoga session',
    description: 'A relaxing session',
    date: new Date('2026-01-01'),
    teacher_id: 1,
    users: [1, 2, 3],
    createdAt: new Date('2026-01-01'),
    updatedAt: new Date('2026-01-01')
  };

  const mockTeacher = {
    id: 1,
    lastName: 'DELAHAYE',
    firstName: 'Margot'
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of(undefined)),
    unParticipate: jest.fn().mockReturnValue(of(undefined))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockMatSnackBar = { open: jest.fn() };
  const mockRouter = { navigate: jest.fn() };
  const mockActivatedRoute = {
    snapshot: { paramMap: { get: jest.fn().mockReturnValue('1') } }
  };

  const configureTestBed = (sessionServiceMock: any) => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule, 
        MatSnackBarModule, 
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        BrowserAnimationsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  };

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('when user participates (is in session.users)', () => {
    const mockSessionService = {
      sessionInformation: { admin: true, id: 1 }
    };

    beforeEach(() => {
      configureTestBed(mockSessionService);
      fixture.detectChanges();
    });

    it('should create and load session + teacher', () => {
      expect(component).toBeTruthy();
      expect(component.session).toEqual(mockSession);
      expect(component.teacher).toEqual(mockTeacher);
    });

    it('should set isParticipate to true', () => {
      expect(component.isParticipate).toBe(true);
    });

    it('should call window.history.back() on back()', () => {
      const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
      component.back();
      expect(backSpy).toHaveBeenCalled();
      backSpy.mockRestore();
    });

    it('should delete session, show snackbar and navigate', () => {
      component.delete();
      expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should call unParticipate and refresh session', () => {
      component.unParticipate();
      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
      // fetchSession rappelé -> detail rappelé une 2e fois (1 au ngOnInit + 1 ici)
      expect(mockSessionApiService.detail).toHaveBeenCalledTimes(2);
    });
  });

  describe('when user does not participate', () => {
    const mockSessionServiceNotParticipating = {
      sessionInformation: { admin: false, id: 99 }
    };

    beforeEach(() => {
      configureTestBed(mockSessionServiceNotParticipating);
      fixture.detectChanges();
    });

    it('should set isParticipate to false', () => {
      expect(component.isParticipate).toBe(false);
    });

    it('should call participate and refresh session', () => {
      component.participate();
      expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '99');
      expect(mockSessionApiService.detail).toHaveBeenCalledTimes(2);
    });
  });
});