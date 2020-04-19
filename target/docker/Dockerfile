FROM dockerfile/java:oracle-java8

VOLUME /tmp
ADD stamp.jar /opt/stamp/
EXPOSE 8080
WORKDIR /opt/stamp/
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xms512m", "-Xmx1g", "-jar", "stamp.jar"]
