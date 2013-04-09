# Remi

A simple Clojure web service prototype of the Epochal Time Model described in Rick Hickey's talk ["The Language of the system"](http://skillsmatter.com/podcast/scala/the-language-of-the-system).

The application is a simple RESTful web service that enables the persistence of arbitrary JSON data structures. Riak is used to store values that are treated as immutable, and Zookeeper is used to reference these values.

With each successive "update" (via PUT requests) the system creates a new value in Riak and updates Zookeper to reference it. Zookeeper maintains all references to older values, so over time we can recall all the previous values of an entity and therefore the persistence behaves like real memory as opposed to place-oriented storage.

## Usage

### Setup

- Use the settings.clj_example file to create settings.clj with your own configuration
- Run with `lein run server`

### Creating a value

With cURL we can store some data via:
`curl -d "{\"test\": \"some data\"}" -H "Content-type: application/json" http://localhost:3000/bucket/clj-test`

which responds with `{"test":"some data","id":"8c781c84-b77b-4d1b-8d5e-1c50bd552495"}`

### Retrieving a value

We can then retrieve the value with:
`curl http://localhost:3000/bucket/clj-test/8c781c84-b77b-4d1b-8d5e-1c50bd552495`

which returns `{"test":"some data","id":"8c781c84-b77b-4d1b-8d5e-1c50bd552495"}`

### Updating a value

Note: That we are not _really_ updating a value, simply storing a new value in Riak and updating
Zookeeper to point to it.

We send an update request via:
`curl -d "{\"test\": \"and something else\"}" -X PUT -H "Content-type: application/json" http://localhost:3000/bucket/clj-test/8c781c84-b77b-4d1b-8d5e-1c50bd552495`

which returns: `{"test":"and something else","id":"8c781c84-b77b-4d1b-8d5e-1c50bd552495"}`

### Summary

We now have a succession of states in the database which represent the identity of `8c781c84-b77b-4d1b-8d5e-1c50bd552495` (best not to read aloud)

## Todo

- Request path for retrieving all historical values of a reference
- Hide the concept of "buckets" from the user?

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.