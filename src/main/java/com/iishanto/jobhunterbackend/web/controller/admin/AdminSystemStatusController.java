package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSystemStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusNames;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetSystemStatusUseCase;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/system-status")
@AllArgsConstructor
public class AdminSystemStatusController {
    GetSystemStatusUseCase getSystemStatusUseCase;
    @GetMapping
    public ApiResponse getJobIndexingStatus(
            @RequestParam SystemStatusNames name
    ){
        return new ApiResponse(
                true,
                getSystemStatusUseCase.getSystemStatus(name),
                "System status fetched successfully"
        );
    }
}
