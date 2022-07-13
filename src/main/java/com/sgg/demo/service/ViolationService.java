package com.sgg.demo.service;

import com.sgg.demo.dto.Violation;
import com.sgg.demo.dto.ViolationSummary;

import java.util.Collection;
import java.util.UUID;

public interface ViolationService {

    Violation create(UUID eventId, Double fine);
    Violation get(UUID id);
    Violation update(Violation violation);
    ViolationSummary getViolationSummary();
    Collection<Violation> getAll();

    void removeAll();
}
