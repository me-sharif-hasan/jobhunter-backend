package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SystemDataStore {
    @Id
    @Column(unique = true, nullable = false)
    private SystemStatusNames name;
    private SystemStatusValues value;

    public SimpleSystemStatus toSimpleSystemStatus(){
        return new SimpleSystemStatus(
                name,value
        );
    }

    public static SystemDataStore fromSimpleSystemStatus(SimpleSystemStatus s){
        SystemDataStore systemDataStore = new SystemDataStore();
        systemDataStore.setName(s.getName());
        systemDataStore.setValue(s.getValue());
        return systemDataStore;
    }
}
