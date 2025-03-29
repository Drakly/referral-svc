FROM openjdk:17

COPY target/referral-svc-0.0.1-SNAPSHOT.jar app1.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app1.jar"]