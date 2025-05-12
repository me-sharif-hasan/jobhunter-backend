package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSystemStatusAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;
import com.iishanto.jobhunterbackend.infrastructure.database.SystemStatus;
import com.iishanto.jobhunterbackend.infrastructure.repository.SystemStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class SystemStatusDataPort implements AdminSystemStatusAdapter {
    private SystemStatusRepository systemStatusRepository;
    @Override
    public SimpleSystemStatus getJobIndexingStatus() {
        SystemStatus systemStatus=systemStatusRepository.findByName(SystemStatusNames.JOB_INDEXER);
        if(systemStatus==null){
            return null;
        }
        return systemStatus.toSimpleSystemStatus();
    }

    @Override
    public void updateJobStatus(SystemStatusNames systemStatusNames, SystemStatusValues systemStatus) {
        SystemStatus dbSystemStatus = new SystemStatus();
        dbSystemStatus.setName(systemStatusNames);
        dbSystemStatus.setValue(systemStatus);
        systemStatusRepository.save(dbSystemStatus);
    }
}
