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