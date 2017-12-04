package com.val.axon.aem.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.val.axon.aem.persistance.entity.DomainEventEntry;
import com.val.axon.aem.exceptions.EventDateParseException;
import com.val.axon.aem.model.dto.EventDto;
import com.val.axon.aem.model.dto.ModifiedEventDto;
import org.springframework.stereotype.Service;

@Service
public class EventConverterDefault implements EventConverter {

    private final SimpleDateFormat simpleDateFormat;

    public EventConverterDefault() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public EventDto convert(final DomainEventEntry domainEventEntry) {
        EventDto eventDto = new EventDto();
        try {
            eventDto.setGlobalIndex(domainEventEntry.getGlobalIndex());
            eventDto.setEventIdentifier(domainEventEntry.getEventIdentifier());
            eventDto.setMetadata(new String(domainEventEntry.getMetadata()));
            eventDto.setPayload(new String(domainEventEntry.getPayload()));
            eventDto.setPayloadRevision(domainEventEntry.getPayloadRevision());
            eventDto.setPayloadType(domainEventEntry.getPayloadType());
            eventDto.setDate(getParsedDate(domainEventEntry));
            eventDto.setAggregateIdentifier(domainEventEntry.getAggregateIdentifier());
            eventDto.setSequenceNumber(domainEventEntry.getSequenceNumber());
            eventDto.setType(domainEventEntry.getType());
        } catch (ParseException e) {
            throw new EventDateParseException(e);
        }

        return eventDto;
    }

    private Date getParsedDate(DomainEventEntry domainEventEntry) throws ParseException {
        return simpleDateFormat.parse(domainEventEntry.getDate());
    }

    @Override
    public List<EventDto> convert(List<DomainEventEntry> domainEventEntries) {
        return domainEventEntries.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public DomainEventEntry splice(final DomainEventEntry domainEventEntry, final ModifiedEventDto modifiedEventDto) {

        if (modifiedEventDto.getPayload() != null && !modifiedEventDto.getPayload().trim().isEmpty()) {
            domainEventEntry.setPayload(modifiedEventDto.getPayload().getBytes());
        }

        if (modifiedEventDto.getPayloadRevision() != null && !modifiedEventDto.getPayloadRevision().trim().isEmpty()) {
            domainEventEntry.setPayloadRevision(modifiedEventDto.getPayloadRevision());
        }

        return domainEventEntry;
    }

}
