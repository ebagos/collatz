FROM golang:1.18-alpine3.15 AS build

WORKDIR /src
COPY ./ /src/

RUN GOOS=linux GOARCH=amd64 CGO_ENABLED=0 go build -ldflags="-s -w" -o /outputs/collatz-literal

FROM alpine:3.15
WORKDIR /app
COPY --from=build /outputs/collatz-literal .
ENTRYPOINT ["/app/collatz-literal"]
EXPOSE 50051
