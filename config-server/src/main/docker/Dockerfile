# Set the base image
FROM igitras/java8

add /application/confserver/confserver*.jar /application/confserver/confserver.jar

RUN mkdir /application/confserver -p \
    && cd /application/confserver \
    && mkdir log data tmp conf \

export 18888

CMD echo "Starting confserver application" \
    && java -jar /application/confserver/confserver.jar