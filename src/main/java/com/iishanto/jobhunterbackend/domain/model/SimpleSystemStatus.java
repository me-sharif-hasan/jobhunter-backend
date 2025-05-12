package com.iishanto.jobhunterbackend.domain.model;

import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleSystemStatus {
    SystemStatusNames name;
    SystemStatusValues value;
}