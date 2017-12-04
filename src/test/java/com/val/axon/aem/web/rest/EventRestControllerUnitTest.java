package com.val.axon.aem.web.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.val.axon.aem.EventApplication;
import com.val.axon.aem.model.dto.EventDto;
import com.val.axon.aem.model.dto.ModifiedEventDto;
import com.val.axon.aem.persistance.entity.DomainEventEntry;
import com.val.axon.aem.persistance.repository.DomainEventEntryRepository;
import com.val.axon.aem.services.EventConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class EventRestControllerUnitTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private String baseUrl = "/events";

    private List<DomainEventEntry> aggregateTwoDomainEventEntries;
    private List<DomainEventEntry> aggregateOneDomainEventEntries;
    private List<DomainEventEntry> allDomainEventEntries;

    private final String aggregateOneType = "AggregateOne";
    private final String aggregateTwoType = "AggregateTwo";
    private final String aggregateOneId = UUID.randomUUID().toString();
    private final String aggregateTwoId = UUID.randomUUID().toString();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DomainEventEntryRepository domainEventEntryRepository;

    @Autowired
    private EventConverter eventConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    @Before
    public void setUp() throws Exception {
        domainEventEntryRepository.deleteAll();
        aggregateOneDomainEventEntries = new ArrayList<>();
        aggregateTwoDomainEventEntries = new ArrayList<>();
        allDomainEventEntries = new ArrayList<>();

        aggregateOneDomainEventEntries.add(new DomainEventEntry(0L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.CreatedEvent", "2017-01-01T00:00:01.001Z", aggregateOneId, 0L, aggregateOneType));
        aggregateOneDomainEventEntries.add(new DomainEventEntry(1L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.002Z", aggregateOneId, 1L, aggregateOneType));
        aggregateOneDomainEventEntries.add(new DomainEventEntry(2L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.003Z", aggregateOneId, 2L, aggregateOneType));
        aggregateOneDomainEventEntries.add(new DomainEventEntry(3L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.004Z", aggregateOneId, 3L, aggregateOneType));

        aggregateTwoDomainEventEntries.add(new DomainEventEntry(4L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.CreatedEvent", "2017-01-01T00:00:01.001Z", aggregateTwoId, 0L, aggregateTwoType));
        aggregateTwoDomainEventEntries.add(new DomainEventEntry(5L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.002Z", aggregateTwoId, 1L, aggregateTwoType));
        aggregateTwoDomainEventEntries.add(new DomainEventEntry(6L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.003Z", aggregateTwoId, 2L, aggregateTwoType));
        aggregateTwoDomainEventEntries.add(new DomainEventEntry(7L, UUID.randomUUID().toString(), "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.004Z", aggregateTwoId, 3L, aggregateTwoType));

        allDomainEventEntries.addAll(aggregateOneDomainEventEntries);
        allDomainEventEntries.addAll(aggregateTwoDomainEventEntries);
        domainEventEntryRepository.save(allDomainEventEntries);
    }

    @Test
    public void when_getAll_expect_allEventsAsJsonAndStatusOk() throws Exception {
        String allEventsJson = json(eventConverter.convert(allDomainEventEntries));

        this.mockMvc.perform(get(baseUrl)
                .contentType(contentType)
                .accept(contentType))
                .andExpect(content().contentType(contentType))
                .andExpect(content().string(allEventsJson))
                .andExpect(status().isOk());
    }

    @Test
    public void when_getOne_expect_oneEventAsJsonAndStatusOk() throws Exception {
        String eventIdentifier = UUID.randomUUID().toString();
        DomainEventEntry eventEntry = new DomainEventEntry(8L, eventIdentifier, "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.004Z", aggregateTwoId, 4L, aggregateTwoType);
        EventDto eventDto = eventConverter.convert(eventEntry);
        aggregateTwoDomainEventEntries.add(eventEntry);
        domainEventEntryRepository.save(eventEntry);

        String eventJson = json(eventDto);

        this.mockMvc.perform(get(baseUrl + "/" + eventIdentifier)
                .contentType(contentType)
                .accept(contentType))
                .andExpect(content().contentType(contentType))
                .andExpect(content().string(eventJson))
                .andExpect(status().isOk());
    }

    @Test
    public void when_getEventsForAggregate_expect_eventsForOneAggregateAsJsonAndStatusOk() throws Exception {
        String eventJson = json(eventConverter.convert(aggregateOneDomainEventEntries));

        this.mockMvc.perform(get(baseUrl + "/aggregate/" + aggregateOneId)
                .contentType(contentType)
                .accept(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(content().string(eventJson))
                .andExpect(status().isOk());
    }

    @Test
    public void when_updateEvent_expect_updatedEventWithNewContentAsJsonAndStatusOk() throws Exception {
        String eventIdentifier = UUID.randomUUID().toString();
        DomainEventEntry eventEntry = new DomainEventEntry(8L, eventIdentifier, "sample".getBytes(), "sample".getBytes(), "1.0", "some.package.ModifiedEvent", "2017-01-01T00:00:01.004Z", aggregateTwoId, 4L, aggregateTwoType);
        domainEventEntryRepository.save(eventEntry);

        ModifiedEventDto modifiedEventDto = new ModifiedEventDto();
        modifiedEventDto.setPayload("new Payload");
        modifiedEventDto.setPayloadRevision("2.0");

        this.mockMvc.perform(post(baseUrl + "/" + eventIdentifier)
                .contentType(contentType)
                .content(json(modifiedEventDto))
                .accept(contentType))
                .andExpect(jsonPath("$.payload", is(modifiedEventDto.getPayload())))
                .andExpect(jsonPath("$.payloadRevision", is(modifiedEventDto.getPayloadRevision())))
                .andExpect(status().isAccepted());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}