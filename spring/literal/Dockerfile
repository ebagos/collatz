FROM openjdk:11.0.14.1-jdk-buster AS build

ARG PROTOC_VERSION="3.20.0"

#ADD https://github.com/google/protobuf/releases/download/v${PROTOC_VERSION}/protoc-${PROTOC_VERSION}-linux-x86_64.zip ./
#RUN unzip protoc-${PROTOC_VERSION}-linux-x86_64.zip -d /usr/local

ENV PROTOC /usr/local/bin/protoc
ENV PROTOC_INCLUDE /usr/local/include
ENV INCLUDE /usr/local/bin

WORKDIR /src
COPY ./ .
RUN ./mvnw install

FROM openjdk:11.0.14.1-jre-buster
WORKDIR /
COPY --from=build /src/target/literal-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "literal-0.0.1-SNAPSHOT.jar"]
EXPOSE 6565
EXPOSE 8080
