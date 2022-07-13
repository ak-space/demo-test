package com.sgg.demo.service.impl;

import com.sgg.demo.dto.Violation;
import com.sgg.demo.dto.ViolationSummary;
import com.sgg.demo.service.ViolationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ViolationServiceImpl implements ViolationService {

    private final ConcurrentHashMap<UUID, Violation> violations;
    private static final Logger LOGGER = LoggerFactory.getLogger(ViolationServiceImpl.class);
    private static final Integer REDUCE_PARALLELISM_THRESHOLD = 4;

    public ViolationServiceImpl() {
        this(new ConcurrentHashMap<>());
    }

    public ViolationServiceImpl(ConcurrentHashMap<UUID, Violation> violations) {
        this.violations = violations;
    }

    @Override
    public Violation create(UUID eventId, Double fine) {
        Violation violation = Violation.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .fine(fine)
                .paid(false)
                .build();
        violations.put(violation.getId(), violation);
        LOGGER.trace("Violation created id:{} eventId:{} fine:{}",violation.getId(),violation.getEventId(),violation.getFine());
        return violation;
    }

    @Override
    public ViolationSummary getViolationSummary() {
        LOGGER.trace("Get violation summary");

        int paid = violations.reduceValuesToInt(REDUCE_PARALLELISM_THRESHOLD,
                v -> Objects.equals(Boolean.TRUE, v.getPaid()) ? 1 : 0, 0, Integer::sum);
        int size = violations.size();
        return new ViolationSummary(paid, size - paid, size);

    }

    @Override
    public Violation get(UUID id) {
        LOGGER.trace("Get violation by id:{}", id);
        return violations.get(id);
    }

    @Override
    public Violation update(Violation violation) {
        violations.computeIfPresent(violation.getId(), (k, v) -> violation);
        LOGGER.trace("Violation updated violation id:{} ", violation.getId());
        return violation;
    }

    @Override
    public Collection<Violation> getAll() {
        LOGGER.trace("Violation getAll");
        return violations.values();
    }

    @Override
    public void removeAll() {
        LOGGER.trace("Violation removeAll");
        violations.clear();
    }


}
