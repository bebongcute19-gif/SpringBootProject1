package re.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.roundcriteriareq.RoundCriteriaRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.RoundCriteriaService;

@RestController
@RequestMapping("/api/round_criteria")
@RequiredArgsConstructor
public class RoundCriteriaController {

    private final RoundCriteriaService
            roundCriteriaService;

    // 33. GET ALL
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getAllRoundCriteria(
            @RequestParam(required = false)
            Integer roundId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        roundCriteriaService
                                .getAllRoundCriteria(roundId),
                        null,
                        200,
                        "Get round criteria successfully"
                )
        );
    }

    // 34. GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getRoundCriteriaById(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        roundCriteriaService
                                .getRoundCriteriaById(id),
                        null,
                        200,
                        "Get round criteria successfully"
                )
        );
    }

    // 35. CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createRoundCriteria(
            @RequestBody
            RoundCriteriaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                roundCriteriaService
                                        .createRoundCriteria(
                                                request
                                        ),
                                null,
                                201,
                                "Create round criteria successfully"
                        )
                );
    }

    // 36. UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    updateRoundCriteria(
            @PathVariable Integer id,
            @RequestBody
            RoundCriteriaRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        roundCriteriaService
                                .updateRoundCriteria(
                                        id,
                                        request
                                ),
                        null,
                        200,
                        "Update round criteria successfully"
                )
        );
    }

    // 37. DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    deleteRoundCriteria(
            @PathVariable Integer id
    ) {

        roundCriteriaService
                .deleteRoundCriteria(id);

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        null,
                        null,
                        200,
                        "Delete round criteria successfully"
                )
        );
    }
}