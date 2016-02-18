Feature: Como usuario quiero poder hacer búsquedas avanzadas por tag de manera de especificar varios tags y user operadores AND y OR.

	Scenario: El usuario realiza una busqueda con el operador AND.
		Given el usuarios busca la palabra "JAVA AND junit" 
		And la oferta X posee el tag "java, Junit"
		And la oferta Y posee el tag "JUNIT"
		When la busqueda es realizada
		Then se muestra la oferta X en los resultados

	Scenario: El usuario realiza una busqueda con el operador OR.
		Given el usuarios busca la palabra "java OR Junit" 
		And la oferta X posee el tag "Java"
		And la oferta Y posee el tag "JUNIT"
		When la busqueda es realizada
		Then se muestran ambas ofertas en los resultados
		
	Scenario: El usuario realiza una busqueda con tres tags y el operador AND.
		Given el usuarios busca la palabra "Java AND junit AND TDD" 
		And la oferta X posee el tag "JAVA, Junit, tdd"
		And la oferta Y posee el tag "Java, TDD"
		When la busqueda es realizada
		Then se muestra la oferta X en los resultados
		
	Scenario: El usuario realiza una busqueda con el operador OR y tres tags.
		Given el usuarios busca la palabra "javascript OR Web OR angular" 
		And la oferta X posee el tag "javascript", "junit"
		And la oferta Y posee los tags "javascript", "ANGULAR"
		And la oferta Z posee el tag "Web", "CSS"
		When la busqueda es realizada
		Then se muestran las tres ofertas en los resultados