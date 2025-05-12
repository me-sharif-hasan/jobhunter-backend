package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSystemStatusAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetSystemStatusUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemStatusService implements GetSystemStatusUseCase {
    private final AdminSystemStatusAdapter adminSystemStatusAdapter;
    @Override
    public SimpleSystemStatus getJobIndexingStatus() {
        SimpleSystemStatus systemStatus = adminSystemStatusAdapter.getJobIndexingStatus();
        if (systemStatus == null) {
            return new SimpleSystemStatus(SystemStatusNames.JOB_INDEXER, SystemStatusValues.JOB_INDEXER_IDLE);
        }
        return systemStatus;
    }

    @Override
    public void updateJobIndexingStatus(SystemStatusValues systemStatus) {
        adminSystemStatusAdapter.updateJobStatus(SystemStatusNames.JOB_INDEXER,systemStatus);
    }
}
