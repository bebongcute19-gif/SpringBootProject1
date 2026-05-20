package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.internshipPhaseReq.InternshipPhaseRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.InternshipPhaseService;

@RestController
@RequestMapping("/api/internship_phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

    private final InternshipPhaseService
            internshipPhaseService;

    // 18. Get all phases
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getAllPhases() {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        internshipPhaseService
                                .getAllPhases(),
                        null,
                        200,
                        "Get internship phases successfully"
                )
        );
    }

    // 19. Get phase by id
    @GetMapping("/{phaseId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getPhaseById(
            @PathVariable Integer phaseId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        internshipPhaseService
                                .getPhaseById(phaseId),
                        null,
                        200,
                        "Get internship phase successfully"
                )
        );
    }

    // 20. Create phase
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createPhase(
            @Valid
            @RequestBody
            InternshipPhaseRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                internshipPhaseService
                                        .createPhase(request),
                                null,
                                201,
                                "Create internship phase successfully"
                        )
                );
    }

    // 21. Update phase
    @PutMapping("/{phaseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    updatePhase(
            @PathVariable Integer phaseId,
            @Valid
            @RequestBody
            InternshipPhaseRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        internshipPhaseService
                                .updatePhase(
                                        phaseId,
                                        request
                                ),
                        null,
                        200,
                        "Update internship phase successfully"
                )
        );
    }

    // 22. Delete phase
    @DeleteMapping("/{phaseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    deletePhase(
            @PathVariable Integer phaseId
    ) {

        internshipPhaseService.deletePhase(phaseId);

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        null,
                        null,
                        200,
                        "Delete internship phase successfully"
                )
        );
    }
}