Steps To start Mail-Poller application as docker container as well as activemq and mysql as same docker container.

1) Go the the base folder of the project.
2) Checkout and pull lates changes on branch "activemq" (without quotes)
3) Update the Email and Password in application.yml
4) run the following command to build-docker image for you application with base image as Java image.
	$>   mvn spring-boot:build-image -DskipTests      
5) run the following command to download, confogure and start all containers i.e. mail-poller, activemq and mysql.
	$> docker-compose up

6) The application will be up and running, if not check the ports in "docker-compose.yml"
7) activemq UI is acceible outside the docker at localhost:8161
8) spring rest endpoints are availble at localhost:8080
9) MySQL Db is accesible at localhost:3307 with username as root and password as Temp@12345.