## Guia para verificar la consistencia entre instancias
### Correr los comandos:

`mvn clean install` 

`docker-compose build`

`docker-compose up`

#### Esperar a que termine de levantar todo
en caso de que no levante todo o algún 
contenedor se caiga, hacer `docker-compose down`
e intentar de vuelta. 

Esperar hasta que el broker se estabilice. Lo indica enviando el mensaje ""

Esperar hasta que los dos containers llamados laboratorio3 a y b 
respectivamente terminen de iniciarse, y que se les asigne una particion de
kafka a cada uno (está bien si a uno le aparece que las particiones asignadas son un array vacío).

correr `sh ./runTests.sh`

  