# Set the base image
FROM igitras/java8

add /application/authserver/authserver*.jar /application/authserver/authserver.jar

RUN mkdir /application/authserver -p \
    && cd /application/authserver \
    && mkdir log data tmp conf \

export 19999

CMD echo "Starting authserver application" \
    && java -jar /application/authserver/authserver.jar