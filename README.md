
![Diseño y recorrido natural de la app android](https://github.com/victorvace/absence_app/blob/master/logo.png "Diseño y recorrido natural de la app android") # Absence app

Este repositorio corresponde al proyecto de fin de curso del grado superior de Desarrollo de Aplicaciones Multiplataforma. Su función es la de poder gestionar faltas de asistencia en un centro educativo, a través de una app Android que se conecta a una API-REST hecha en Java. (APP y Backend para la administración de faltas de asistencia en un centro educativo) 

##Tecnologías y desarrollo

El sistema tiene una arquitectura de cliente-servidor. El servidor debe estár formado por un servidor Tomcat y un sistema gestor de base de datos (MariaDB) corriendo sobre Linux. Dicho servidor hará servir la API-REST sobre la aplicació Android completando el sistema.

La api está programada con Java 8, y hace uso de Hibernate para comunicarse con la base de datos. Además se ha hecho uso de Maven para descargar todos los .jar y empaquetarlos para poder exportar la api al servidor y que funcionase correctamente.

También se ha utilizado el JDK de android para crear una app cliente para la API en Android. La aplicación funciona desde Android 7.1.1 hasta 8.1. Es probable que funcione en en versiones anteriores aunque no ha sido testeado ni enfocado a ello.

##Diseño de interfaces de usuario

![Diseño y recorrido natural de la app android](https://github.com/victorvace/absence_app/blob/master/interfaz.png "Diseño y recorrido natural de la app android")
 
##Diseño de la base de datos

![Diseño de la base de datos](https://github.com/victorvace/absence_app/blob/master/BBDD.png "Diseño de la base de datos")
