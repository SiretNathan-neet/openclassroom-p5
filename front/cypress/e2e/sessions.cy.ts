describe('Sessions spec', () => {

  const teacher = {
    id: 1,
    firstName: 'Margot',
    lastName: 'DELAHAYE',
    createdAt: '2023-01-01T00:00:00',
    updatedAt: '2023-01-01T00:00:00'
  }

  const session = {
    id: 1,
    name: 'Yoga session',
    description: 'A relaxing yoga session',
    date: '2026-09-15T00:00:00.000Z',
    teacher_id: 1,
    users: [],
    createdAt: '2026-09-01T00:00:00.000Z',
    updatedAt: '2026-09-01T00:00:00.000Z'
  }

  const loginAsAdmin = () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'admin',
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  }

  it('Sessions list is displayed', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.contains(session.name).should('be.visible')
  })

  it('Create and Detail buttons appear for admin', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.contains('button', 'Create').should('be.visible')
    cy.contains('button', 'Detail').should('be.visible')
  })

  it('Session information is correctly displayed', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/session/1', session).as('sessionDetail')
    cy.intercept('GET', '/api/teacher/1', teacher).as('teacherDetail')

    cy.contains('button', 'Detail').click()

    cy.wait('@sessionDetail')
    cy.wait('@teacherDetail')

    cy.contains(session.name, { matchCase: false }).should('be.visible')
    cy.contains(session.description).should('be.visible')
    cy.contains(teacher.firstName).should('be.visible')
  })

  it('Delete button appears for admin on session detail', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/session/1', session).as('sessionDetail')
    cy.intercept('GET', '/api/teacher/1', teacher).as('teacherDetail')

    cy.contains('button', 'Detail').click()
    cy.wait('@sessionDetail')
    cy.wait('@teacherDetail')

    cy.contains('button', 'Delete').should('be.visible')
  })

  it('Session is created', () => {
    cy.intercept('GET', '/api/session', []).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/teacher', [teacher]).as('teachers')
    cy.intercept('POST', '/api/session', session).as('createSession')
    cy.intercept('GET', '/api/session', [session]).as('sessionsAfterCreate')

    cy.contains('button', 'Create').click()
    cy.wait('@teachers')

    cy.get('input[formControlName=name]').type('Yoga session')
    cy.get('input[formControlName=date]').type('2026-09-15')
    cy.get('mat-select[formControlName=teacher_id]').click()
    cy.get('mat-option').contains(teacher.firstName).click()
    cy.get('textarea[formControlName=description]').type('A relaxing yoga session')

    cy.get('button[type=submit]').click()

    cy.wait('@createSession')
    cy.url().should('include', '/sessions')
  })

  it('Session creation shows error on missing required field', () => {
    cy.intercept('GET', '/api/session', []).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/teacher', [teacher]).as('teachers')

    cy.contains('button', 'Create').click()
    cy.wait('@teachers')

    cy.get('input[formControlName=name]').type('Yoga session')
    // date, teacher and description left empty on purpose

    cy.get('button[type=submit]').should('be.disabled')
  })

  it('Session is updated', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/session/1', session).as('sessionDetail')
    cy.intercept('GET', '/api/teacher', [teacher]).as('teachers')
    cy.intercept('PUT', '/api/session/1', session).as('updateSession')

    cy.contains('button', 'Edit').click()

    cy.wait('@sessionDetail')
    cy.wait('@teachers')

    cy.get('input[formControlName=name]').clear().type('Updated Yoga session')

    cy.get('button[type=submit]').click()

    cy.wait('@updateSession')
    cy.url().should('include', '/sessions')
  })

  it('Session update shows error on missing required field', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/session/1', session).as('sessionDetail')
    cy.intercept('GET', '/api/teacher', [teacher]).as('teachers')

    cy.contains('button', 'Edit').click()

    cy.wait('@sessionDetail')
    cy.wait('@teachers')

    cy.get('input[formControlName=name]').clear()

    cy.get('button[type=submit]').should('be.disabled')
  })

  it('Session is deleted', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions')
    loginAsAdmin()
    cy.wait('@sessions')

    cy.intercept('GET', '/api/session/1', session).as('sessionDetail')
    cy.intercept('GET', '/api/teacher/1', teacher).as('teacherDetail')
    cy.intercept('DELETE', '/api/session/1', {}).as('deleteSession')

    cy.contains('button', 'Detail').click()
    cy.wait('@sessionDetail')
    cy.wait('@teacherDetail')

    cy.contains('button', 'Delete').click()

    cy.wait('@deleteSession')
    cy.url().should('include', '/sessions')
  })

});