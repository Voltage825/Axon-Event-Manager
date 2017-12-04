package com.val.axon.aem.model.dto;

import lombok.Data;

@Data
public class ModifiedEventDto {

    private String payload;
    private String payloadRevision;
}
