FROM ibmjava:8-sdk-alpine AS build

WORKDIR /src
COPY ./ /src
RUN ./gradlew installDist

FROM ibmjava:8-jre-alpine
WORKDIR /
COPY --from=build /src/server/build/install/server /server
ENTRYPOINT ["/server/bin/collatz-server"]
EXPOSE 50051
