# weapons-shop

An example Clojure API using a variety of relevant libraries.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    docker-compose up --build

The server will be available on port 3000.

## Testing

To run the test suite, run:

    lein test


## Exercises

### Inconsistency between API and database

You may notice that the API expects a JSON key called `cost` but the
database uses one called `price`. This currently leaks out to the API
consumer in responses. Write helper functions that convert between the
two formats and use them in `db.clj`.

### Ensuring keys are present

Another quirk of the interaction between the database library and JSON
API is the treatment of optional keys. We want to be able to leave the
key off in the JSON API but the DB library expects an explicit
null. The current code does this somewhat hackily; write a helper
function that ensures a given key is present in the map, with a nil
value if it's absent, then use it in `db.clj`.


### Test the damage and cost fields

Right now the tests only check the returned name field on response
bodies. Check damage and cost too

### Test the optional field

The tests currently don't pass the optional `durability` field. Add a
new test that uses it.

## License

Copyright © 2022 Danielle Brook-Roberge
