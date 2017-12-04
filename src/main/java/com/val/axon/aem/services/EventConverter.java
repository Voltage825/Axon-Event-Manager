package com.val.axon.aem.services;

import java.util.List;

import com.val.axon.aem.persistance.entity.DomainEventEntry;
import com.val.axon.aem.model.dto.EventDto;
import com.val.axon.aem.model.dto.ModifiedEventDto;

public interface EventConverter {

    EventDto convert(DomainEventEntry domainEventEntry);

    List<EventDto> convert(List<DomainEventEntry> domainEventEntries);

    DomainEventEntry splice(DomainEventEntry domainEventEntry, ModifiedEventDto modifiedEventDto);
}
