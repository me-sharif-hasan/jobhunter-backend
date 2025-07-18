package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSystemStatusAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;
import com.iishanto.jobhunterbackend.infrastructure.database.SystemDataStore;
import com.iishanto.jobhunterbackend.infrastructure.repository.SystemStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class SystemStatusDataPort implements AdminSystemStatusAdapter {
    private SystemStatusRepository systemStatusRepository;
    @Override
    public SimpleSystemStatus getJobIndexingStatus() {
        SystemDataStore systemDataStore =systemStatusRepository.findByName(SystemStatusNames.JOB_INDEXER);
        if(systemDataStore ==null){
            return null;
        }
        return systemDataStore.toSimpleSystemStatus();
    }

    @Override
    public void updateJobStatus(SystemStatusNames systemStatusNames, SystemStatusValues systemStatus) {
        SystemDataStore dbSystemDataStore = new SystemDataStore();
        dbSystemDataStore.setName(systemStatusNames);
        dbSystemDataStore.setValue(systemStatus);
        systemStatusRepository.save(dbSystemDataStore);
    }
}
