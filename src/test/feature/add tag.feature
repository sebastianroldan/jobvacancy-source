Feature: Como oferente quiero poder "tagguear" mi ofertas.

	Scenario: El usuario crea una oferta sin especificar tags.
		Given el usuario no especifica un tag
		When la oferta es creada
		Then la oferta no posee tags

	Scenario: El usuario crea una oferta con un solo tag
		Given un usuario especifica un solo tag “java”
		When la oferta es creada
		Then la oferta posee un solo tag “java”

	Scenario: El usuario crea una oferta con varios tags
		Given un usuario especifica varios tags “java, spring, hibernate”
		When la oferta es creada
		Then la oferta posee varios tags “java”,”spring”,”hibernate”
