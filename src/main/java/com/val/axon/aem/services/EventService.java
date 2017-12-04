package com.val.axon.aem.services;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.val.axon.aem.model.dto.ModifiedEventDto;
import com.val.axon.aem.persistance.entity.DomainEventEntry;
import com.val.axon.aem.persistance.repository.DomainEventEntryRepository;
import com.val.axon.aem.model.dto.EventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final DomainEventEntryRepository domainEventEntryRepository;
    private final EventConverter eventConverter;

    @Autowired
    public EventService(final DomainEventEntryRepository domainEventEntryRepository, final EventConverterDefault eventConverterDefault) {
        this.domainEventEntryRepository = domainEventEntryRepository;
        this.eventConverter = eventConverterDefault;
    }

    public List<EventDto> getAllEvents() throws ParseException {
        return eventConverter.convert(domainEventEntryRepository.findAll());
    }

    public EventDto getOneByEventIdentifier(final String eventIdentifier) throws ParseException {
        Optional<DomainEventEntry> domainEventEntryOptional = domainEventEntryRepository.findByEventIdentifier(eventIdentifier);

        if (domainEventEntryOptional.isPresent()) {
            return eventConverter.convert(domainEventEntryOptional.get());
        } else {
            return null;
        }
    }

    public List<EventDto> getEventsForAggregate(final String aggregateId) throws ParseException {
        return eventConverter.convert(domainEventEntryRepository.findAllByAggregateIdentifier(aggregateId));
    }

    public EventDto updateEvent(final String eventId, final ModifiedEventDto modifiedEventDto) throws ParseException {
        Optional<DomainEventEntry> optionalEntity = domainEventEntryRepository.findByEventIdentifier(eventId);
        if (optionalEntity.isPresent()) {
            return eventConverter.convert(
                    domainEventEntryRepository.save(
                            eventConverter.splice(optionalEntity.get(), modifiedEventDto)));
        } else {
            return null;
        }
    }
}
