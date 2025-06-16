package com.iishanto.jobhunterbackend.web.dto.request;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JobUpdateDto {
    @NotBlank
    public String title;
    public String jobUrl;
    public String location;
    public String salary;
    public String jobType;
    public String jobCategory;
    public String jobDescription;
    public String jobPostedDate;
    public String jobLastDate;
    public String jobApplyLink;
    public String jobApplyEmail;

    public SimpleJobModel toSimpleJobModel(){
        SimpleJobModel simpleJobModel=new SimpleJobModel();
        simpleJobModel.setTitle(title);
        simpleJobModel.setJobUrl(jobUrl);
        simpleJobModel.setLocation(location);
        simpleJobModel.setSalary(salary);
        simpleJobModel.setJobType(jobType);
        simpleJobModel.setJobCategory(jobCategory);
        simpleJobModel.setJobDescription(jobDescription);
        simpleJobModel.setJobPostedDate(jobPostedDate);
        simpleJobModel.setJobLastDate(jobLastDate);
        simpleJobModel.setJobApplyLink(jobApplyLink);
        simpleJobModel.setJobApplyEmail(jobApplyEmail);
        return simpleJobModel;
    }
}
