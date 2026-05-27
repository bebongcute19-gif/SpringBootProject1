package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.mentorReq.MentorRequest;
import re.edu.model.dto.request.mentorReq.UpdateMentorRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.MentorService;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    // 14. Get all mentors
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse>
    getAllMentors() {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        mentorService.getAllMentors(),
                        null,
                        200,
                        "Lấy danh sách giảng viên hướng dẫn thành công"
                )
        );
    }

    // 15. Get mentor by id
    @GetMapping("/{mentorId}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR','STUDENT')")
    public ResponseEntity<ApiResponse>
    getMentorById(
            @PathVariable Integer mentorId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        mentorService.getMentorById(mentorId),
                        null,
                        200,
                        "Lấy thông tin giảng viên hướng dẫn thành công"
                )
        );
    }

    // 16. Create mentor
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createMentor(
            @Valid
            @RequestBody
            MentorRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                mentorService.createMentor(request),
                                null,
                                201,
                                "Tạo giảng viên hướng dẫn thành công"
                        )
                );
    }

    // 17. Update mentor
    @PutMapping("/{mentorId}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public ResponseEntity<ApiResponse> updateMentor(
            @PathVariable Integer mentorId,

            @Valid
            @RequestBody
            UpdateMentorRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        mentorService.updateMentor(
                                mentorId,
                                request
                        ),
                        null,
                        200,
                        "Cập nhật giảng viên hướng dẫn thành công"
                )
        );
    }
}