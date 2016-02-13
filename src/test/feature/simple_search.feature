Feature: Como usuario quiero poder hacer búsquedas simples por tag (un solo tag en la búsqueda)

	Scenario: El usuario realiza una busqueda simple con una palabra en mayuscula.
		Given el usuarios busca la palabra "JAVA" 
		And la oferta X posee el tag "java"
		When la busqueda es realizada
		Then se muestra la oferta en los resultados

	Scenario: El usuario realiza una busqueda simple con una palabra en minuscula.
		Given el usuarios busca la palabra "java" 
		And la oferta X posee el tag "Java"
		When la busqueda es realizada
		Then se muestra la oferta X en los resultados
		
	Scenario: El usuario realiza una busqueda simple.
		Given el usuarios busca la palabra "Java" 
		And la oferta X posee el tag "JAVA"
		And la oferta Y posee el tag "Java"
		When la busqueda es realizada
		Then se muestran ambas ofertas en los resultados
		
	Scenario: El usuario realiza una busqueda simple.
		Given el usuarios busca la palabra "jAva" 
		And la oferta X posee el tag "java", "junit"
		And la oferta Y posee los tags "spring", "hibernate"
		When la busqueda es realizada
		Then se muestra la oferta X en los resultados
		
	Scenario: El usuario realiza una busqueda con una palabra que contiene espacios en blanco.
		Given el usuarios busca la palabra "ja va" 
		And la oferta X posee el tag "java", "junit"
		And la oferta Y posee los tags "spring", "JAVa"
		When la busqueda es realizada
		Then ambas ofertas se muestran en los resultados.
		