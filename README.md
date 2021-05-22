# Readme

### Running the project through Docker

* Navigate to <code>src/main/resource/application.properties</code> and set the base-url and test-data-endpoint values

* Run the following command on the terminal: <code>mvn spring-boot:build-image -Dspring-boot.build-image.imageName=data-aggregator-project
  </code>
  
* To start the docker container run: <code>docker run -p 8080:8080 data-aggregator-project</code> or adding the option -d if you want to run it in the background

* The service can be stopped with either Ctrl + C or, in case you are running it in the background, with <code>docker stop *containerId*</code> 


