package com.val.axon.aem.persistance.repository;

import java.util.List;
import java.util.Optional;

import com.val.axon.aem.persistance.entity.DomainEventEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainEventEntryRepository extends CrudRepository<DomainEventEntry, Long> {

    List<DomainEventEntry> findAll();

    Optional<DomainEventEntry> findByEventIdentifier(String eventIdentifier);

    List<DomainEventEntry> findAllByAggregateIdentifier(String aggregateIdentifier);

}
