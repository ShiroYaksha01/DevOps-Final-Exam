FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Install dependencies: JDK 21, NGINX, SSH, Git, Maven
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    nginx \
    openssh-server \
    git \
    maven \
    curl \
    php-cli \
    && rm -rf /var/lib/apt/lists/*

# Configure SSH to listen on port 2222
RUN mkdir -p /var/run/sshd && \
    sed -i 's/#Port 22/Port 2222/' /etc/ssh/sshd_config && \
    echo 'root:Hello@123' | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# Configure NGINX
COPY nginx.conf /etc/nginx/sites-available/default

WORKDIR /app
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080 2222 8081

CMD ["/app/entrypoint.sh"]
