# Ixigo Server Helper

This is a Spring Boot project used to monitor and perform some custom tasks on the same machine where the CSGO server is running. The mains tasks are:

- Upload the DEM files to the [Ixigo Dem Manager service](../IxigoDemManager/)
- Monitor the CSGO server and send events to the [Ixigo Event Dispatcher service](../IxigoEventDispatcher/) like:
  - Warmup Start / End
  - Round Start / End
