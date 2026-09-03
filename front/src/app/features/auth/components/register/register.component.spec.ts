import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { expect, jest } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockAuthService = {
    register: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
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
      component.form.setValue({
        email: 'not-an-email',
        firstName: 'John',
        lastName: 'Doe',
        password: 'azerty'
      });
      expect(component.form.valid).toBeFalsy();
    });

    it('should be valid with correct values', () => {
      component.form.setValue({
        email: 'john.doe@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'azerty123'
      });
      expect(component.form.valid).toBeTruthy();
    });
  });

  describe('submit()', () => {
    it('should register and navigate to /login on success', () => {
      mockAuthService.register.mockReturnValue(of(undefined));
      component.form.setValue({
        email: 'john.doe@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'azerty123'
      });

      component.submit();

      expect(mockAuthService.register).toHaveBeenCalledWith({
        email: 'john.doe@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'azerty123'
      });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
      expect(component.onError).toBe(false);
    });

    it('should set onError to true when registration fails', () => {
      mockAuthService.register.mockReturnValue(throwError(() => new Error('Email already used')));
      component.form.setValue({
        email: 'john.doe@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'azerty123'
      });

      component.submit();

      expect(component.onError).toBe(true);
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