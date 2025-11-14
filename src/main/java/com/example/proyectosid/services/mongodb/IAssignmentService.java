// services/mongodb/IAssignmentService.java
package com.example.proyectosid.services.mongodb;

import com.example.proyectosid.model.mongodb.Assignment;
import java.time.LocalDateTime;
import java.util.List;

public interface IAssignmentService {
    Assignment assignTrainerToUser(String userId, String trainerId);
    Assignment getActiveAssignment(String userId);
    List<Assignment> getTrainerUsers(String trainerId);
    Assignment deactivateAssignment(String userId);
    Long countActiveUsersByTrainer(String trainerId);
    Long countAssignmentsByTrainerInPeriod(String trainerId, LocalDateTime start, LocalDateTime end);

}
