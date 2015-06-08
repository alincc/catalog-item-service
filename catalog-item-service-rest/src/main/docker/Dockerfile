FROM java:8
ADD catalog-item-service.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "spring.profiles.active=docker", "-jar", "app.jar"]