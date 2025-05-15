export default class Job {
    jobId: string | undefined;
    title: string | undefined;
    company: string | undefined;
    companyWebsite: string | undefined;
    jobUrl: string | undefined;
    location: string | undefined;
    salary: string | undefined;
    jobType: string | undefined;
    jobCategory: string | undefined;
    jobDescription: string | undefined;
    jobPostedDate: string | undefined;
    jobLastDate: string | undefined;
    jobApplyLink: string | undefined;
    jobApplyEmail: string | undefined;
    jobParsedAt: Date | undefined;
    jobUpdatedAt: Date | undefined;
    skillsNeeded: string | undefined;
    experienceNeeded: string | undefined;
    isApproved: boolean | undefined;
    isDuplicate: boolean | undefined;
}
