FROM image-registry.openshift-image-registry.svc:5000/build-strategies/ubi8-modified
COPY ./basic-op-add-1.0.0.jar /app.jar

EXPOSE 8001/tcp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]