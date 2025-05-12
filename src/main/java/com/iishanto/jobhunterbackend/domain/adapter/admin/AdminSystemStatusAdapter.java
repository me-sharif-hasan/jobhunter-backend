package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;

public interface AdminSystemStatusAdapter {
    SimpleSystemStatus getJobIndexingStatus();

    void updateJobStatus(SystemStatusNames systemStatusNames, SystemStatusValues systemStatus);
}
