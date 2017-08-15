FROM gradle:alpine
USER root
ENV GRADLE_USER_HOME=/opt/gradle
ADD . ./
RUN gradle build --parallel
CMD ["java","-Xmx256m","-jar","build/libs/gradle-0.1.jar"]
