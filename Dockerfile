FROM debian:trixie

USER root
RUN apt update && apt install -y \
    openjdk-21-jdk \
    maven \
    unzip \
    wget \
    curl \
    telnet \
    iproute2 \
    && useradd -ms /bin/bash po23009

USER po23009
WORKDIR /home/po23009

RUN wget https://public.dhe.ibm.com/ibmdl/export/pub/software/openliberty/runtime/release/25.0.0.8/openliberty-jakartaee10-25.0.0.8.zip \
    && unzip openliberty-jakartaee10-25.0.0.8.zip \
    && /home/po23009/wlp/bin/server create servidorPRN335

RUN mkdir -p /home/po23009/wlp/usr/servers/servidorPRN335/resources
RUN wget -O /home/po23009/wlp/usr/servers/servidorPRN335/resources/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.7.2.jar

COPY --chown=po23009:po23009 pom.xml /home/po23009/app-source/pom.xml
WORKDIR /home/po23009/app-source
RUN mvn dependency:go-offline 

COPY --chown=po23009:po23009 src /home/po23009/app-source/src
RUN mvn package -DskipTests

RUN cp target/*.war /home/po23009/wlp/usr/servers/servidorPRN335/dropins/
RUN cp src/main/liberty/config/server.xml /home/po23009/wlp/usr/servers/servidorPRN335/server.xml

EXPOSE 9080
CMD ["/home/po23009/wlp/bin/server", "run", "servidorPRN335"]
