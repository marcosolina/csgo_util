# Ixigo Dem Manager

This Spring Boot Microservice is used to store and retrieve the information extracted byt the "dem" files. When a CSGO round is ended, the [Ixigo Server Helper](../IxigoServerHelper/) service will upload to this service the new dem file. This service will then use the [Dem Parser](../DemParser/) to extract the players scores and then store them into the database for later user.
