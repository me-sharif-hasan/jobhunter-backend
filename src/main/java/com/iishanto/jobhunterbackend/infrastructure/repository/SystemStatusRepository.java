package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.infrastructure.database.SystemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemStatusRepository extends JpaRepository<SystemStatus, Integer> {
    SystemStatus findByName(SystemStatusNames name);
}
