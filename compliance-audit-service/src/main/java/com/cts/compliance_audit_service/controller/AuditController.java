package com.cts.compliance_audit_service.controller;

import com.cts.compliance_audit_service.entity.Audit;
import com.cts.compliance_audit_service.enums.AuditScope;
import com.cts.compliance_audit_service.enums.AuditStatus;
import com.cts.compliance_audit_service.projection.AuditByIdProjection;
import com.cts.compliance_audit_service.service.IAuditService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/audit")
public class AuditController {

    private final IAuditService auditService;

    @Autowired
    public AuditController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/createAudit")
    public ResponseEntity<String> createAudit(@Valid @RequestBody Audit audit) {
        log.info("Request received to create audit");
        auditService.createAudit(audit);
        log.info("Audit created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body("Audit created successfully");
    }

    @PutMapping("/updateAudit/{auditId}")
    public ResponseEntity<Audit> updateAudit(@PathVariable Long auditId,@Valid @RequestBody Audit updatedAudit) {
        log.info("Updating audit with id {}", auditId);
        Audit audit = auditService.updateAudit(auditId, updatedAudit);
        log.info("Audit {} updated successfully", auditId);
        return ResponseEntity.ok(audit);
    }

    @GetMapping("/getAllAudits")
    public ResponseEntity<List<Audit>> getAllAudits() {
        log.info("Fetching all audits");
        return ResponseEntity.ok(auditService.getAllAudits());
    }

    @GetMapping("/getAuditById/{auditId}")
    public ResponseEntity<AuditByIdProjection> getAuditById(@PathVariable Long auditId) {
        log.info("Fetching audit with id {}", auditId);
        return auditService.getAuditById(auditId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByAuditStatus/{auditStatus}")
    public ResponseEntity<List<Audit>> findByAuditStatus(@PathVariable AuditStatus auditStatus) {
        log.info("Finding audits with status {}", auditStatus);
        return ResponseEntity.ok(auditService.findByAuditStatus(auditStatus));
    }

    @GetMapping("/findByAuditScope/{auditScope}")
    public ResponseEntity<List<Audit>> findByAuditScope(@PathVariable AuditScope auditScope) {
        log.info("Finding audits with scope {}", auditScope);
        return ResponseEntity.ok(auditService.findByAuditScope(auditScope));
    }

    @GetMapping("/findAuditByOfficer_UserId/{userId}")
    public ResponseEntity<List<Audit>> findAuditByOfficer_UserId(@PathVariable Long userId) {
        log.info("Finding audits for officer userId {}", userId);
        return ResponseEntity.ok(auditService.findAuditByOfficerId(userId));
    }

    @DeleteMapping("/deleteAudit/{auditId}")
    public ResponseEntity<String> deleteAudit(@PathVariable Long auditId) {
        log.warn("Deleting audit with id {}", auditId);
        auditService.deleteAudit(auditId);
        log.info("Audit {} deleted", auditId);
        return ResponseEntity.ok("Audit deleted successfully");
    }
}
