Feature: Prueba 
	As a admin
  I want to see all proposal that are in process
  So that i can see how they evolved

Scenario: Admin see all proposal that are in process 
	Given the user is on the login page 
	And the user is logger on as admin 
	And the user is on the admin main page 
	When the user clicks on the button "Propuestas en trámite" 
	Then the proposals are in process will be shown to the user