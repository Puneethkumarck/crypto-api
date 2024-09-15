FROM eclipse-temurin:21-jre

COPY app/build/libs/app.jar /opt/app.jar

COPY ./deploy/docker/entrypoint.sh /opt/entrypoint.sh

RUN chmod +x /opt/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/opt/entrypoint.sh"]