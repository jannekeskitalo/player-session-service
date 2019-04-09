FROM amazoncorretto:8

COPY target/player-session-service-0.0.1-SNAPSHOT.jar /opt/app.jar

CMD sleep 20 && java -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /opt/app.jar