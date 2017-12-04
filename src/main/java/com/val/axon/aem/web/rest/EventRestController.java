package com.val.axon.aem.web.rest;

import java.text.ParseException;
import java.util.List;

import com.val.axon.aem.model.dto.EventDto;
import com.val.axon.aem.model.dto.ModifiedEventDto;
import com.val.axon.aem.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/events")
public class EventRestController {

    private EventService eventService;

    @Autowired
    public EventRestController(final EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(
            value = "",
            method = GET)
    public List<EventDto> getAll() throws ParseException {
        return eventService.getAllEvents();
    }

    @RequestMapping(
            value = "/{id}",
            method = GET)
    public HttpEntity<EventDto> getOne(@PathVariable("id") final String eventId) throws ParseException {
        EventDto eventDto = eventService.getOneByEventIdentifier(eventId);
        if (eventDto == null) {
            return new ResponseEntity<>((EventDto) null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(eventDto, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "/aggregate/{id}",
            method = GET)
    public List<EventDto> getEventsForAggregate(@PathVariable("id") final String aggregateId) throws ParseException {
        return eventService.getEventsForAggregate(aggregateId);
    }

    @RequestMapping(
            value = "/{id}",
            method = POST)
    public HttpEntity<EventDto> updateEvent(@PathVariable("id") final String eventId, @RequestBody final ModifiedEventDto modifiedEventDto) throws ParseException {
        EventDto eventDto = eventService.updateEvent(eventId, modifiedEventDto);
        if (eventDto == null) {
            return new ResponseEntity<>((EventDto) null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(eventDto, HttpStatus.ACCEPTED);
        }
    }
}
