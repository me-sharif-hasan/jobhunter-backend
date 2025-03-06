package com.iishanto.jobhunterbackend.web.dto.request;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubscriptionDto {
    @NotNull
    private Long site_id;
    public SimpleSubscriptionModel toSimpleSubscriptionModel(){
        SimpleSubscriptionModel simpleSubscriptionModel=new SimpleSubscriptionModel();
        simpleSubscriptionModel.setSite(
                SimpleSiteModel.builder()
                        .id(site_id)
                        .build()

        );
        return simpleSubscriptionModel;
    }
}
