# Set the base image
FROM igitras/java8

add *.jar /application/gateway/gateway.jar

RUN mkdir /application/gateway -p \
    && cd /application/gateway \
    && mkdir log data tmp conf \

export 15555

CMD echo "Starting gateway application" \
    && java -jar /application/gateway/gateway.jar