FROM openjdk:17

COPY target/referral-svc-*.jar app1.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app1.jar"]