package com.example.web.enrollment;

import java.util.UUID;

public record EnrollResponse(UUID id, UUID studentId, UUID courseId) {
}
