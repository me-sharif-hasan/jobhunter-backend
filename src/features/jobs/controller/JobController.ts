import {inject, injectable} from "inversify";
import JobDatasource from "../datasource/JobDatasource.ts";
import Job from "../../common/types/Job.ts";
import SystemStatusDatasource from "../../common/datasource/SystemStatusDatasource.ts";
import {SystemStatusName} from "../../common/types/SystemStatus.ts";

@injectable()
export default class JobController{
    constructor(
        @inject(JobDatasource) protected jobDatasource: JobDatasource,
        @inject(SystemStatusDatasource) protected systemStatusDatasource: SystemStatusDatasource,
    ) {
    }
    async getAllJobs(page: number, size: number,query:string|undefined):Promise<{ jobs: Job[]; total: number }>{
        return this.jobDatasource.getAllJobsAsAdmin(page,size,query??"");
    }

    async getJobIndexingStatus(){
        return this.systemStatusDatasource.getSystemStatus(SystemStatusName.JOB_INDEXER);
    }
}