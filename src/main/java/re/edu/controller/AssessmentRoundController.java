package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.assessmentRoundReq.AssessmentRoundRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.AssessmentRoundService;

@RestController
@RequestMapping("/api/assessment_rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService
            assessmentRoundService;

    //28 GET ALL
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getAllRounds(
            @RequestParam(required = false)
            Integer phaseId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assessmentRoundService
                                .getAllRounds(phaseId),
                        null,
                        200,
                        "Lấy danh sách đợt đánh giá thành công"
                )
        );
    }

    //29 GET BY ID
    @GetMapping("/{roundId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getRoundById(
            @PathVariable Integer roundId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assessmentRoundService
                                .getRoundById(roundId),
                        null,
                        200,
                        "Lấy thông tin đợt đánh giá thành công"
                )
        );
    }

    //30 CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createRound(
            @Valid
            @RequestBody
            AssessmentRoundRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                assessmentRoundService
                                        .createRound(request),
                                null,
                                201,
                                "Tạo đợt đánh giá thành công"
                        )
                );
    }

    //31 UPDATE
    @PutMapping("/{roundId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    updateRound(
            @PathVariable Integer roundId,
            @RequestBody
            AssessmentRoundRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assessmentRoundService
                                .updateRound(
                                        roundId,
                                        request
                                ),
                        null,
                        200,
                        "Cập nhật đợt đánh giá thành công"
                )
        );
    }

    //32 DELETE
    @DeleteMapping("/{roundId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    deleteRound(
            @PathVariable Integer roundId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assessmentRoundService
                                .deleteRound(roundId),
                        null,
                        200,
                        "Xóa đợt đánh giá thành công"
                )
        );
    }
}