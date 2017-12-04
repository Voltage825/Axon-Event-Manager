package com.val.axon.aem.model.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDto {

    private long globalIndex;
    private String eventIdentifier;
    private String metadata;
    private String payload;
    private String payloadRevision;
    private String payloadType;
    private Date date;
    private String aggregateIdentifier;
    private long sequenceNumber;
    private String type;
}
