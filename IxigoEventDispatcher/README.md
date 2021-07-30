# Ixigo Event Dispatcher

This is a Spring Boot service which will listend for the events fired by the [Ixigo Server Helper](../IxigoServerHelper/) service and dispatch them to all the registered listeners.

## Register a CSGO Event Listener

The **Ixigo Event Dispatcher** service allows you to register you app and listen for some events which are fired by our CSGO dedicated server. [Here you can find](https://marco.selfip.net/ixigoproxy/ixigo-event-dispatcher/eventsdispatcher/swagger-ui.html) the APIs documentation to register / un-register your app

Supported events:

- **ROUND_END**: Triggered when the round ends (The warmup round does NOT trigger this event)
- **ROUND_START**: Triggered when a new round starts (The warmup round triggers this event)
- **CS_WIN_PANEL_MATCH**: Triggered when the match is over and the "Vote Screen" is displayed
- **WARMUP_START**: Triggered when the warmup starts
- **WARMUP_END**: This is calculated by the service, it will be fired when CSGO distpaches the first "ROUND_START" after the "WARMUP_START"
- **SHUT_DOWN**: The server will be turned off in a couple of minutes

### Register an event listener

To register your event listener you have to perform an HTTP POST request to the service and provide:

- The name of the event that you want to register for
- The URL to call back when the specific event occurs.

**IMPORTANT**

- When the CSGO event is fired, your URL will receive an HTTP POST with a "content-type: application/json" and a JSON object in the body simlar to this one: "{"eventTime":"2021-05-01T19:35:32.178Z", "eventType": "ROUND_END" }"
- Your endpoint must reply with an OK HTTP Status Code (200)
- If your endpoint does not return 200 for more than three times in a row it will be "disabled". In this case you have to "un-register" and register it again

### **Examples**

**Register an event Listener**

Following is an example of registration of an event listener for the "CS_WIN_PANEL_MATCH" event

~~~~bash
curl --location --request POST 'https://marco.selfip.net/ixigoproxy/ixigo-event-dispatcher/eventsdispatcher/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "http://192.168.1.26:8080/myservicelistener/events",
    "eventType": "CS_WIN_PANEL_MATCH"
}'
~~~~

**Event Dispatched**

Following is an example of a dispatched event from the service to your event listener

~~~~bash
curl --location --request POST 'http://192.168.1.26:8080/myservicelistener/events' \
--header 'Content-Type: application/json' \
--data-raw '{
  "eventTime": "2021-05-01T19:35:32.178Z",
  "eventType": "CS_WIN_PANEL_MATCH"
}'
~~~~