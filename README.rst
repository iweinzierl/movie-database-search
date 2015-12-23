=====================
movie-database-search
=====================

.. image:: https://travis-ci.org/iweinzierl/movie-database-search.svg?branch=master
   :target: https://travis-ci.org/iweinzierl/movie-database-search
   :alt: Build Status

Backend application for movie-database. This service provides a REST API to search movies using third party services
like TMDb (The Movie Database).

Run
---
.. code-block:: bash

   $ mvn package
   $ #mvn package -P production  // build for production (copies resources from src/main/envs/production

   $ java -jar target/moviedatabase-search-0.0.1-SNAPSHOT.jar
   $ #java -jar target/moviedatabase-search-0.0.1-SNAPSHOT.jar --server.port=7722  // adapt the server port


License
=======

Copyright 2013-2015 Ingo Weinzierl

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
