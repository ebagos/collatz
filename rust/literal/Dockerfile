FROM rust:1.60.0-bullseye AS build

ARG PROTOC_VERSION="3.20.0"

RUN apt-get update \
  && apt-get upgrade -y \
  && apt-get install -y -q \
  ca-certificates \
  locales \
  apt-transport-https\
  libssl-dev \
  libpq-dev \
  pkg-config \
  curl \
  build-essential \
  libdbus-1-dev \
  libsqlite3-dev \
  mariadb-client \
  git \
  wget 

ADD https://github.com/google/protobuf/releases/download/v${PROTOC_VERSION}/protoc-${PROTOC_VERSION}-linux-x86_64.zip ./
RUN unzip protoc-${PROTOC_VERSION}-linux-x86_64.zip -d /usr/local

ENV PROTOC /usr/local/bin/protoc
ENV PROTOC_INCLUDE /usr/local/include
ENV INCLUDE /usr/local/bin

WORKDIR /rust
COPY . /rust/

RUN RUSTFLAGS="-C target-feature=-crt-static" cargo build --release

FROM debian:bullseye-slim
WORKDIR /app
COPY --from=build /rust/target/release/collatz-server .
ENTRYPOINT ["/app/collatz-server"]
EXPOSE 50051
