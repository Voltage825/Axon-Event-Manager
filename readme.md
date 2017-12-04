# Axon Event Manager

###### Take control of your Axon events 

The premise for this application is to help you manage your broken events in an Axon framework based application.

Connect it directly to the same database as your Axon based application and rebuild your read side for best effect.

___
### Use Cases

Clone the repo and install, then run using spring boot
```bash
mvn clean install
mvn spring-boot:run -Dspring.config.location='your-properties-file'
```

There are 4 available endpoints:
* GET /events
* GET /events/{event ID}
* GET /events/aggregate/{aggregate ID}
* POST /events

| endpoint                          | Method    | Desctiption                                                |
| :-------------------------------- | :-------- | :--------------------------------------------------------- |
| /events                           | GET       | Gets a list of all available events. (probably a bad idea) |
| /events/{event ID}                | GET       | Gets one event.                                            |
| /events/aggregate/{aggregate ID}  | GET       | Gets all events for a specific aggregate.                  |
| /events/{event ID}                | POST      | Updates an event's Payload and PayloadRevision             |

Sample:

```bash
curl -H "Content-Type: application/json" -X POST -d '{"payload": "<com.val.axon.sample.api.domain.client.events.ClientCreatedEvent><clientAggregateId>client1</clientAggregateId><client><name>two</name></client></com.val.axon.sample.api.domain.client.events.ClientCreatedEvent>","payloadRevision": "1"}' http://localhost:8080/events/b456b290-05b9-4083-a9ba-d0deb4e17d91
```

