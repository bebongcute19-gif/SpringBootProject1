package re.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.internshipassignmentreq.InternshipAssignmentRequest;
import re.edu.model.dto.request.internshipassignmentreq.UpdateAssignmentStatusRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.InternshipAssignmentService;

@RestController
@RequestMapping("/api/internship_assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService
            assignmentService;

    // 38. GET ALL
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getAllAssignments() {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assignmentService
                                .getAllAssignments(),
                        null,
                        200,
                        "Lấy danh sách phân công thực tập thành công"
                )
        );
    }

    // 39. GET BY ID
    @GetMapping("/{assignmentId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getAssignmentById(
            @PathVariable Integer assignmentId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assignmentService
                                .getAssignmentById(
                                        assignmentId
                                ),
                        null,
                        200,
                        "Lấy thông tin phân công thực tập thành công"
                )
        );
    }

    // 40. CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createAssignment(
            @RequestBody
            InternshipAssignmentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                assignmentService
                                        .createAssignment(
                                                request
                                        ),
                                null,
                                201,
                                "Tạo phân công thực tập thành công"
                        )
                );
    }

    // 41. UPDATE STATUS
    @PutMapping("/{assignmentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    updateAssignmentStatus(
            @PathVariable Integer assignmentId,
            @RequestBody
            UpdateAssignmentStatusRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        assignmentService
                                .updateAssignmentStatus(
                                        assignmentId,
                                        request
                                ),
                        null,
                        200,
                        "Cập nhật trạng thái phân công thực tập thành công"
                )
        );
    }
}