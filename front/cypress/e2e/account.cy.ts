describe('Account spec', () => {

  const login = (admin: boolean) => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin
      },
    })

    cy.intercept('GET', '/api/session', []).as('sessions')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.wait('@sessions')
  }

  it('User information is correctly displayed', () => {
    login(false)

    cy.intercept('GET', '/api/user/1', {
      id: 1,
      email: 'yoga@studio.com',
      lastName: 'lastName',
      firstName: 'firstName',
      admin: false,
      createdAt: '2026-01-01T00:00:00',
      updatedAt: '2026-01-05T00:00:00'
    }).as('userDetail')

    cy.contains('span', 'Account').click()

    cy.wait('@userDetail')

    cy.contains('firstName').should('be.visible')
    cy.contains('LASTNAME').should('be.visible')
    cy.contains('yoga@studio.com').should('be.visible')
  })

  it('Admin badge is displayed for an admin user', () => {
    login(true)

    cy.intercept('GET', '/api/user/1', {
      id: 1,
      email: 'yoga@studio.com',
      lastName: 'lastName',
      firstName: 'firstName',
      admin: true,
      createdAt: '2026-01-01T00:00:00',
      updatedAt: '2026-01-05T00:00:00'
    }).as('userDetail')

    cy.contains('span', 'Account').click()

    cy.wait('@userDetail')

    cy.contains('You are admin').should('be.visible')
  })

});