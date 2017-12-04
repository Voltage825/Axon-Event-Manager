package com.val.axon.aem.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DOMAIN_EVENT_ENTRY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventEntry {

    @Id
    @Column(name = "GLOBAL_INDEX")
    private long globalIndex;

    @Column(name = "EVENT_IDENTIFIER")
    private String eventIdentifier;

    @Lob
    @Column(name = "META_DATA")
    private byte[] metadata;

    @Lob
    @Column(name = "PAYLOAD")
    private byte[] payload;

    @Column(name = "PAYLOAD_REVISION")
    private String payloadRevision;

    @Column(name = "PAYLOAD_TYPE")
    private String payloadType;

    @Column(name = "TIME_STAMP")
    private String date;

    @Column(name = "AGGREGATE_IDENTIFIER")
    private String aggregateIdentifier;

    @Column(name = "SEQUENCE_NUMBER")
    private long sequenceNumber;

    @Column(name = "TYPE")
    private String type;
}
