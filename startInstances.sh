mvn docker:start -Dspring.port=8081 -Ddb=midb1
mvn docker:start -Dspring.port=8082 -Ddb=midb2
mvn docker:start -Dspring.port=8083 -Ddb=midbc3

echo "INSTANCIAS CREADAS EN LOS PUERTOS 8081, 8082, Y 8083!"