# language: es

Característica: el usuario debe poder votar las propuestas

Escenario: Votar las propuestas
	Cuando el usuario se logea en la aplicacion
	Entonces puede ver todas las propuestas en tramite
	Entonces votara la propuesta 3 a favor
	Entonces votara la propuesta 4 en contra