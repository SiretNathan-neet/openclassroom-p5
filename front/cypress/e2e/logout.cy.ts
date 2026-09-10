describe('Logout spec', () => {

  it('User is logged out', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.intercept('GET', '/api/session', []).as('sessions')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.wait('@sessions')

    cy.contains('span', 'Logout').click()

    cy.contains('span', 'Login').should('be.visible')
    cy.contains('span', 'Register').should('be.visible')
    cy.contains('span', 'Logout').should('not.exist')
    cy.contains('span', 'Sessions').should('not.exist')
  })

});