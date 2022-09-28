Como testear el laboratorio 2:

correr el script que inicia las imagenes y esperar a que termine de correr:

    sh startInstances.sh

activar el ambiente virtual de python (requiere python3):

    source requests/bin/activate

correr el script de python:

    python3 tests.py

al terminar de correr el script deberían mostrarse los saldos e inserciones, lo unico que quedaría sería
desactivar el ambiente virtual:

    deactivate