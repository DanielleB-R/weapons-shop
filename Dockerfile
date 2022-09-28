FROM clojure:lein

WORKDIR /code

COPY project.clj .
RUN lein deps

COPY . /code

RUN lein ring uberjar

CMD java -jar target/weapons-shop-0.1.0-SNAPSHOT-standalone.jar
