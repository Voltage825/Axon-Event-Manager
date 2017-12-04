package com.val.axon.aem.services;

import java.util.UUID;

import com.val.axon.aem.persistance.entity.DomainEventEntry;
import com.val.axon.aem.model.dto.ModifiedEventDto;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventConverterDefaultUnitTest {
    private EventConverterDefault eventConverterDefault;

    @Before
    public void setUp() throws Exception {
        eventConverterDefault = new EventConverterDefault();
    }

    @Test
    public void when_updatingBothPayloadAndRevision_expect_splicedResultToReflectBothChanges() throws Exception {
        String eventIdentifier = UUID.randomUUID().toString();

        DomainEventEntry domainEventEntry = getStubDomainEventEntry(eventIdentifier);

        ModifiedEventDto modifiedEventDto = new ModifiedEventDto();
        modifiedEventDto.setPayload("new payload");
        modifiedEventDto.setPayloadRevision("2");

        DomainEventEntry splice = eventConverterDefault.splice(domainEventEntry, modifiedEventDto);

        assertThat("Payload is wrong.", splice.getPayload(), is(modifiedEventDto.getPayload().getBytes()));
        assertThat("Payload revision is wrong.", splice.getPayloadRevision(), is(modifiedEventDto.getPayloadRevision()));
    }

    @Test
    public void when_updatingPayload_expect_splicedResultToReflectPayloadChangesOnly() throws Exception {
        String eventIdentifier = UUID.randomUUID().toString();

        DomainEventEntry domainEventEntry = getStubDomainEventEntry(eventIdentifier);

        ModifiedEventDto modifiedEventDto = new ModifiedEventDto();
        modifiedEventDto.setPayload("new payload");

        DomainEventEntry splice = eventConverterDefault.splice(domainEventEntry, modifiedEventDto);

        assertThat("Payload is wrong.", splice.getPayload(), is(modifiedEventDto.getPayload().getBytes()));
        assertThat("Payload revision should stay the same.", splice.getPayloadRevision(), is(domainEventEntry.getPayloadRevision()));
    }

    @Test
    public void when_updatingPayloadRevision_expect_splicedResultToReflectPayloadRevisionChangesOnly() throws Exception {
        String eventIdentifier = UUID.randomUUID().toString();

        DomainEventEntry domainEventEntry = getStubDomainEventEntry(eventIdentifier);

        ModifiedEventDto modifiedEventDto = new ModifiedEventDto();
        modifiedEventDto.setPayloadRevision("2");

        DomainEventEntry splice = eventConverterDefault.splice(domainEventEntry, modifiedEventDto);

        assertThat("Payload should stay the same.", splice.getPayload(), is(domainEventEntry.getPayload()));
        assertThat("Payload revision is wrong.", splice.getPayloadRevision(), is(modifiedEventDto.getPayloadRevision()));
    }

    private DomainEventEntry getStubDomainEventEntry(String eventIdentifier) {
        DomainEventEntry domainEventEntry = new DomainEventEntry();

        domainEventEntry.setGlobalIndex(0L);
        domainEventEntry.setEventIdentifier(eventIdentifier);
        domainEventEntry.setMetadata("Old meta".getBytes());
        domainEventEntry.setPayload("Old payload".getBytes());
        domainEventEntry.setPayloadRevision("1");
        domainEventEntry.setPayloadType("something.type");
        domainEventEntry.setDate("oldDate");
        domainEventEntry.setAggregateIdentifier("Aggregate1");
        domainEventEntry.setSequenceNumber(0L);
        domainEventEntry.setType("myType");

        return domainEventEntry;
    }
}