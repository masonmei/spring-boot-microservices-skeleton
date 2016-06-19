# Set the base image
FROM igitras/java8

add /application/registry/registry*.jar /application/registry/registry.jar

RUN mkdir /application/registry -p \
    && cd /application/registry \
    && mkdir log data tmp conf \

export 17777

CMD echo "Starting registry application" \
    && java -jar /application/registry/registry.jar