FROM maven:3.9-eclipse-temurin-21-jammy

# Install Google Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
       > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Cache Maven dependencies separately from source so rebuilds are fast
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src

# Tests run headless by default inside the container
CMD ["mvn", "test", "-Dheadless=true"]
