describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {},
    }).as('registerSuccess')

    cy.get('input[formControlName=firstName]', { timeout: 10000 }).type("John")
    cy.get('input[formControlName=lastName]').type("Doe")
    cy.get('input[formControlName=email]').type("john.doe@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.wait('@registerSuccess')

    cy.url().should('include', '/login')
  })

  it('Register failed - missing required field', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]', { timeout: 10000 }).type("John")
    cy.get('input[formControlName=lastName]').type("Doe")
    cy.get('input[formControlName=email]').type("john.doe@studio.com")
    // password left empty on purpose

    cy.get('button[type=submit]').should('be.disabled')
  })
});