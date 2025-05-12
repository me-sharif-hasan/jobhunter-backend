package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;

public interface GetSystemStatusUseCase {
    SimpleSystemStatus getJobIndexingStatus();
    default SimpleSystemStatus getSystemStatus(SystemStatusNames name){
        return switch (name) {
            case JOB_INDEXER -> getJobIndexingStatus();
        };
    }
    void updateJobIndexingStatus(SystemStatusValues systemStatus);
}
