FROM eclipse-temurin:19-jdk-jammy

# Install GrpcCurl
RUN wget --no-check-certificate https://github.com/fullstorydev/grpcurl/releases/download/v1.8.2/grpcurl_1.8.2_linux_x86_64.tar.gz
RUN tar -xvzf grpcurl_1.8.2_linux_x86_64.tar.gz
RUN chmod +x grpcurl

# Add application
ADD target/orders-ms-1.0-SNAPSHOT.jar orders-ms.jar
# Add OpenTelemetry instrumenation lib
RUN wget --no-check-certificate https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# Expose the HTTP Server
EXPOSE 12000
# Expose the GRPC Server
EXPOSE 12001

# Starts orders-api microservice, with OpenTelemetry instrumentation
ENTRYPOINT ["sh","-c","java -javaagent:opentelemetry-javaagent.jar \
                            -jar orders-ms.jar"]
