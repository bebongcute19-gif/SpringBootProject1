package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.studentReq.StudentRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.StudentService;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // 10. Get all students
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public ResponseEntity<ApiResponse>
    getAllStudents() {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        studentService.getAllStudents(),
                        null,
                        200,
                        "Get student list successfully"
                )
        );
    }

    // 11. Get student by id
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR','STUDENT')")
    public ResponseEntity<ApiResponse>
    getStudentById(
            @PathVariable Integer studentId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        studentService.getStudentById(studentId),
                        null,
                        200,
                        "Get student successfully"
                )
        );
    }

    // 12. Create student
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createStudent(
            @Valid
            @RequestBody
            StudentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                studentService
                                        .createStudent(request),
                                null,
                                201,
                                "Create student successfully"
                        )
                );
    }

    // 13. Update student
    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse>
    updateStudent(
            @PathVariable Integer studentId,
            @Valid
            @RequestBody
            StudentRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        studentService.updateStudent(
                                studentId,
                                request
                        ),
                        null,
                        200,
                        "Update student successfully"
                )
        );
    }
}