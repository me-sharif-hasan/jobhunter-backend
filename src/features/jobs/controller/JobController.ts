import {inject, injectable} from "inversify";
import JobDatasource from "../datasource/JobDatasource.ts";
import Job from "../../common/types/Job.ts";

@injectable()
export default class JobController{
    constructor(
        @inject(JobDatasource) protected jobDatasource: JobDatasource,
    ) {
    }
    async getAllJobs(page: number, size: number,query:string|undefined):Promise<{ jobs: Job[]; total: number }>{
        return this.jobDatasource.getAllJobsAsAdmin(page,size,query??"");
    }
}