FROM maven:3.6.3-jdk-8-slim AS builder

WORKDIR /

COPY . .

RUN mvn clean package

FROM tomcat:9.0-jdk8-openjdk
LABEL authors="ceotungbeo"

COPY --from=builder /target/ql_banhang-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]