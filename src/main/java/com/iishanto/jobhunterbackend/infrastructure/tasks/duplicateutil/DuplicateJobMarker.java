package com.iishanto.jobhunterbackend.infrastructure.tasks.duplicateutil;

public interface DuplicateJobMarker {
    void findDuplicates();
    void markDuplicates();
    void findAndMarkDuplicates();
}
