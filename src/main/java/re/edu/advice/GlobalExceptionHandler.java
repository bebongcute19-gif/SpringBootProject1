package re.edu.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re.edu.exception.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.response.authRes.ApiResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= METHOD NOT ALLOWED =================
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi phương thức", "Phương thức HTTP không được hỗ trợ");

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 405, "Phương thức không được hỗ trợ"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // ================= JWT =================
    @ExceptionHandler(JwtExceptionCustom.class)
    public ResponseEntity<?> handleJwtException(JwtExceptionCustom e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi token", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 401, "Xác thực token thất bại"), HttpStatus.UNAUTHORIZED);
    }

    // ================= LOGIN =================
    @ExceptionHandler(BadCredentialsExceptionCustom.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsExceptionCustom e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi đăng nhập", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 401, "Đăng nhập thất bại"), HttpStatus.UNAUTHORIZED);
    }

    // ================= ACCESS DENIED =================
    @ExceptionHandler(AccessDeniedExceptionCustom.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedExceptionCustom e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi phân quyền", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 403, "Bạn không có quyền truy cập"), HttpStatus.FORBIDDEN);
    }

    // ================= VALIDATION =================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {

        Map<String, Object> errors = new HashMap<>();

        e.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, errors, 400, "Dữ liệu gửi lên không hợp lệ"), HttpStatus.BAD_REQUEST);
    }

    // ================= DUPLICATE =================
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateResourceException e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi trùng lặp", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 409, "Dữ liệu đã tồn tại"), HttpStatus.CONFLICT);
    }

    // ================= INVALID JSON =================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e
    ) {

        Map<String, Object> res =
                new HashMap<>();

        String message =
                e.getMostSpecificCause()
                        .getMessage();

        System.out.println(message);

        // ===== LOCAL DATE FORMAT =====
        if (
                message.contains("could not be parsed")
                        ||
                        message.contains("DateTimeParseException")
                        ||
                        message.contains("LocalDate")
        ){

            res.put(
                    "Lỗi dữ liệu",
                    "Ngày phải có định dạng yyyy-MM-dd"
            );
        }

        // ===== BOOLEAN =====
        else if (
                message.contains("Boolean")
        ) {

            res.put(
                    "Lỗi dữ liệu",
                    "Giá trị phải là true hoặc false"
            );
        }

        // ===== ROLE ENUM =====
        else if (
                message.contains("Role")
        ) {

            res.put(
                    "Lỗi dữ liệu",
                    "Vai trò không hợp lệ"
            );
        }

        // ===== ASSIGNMENT STATUS ENUM =====
        else if (
                message.contains("AssignmentStatus")
        ) {

            res.put(
                    "Lỗi dữ liệu",
                    "Trạng thái phân công không hợp lệ"
            );
        }

        // ===== UNKNOWN FIELD =====
        else if (
                message.contains("Unrecognized field")
        ) {

            String fieldName = "field";

            if (
                    message.contains("\"assignmentId\"")
            ) {

                fieldName = "assignmentId";
            }

            else if (
                    message.contains("\"roundId\"")
            ) {

                fieldName = "roundId";
            }

            else if (
                    message.contains("\"criterionId\"")
            ) {

                fieldName = "criterionId";
            }

            else if (
                    message.contains("\"phaseId\"")
            ) {

                fieldName = "phaseId";
            }

            else if (
                    message.contains("\"mentorId\"")
            ) {

                fieldName = "mentorId";
            }

            else if (
                    message.contains("\"studentId\"")
            ) {

                fieldName = "studentId";
            }

            res.put(
                    "Lỗi dữ liệu",
                    fieldName
                            + " không được phép xuất hiện trong request"
            );
        }

        // ===== NUMBER FORMAT =====
        else if (
                message.contains("Integer")
                        ||
                        message.contains("Long")
                        ||
                        message.contains("Double")
        ) {

            res.put(
                    "Lỗi dữ liệu",
                    "Kiểu dữ liệu số không hợp lệ"
            );
        }

        // ===== DEFAULT =====
        else {

            res.put(
                    "Lỗi dữ liệu",
                    "JSON gửi lên không hợp lệ"
            );
        }

        return new ResponseEntity<>(
                MapToAPIResponse.mapTo(
                        null,
                        res,
                        400,
                        "Dữ liệu không hợp lệ"
                ),
                HttpStatus.BAD_REQUEST
        );
    }// ================= ILLEGAL ARGUMENT =================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Lỗi dữ liệu", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 400, "Dữ liệu không hợp lệ"), HttpStatus.BAD_REQUEST);
    }

    // ================= NOT FOUND =================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException e) {

        Map<String, Object> res = new HashMap<>();

        res.put("Không tìm thấy", e.getMessage());

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 404, "Không tìm thấy dữ liệu"), HttpStatus.NOT_FOUND);
    }

    // ================= DATABASE CONSTRAINT =================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        Map<String, Object> res = new HashMap<>();

        String rootMessage = ex.getMostSpecificCause().getMessage();

        String message = "Vi phạm ràng buộc dữ liệu";

        // ===== INTERNSHIP PHASE DUPLICATE =====
        if (rootMessage.contains("internship_phase_phase_name_key")) {

            message = "Tên giai đoạn thực tập đã tồn tại";
        }

        // ===== INTERNSHIP PHASE FOREIGN KEY =====
        else if (rootMessage.contains("fk_assignment_phase")) {

            message = "Không thể xóa vì giai đoạn đang được sử dụng";
        }

        // ===== PHASE SEQUENCE =====
        else if (rootMessage.contains("internship_phase_pkey")) {

            message = "Sequence phase_id không đồng bộ với database";
        }

        // ===== EVALUATION CRITERIA DUPLICATE =====
        else if (rootMessage.contains("evaluation_criteria_criterion_name_key")) {

            message = "Tên tiêu chí đánh giá đã tồn tại";
        }

        // ===== EVALUATION CRITERIA SEQUENCE =====
        else if (rootMessage.contains("evaluation_criteria_pkey")) {

            message = "Sequence criterion_id không đồng bộ với database";
        }

        // ===== ASSESSMENT ROUND SEQUENCE =====
        else if (rootMessage.contains("assessment_rounds_pkey")) {

            message = "Sequence assessment_round_id không đồng bộ với database";
        }

        // ===== ROUND CRITERIA DUPLICATE =====
        else if (rootMessage.contains("round_criteria_round_id_criterion_id_key")) {

            message = "Tiêu chí đã tồn tại trong đợt đánh giá";
        }

        // ===== ROUND CRITERIA SEQUENCE =====
        else if (rootMessage.contains("round_criteria_pkey")) {

            message = "Sequence round_criterion_id không đồng bộ với database";
        }

        // ===== INTERNSHIP ASSIGNMENT DUPLICATE =====
        else if (rootMessage.contains("internship_assignments_student_id_phase_id_key")) {

            message = "Sinh viên đã được phân công trong giai đoạn này";
        }

        // ===== INTERNSHIP ASSIGNMENT SEQUENCE =====
        else if (rootMessage.contains("internship_assignments_pkey")) {

            message = "Sequence assignment_id không đồng bộ với database";
        }

        // ===== NOT NULL =====
        else if (rootMessage.contains("null value")) {

            message = "Không được để trống dữ liệu bắt buộc";
        }

        res.put("Lỗi dữ liệu", message);

        System.out.println("DB ERROR: " + rootMessage);

        return new ResponseEntity<>(MapToAPIResponse.mapTo(null, res, 409, "Lỗi ràng buộc dữ liệu"), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse>
    handleIllegalState(
            IllegalStateException e
    ) {

        Map<String, String> errors =
                new HashMap<>();

        errors.put(
                "Lỗi dữ liệu",
                e.getMessage()
        );

        return new ResponseEntity<>(
                MapToAPIResponse.mapTo(
                        null,
                        errors,
                        409,
                        "Lỗi ràng buộc dữ liệu"
                ),
                HttpStatus.CONFLICT
        );
    }
}