## Guia para verificar la consistencia entre instancias
### Correr los comandos:

`mvn dependency:resolve`

`mvn clean package`


`docker-compose build`

`docker-compose up`

#### Esperar a que termine de levantar todo
en caso de que no levante todo o algún 
contenedor se caiga, hacer `docker-compose down`
e intentar de vuelta. 

Esperar hasta que el broker se estabilice. Lo indica enviando el mensaje "Stabilized group Bordero-B..."

Esperar hasta que los containers correspondientes a los backends terminen de iniciarse, y que se les asigne una particion de
kafka a cada uno.

Si a alguno no se le asigna una partición, reiniciar los tres containers de backend y esperar a que se les asigne una partición. 

correr `sh ./runTests.sh`

  