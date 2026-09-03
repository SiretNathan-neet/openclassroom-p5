import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

import { SessionService } from './session.service';

describe('SessionService (integration test - no mocks needed)', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: 'fake-token',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have isLogged false and no sessionInformation by default', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  describe('LogIn()', () => {
    it('should set sessionInformation, isLogged to true', () => {
      service.logIn(mockUser);
      expect(service.sessionInformation).toEqual(mockUser);
      expect(service.isLogged).toBe(true);
    });

    it('should emit true on $isLogged()', (done) => {
      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(true);
        done();
      });
      service.logIn(mockUser);
    });
  });

  describe('logOut()', () => {
    it('should clear sessionInformation and set isLogged to false', () => {
      service.logIn(mockUser); // on part d'un état connecté
      service.logOut();

      expect(service.sessionInformation).toBeUndefined();
      expect(service.isLogged).toBe(false);
    });

    it('should emit false via $isLogged()', (done) => {
      service.logIn(mockUser); // état initial : connecté

      let callCount = 0;
      service.$isLogged().subscribe(isLogged => {
        callCount++;
        // 1er emit reçu = valeur actuelle (true, car BehaviorSubject rejoue la dernière valeur)
        if (callCount === 1) {
          expect(isLogged).toBe(true);
        }
        // 2e emit = après logOut()
        if (callCount === 2) {
          expect(isLogged).toBe(false);
          done();
        }
      });

      service.logOut();
    });
  });

  describe('$isLogged()', () => {
    it('should return an observable that emits the current isLogged state', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(false); // valeur par défaut
        done();
      });
    });
  });
});
