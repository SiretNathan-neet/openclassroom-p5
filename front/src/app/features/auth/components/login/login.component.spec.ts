import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect, jest } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockSessionInformation = {
    token: 'fake-token',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true
  };

  const mockAuthService = {
    login: jest.fn()
  };

  const mockSessionService = {
    logIn: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService},
        { provide: SessionService, useValue: mockSessionService},
        { provide: Router, useValue: mockRouter}
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('form validation (integration test — real ReactiveForms)', () => {
    it('should be invalid when empty', () => {
      expect(component.form.valid).toBeFalsy();
    });

    it('should be invalid with a malformed email', () => {
      component.form.setValue({ email: 'not-an-email', password: 'azerty' });
      expect(component.form.valid).toBeFalsy();
    });

    it('should be valid with a correct email and password', () => {
      component.form.setValue({ email: 'yoga@studio.com', password: 'test!1234' });
      expect(component.form.valid).toBeTruthy();
    });
  });

  describe('submit()', () => {
    it('should log in, store session and navigate on success', () => {
      mockAuthService.login.mockReturnValue(of(mockSessionInformation));
      component.form.setValue({ email: 'yoga@studio.com', password: 'test!1234' });

      component.submit();

      expect(mockAuthService.login).toHaveBeenCalledWith({
        email: 'yoga@studio.com',
        password: 'test!1234'
      });
      expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBe(false);
    });

    it('should set onError to true when login fails', () => {
      mockAuthService.login.mockReturnValue(throwError(() => new Error('Invalid credentials')));
      component.form.setValue({ email: 'yoga@studio.com', password: 'wrongpass' });

      component.submit();

      expect(component.onError).toBe(true);
      expect(mockSessionService.logIn).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });
  });

  describe('template', () => {
    it('should display the error message when onError is true', () => {
      component.onError = true;
      fixture.detectChanges();
      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('.error')?.textContent).toContain('An error occurred');
    });

    it('should not display the error message when onError is false', () => {
      component.onError = false;
      fixture.detectChanges();
      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('.error')).toBeFalsy();
    });
  });
});
