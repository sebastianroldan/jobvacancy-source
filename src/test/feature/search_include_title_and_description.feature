Feature: Como usuario quiero que la busqueda de jobs, tenga en cuenta las descripciones y los titles

	Scenario: El usuario realiza una busqueda con una palabra contenida en el titulo.
		Given el usuarios busca la palabra "Movil" 
		And la oferta X posee el titulo "Aplicaciones movil"
		When la busqueda es realizada
		Then la oferta X se muestra en los resultados.
		
	Scenario: El usuario realiza una busqueda con una palabra contenida en la descripcion.
		Given el usuarios busca la palabra "Apps" 
		And la oferta X posee la descripcion "Desarrollo de apps para IOs y Android"
		When la busqueda es realizada
		Then la oferta X se muestra en los resultados.
		
	Scenario: El usuario realiza una busqueda con una palabra contenida en la descripcion, en el titulo y en los tags de diferentes ofertas.
		Given el usuarios busca la palabra "Apps" 
		And la oferta X posee la descripcion "Desarrollo de apps para IOs y Android"
		And la oferta Y posee el titulo "Android Apps"
		And la oferta z posee los tags "Apps", "movil", "Android"
		When la busqueda es realizada
		Then las tres ofertas se muestran en los resultados.