import { parseEvents, parseTicks, listGameEvents,parseGrenades,parseHeader,parsePlayerInfo } from "@laihoe/demoparser2";

const pathToDemo  = "C:\\temp\\auto-20231009-1930-de_overpass-IXI-GO__Monday_Nights.dem"

// Get the names of all events
let eventNames = listGameEvents(pathToDemo);

// Parse all events
let allEvents = parseEvents(pathToDemo, ["all"]);
console.log(allEvents)
// Object to hold event names and their fields
let eventFields: { [eventName: string]: string[] } = {};

eventNames.forEach((eventName: any) => {
    // Filter events by type
    let eventsOfType = allEvents.filter((event: any) => event.event_name === eventName);

    // If there are events of this type, extract field names
    if (eventsOfType.length > 0) {
        // Get the keys of the first event object as field names
        eventFields[eventName] = Object.keys(eventsOfType[0]);
    } else {
        // If no events of this type, note that as well
        eventFields[eventName] = ['No events of this type'];
    }
});

// Logging event fields
console.log(eventFields);
