package com.sgg.demo.controller;

import com.sgg.demo.dto.Violation;
import com.sgg.demo.dto.ViolationSummary;
import com.sgg.demo.service.ViolationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/violations")
public class ViolationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViolationController.class);
    private final ViolationService violationService;

    @Autowired
    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Violation> getViolation(@PathVariable UUID id) {
        LOGGER.trace("Violation API get Violation - id:{}", id);
        return Optional
                .ofNullable(violationService.get(id))
                .map(violation -> ResponseEntity.ok().body(violation))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Violation> payViolation(@PathVariable UUID id) {

        LOGGER.trace("Violation API update Violations id:{}", id);
        return Optional.ofNullable(violationService.get(id))
                .map(violation -> {
                    violation.setPaid(true);
                    return ResponseEntity.ok().body(violationService.update(violation));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Violation>> getViolation() {
        LOGGER.trace("Violation API get all");
        return ResponseEntity.ok().body(violationService.getAll());
    }


    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViolationSummary> getViolationSummary() {
        LOGGER.trace("Violation API get Violations Summary");
        return ResponseEntity.ok().body(violationService.getViolationSummary());
    }

}
