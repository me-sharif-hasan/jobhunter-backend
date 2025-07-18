package com.iishanto.jobhunterbackend.infrastructure.tasks.duplicateutil;

import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;

public interface DuplicateJobMarker {
    void findDuplicates();
    void markDuplicates();
    void findAndMarkDuplicates();
    void isDuplicate(Opportunity opportunity);
}
