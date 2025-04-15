package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.usecase.IndexJobs;
import com.iishanto.jobhunterbackend.scheduled.ScheduledJobIndexRefresher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/job")
@AllArgsConstructor
public class AdminJobController {
    ScheduledJobIndexRefresher refreshJobUseCase;
    @GetMapping("/refresh")
    public void refreshJobs(){
        refreshJobUseCase.refreshJobIndex();
        System.out.println("Jobs Refreshed");
    }
}
