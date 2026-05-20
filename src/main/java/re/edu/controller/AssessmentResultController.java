package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.assessmentresultreq.AssessmentResultRequest;
import re.edu.model.dto.request.assessmentresultreq.AssessmentResultUpdateRequest;
import re.edu.service.AssessmentResultService;

@RestController
@RequestMapping("/api/assessment_results")
@RequiredArgsConstructor
public class AssessmentResultController {

    private final AssessmentResultService
            assessmentResultService;

    // 42. GET
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<?> getAllResults(
            @RequestParam(required = false)
            Integer assignmentId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assessmentResultService
                                .getAllResults(
                                        assignmentId
                                ),
                        null,
                        200,
                        "Get assessment results successfully"
                )
        );
    }

    // 43. CREATE
    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> createResult(
            @Valid
            @RequestBody
            AssessmentResultRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                assessmentResultService
                                        .createResult(
                                                request
                                        ),
                                null,
                                201,
                                "Create assessment result successfully"
                        )
                );
    }

    // 44. UPDATE
    @PutMapping("/{resultId}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> updateResult(
            @PathVariable Integer resultId,
            @Valid
            @RequestBody
            AssessmentResultUpdateRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assessmentResultService
                                .updateResult(
                                        resultId,
                                        request
                                ),
                        null,
                        200,
                        "Update assessment result successfully"
                )
        );
    }
}