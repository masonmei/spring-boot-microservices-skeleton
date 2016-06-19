# Set the base image
FROM igitras/java8

add /application/blog/blog*.jar /application/blog/blog.jar

RUN mkdir /application/blog -p \
    && cd /application/blog \
    && mkdir log data tmp conf \

export 18082

CMD echo "Starting blog application" \
    && java -jar /application/blog/blog.jar