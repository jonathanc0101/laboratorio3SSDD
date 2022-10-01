Como testear el laboratorio 2:


Ejecutar el comando:
    mvn clean verify docker:build

Correr el script que inicia las imagenes y esperar a que termine de correr (alrededor de 30 segundos):

    sh startInstances.sh

Ejecutar el programa en java ubicado en:

    "/src/test/java/com.example.test1/ApplicationTest1Tests"

Si esta todo bien se pueden cerrar los contenedores con el comando

    sh stopInstances.sh