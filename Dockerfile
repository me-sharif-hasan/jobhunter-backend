FROM ubuntu:22.04

WORKDIR /app
ENV GEMINI_KEY=AIzaSyB2BDJFSdBOE0z8WcvgxKkIdzZYDsuI46U

# Update package lists and install dependencies in smaller batches
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    openjdk-21-jdk \
    maven \
    wget && \
    apt-get clean

# Install Chrome dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libgdk-pixbuf2.0-0 \
    libnss3 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libxkbcommon-x11-0 \
    libpangocairo-1.0-0 \
    libasound2 && \
    apt-get clean

# Install Google Chrome
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt install -y ./google-chrome-stable_current_amd64.deb && \
    rm ./google-chrome-stable_current_amd64.deb

# Copy project files and build
COPY . .

RUN mvn clean package -DskipTests && cp target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]


# Use GraalVM base image instead of Ubuntu
#FROM ghcr.io/graalvm/graalvm-community:21-ol9 as builder
#
#WORKDIR /app
#ENV GEMINI_KEY=AIzaSyB2BDJFSdBOE0z8WcvgxKkIdzZYDsuI46U
#
## Install only essential build tools
#RUN microdnf install -y wget unzip maven && \
#    microdnf clean all
#
## Enable additional repositories and install Chrome dependencies (minimal set for headless)
#RUN microdnf install -y epel-release && \
#    microdnf install -y \
#    nss \
#    libX11 \
#    libXcomposite \
#    libXdamage \
#    libXfixes \
#    libXrandr \
#    libgbm \
#    libxkbcommon \
#    pango \
#    alsa-lib && \
#    microdnf clean all
#
## Install Chrome
#RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm && \
#    microdnf install -y ./google-chrome-stable_current_x86_64.rpm && \
#    rm ./google-chrome-stable_current_x86_64.rpm
#
## Copy project files
#COPY . .
#
## Build native image with Maven
## Note: Your project needs to be configured for GraalVM native image
#RUN mvn clean package -Pnative -DskipTests && \
#    mv target/*-runner app
#
## Final runtime image
#FROM debian:bookworm-slim
#
#WORKDIR /app
#
## Install only runtime dependencies for Chrome headless
#RUN apt-get update && \
#    apt-get install -y --no-install-recommends \
#    libnss3 \
#    libx11-6 \
#    libxcomposite1 \
#    libxdamage1 \
#    libxfixes3 \
#    libxrandr2 \
#    libgbm1 \
#    libxkbcommon0 \
#    libpango-1.0-0 \
#    libasound2 && \
#    apt-get clean && \
#    rm -rf /var/lib/apt/lists/*
#
## Copy Chrome from builder
#COPY --from=builder /opt/google/chrome /opt/google/chrome
#
## Copy the native executable
#COPY --from=builder /app/app /app/app
#
#EXPOSE 8080
#
## Run in headless mode by default
#CMD ["/app/app", "--headless"]