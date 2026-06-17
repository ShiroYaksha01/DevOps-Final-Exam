#!/bin/bash
# Start SSH daemon
/usr/sbin/sshd

# Start NGINX
service nginx start

# Clone the Spring Boot project
cd /app
if [ ! -d "DevOps-Final-Exam" ]; then
    echo "Cloning Spring Boot project..."
    git clone -b ex01 https://github.com/ShiroYaksha01/DevOps-Final-Exam.git
fi

cd DevOps-Final-Exam

# Build the project
echo "Building the project..."
mvn clean package -DskipTests

# Run the project
echo "Starting Spring Boot..."
java -jar target/*.jar
