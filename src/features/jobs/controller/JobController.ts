import {inject, injectable} from "inversify";
import JobDatasource from "../datasource/JobDatasource.ts";
import Job from "../../common/types/Job.ts";
import SystemStatusDatasource from "../../common/datasource/SystemStatusDatasource.ts";
import {SystemStatusName, SystemStatusNameValue} from "../../common/types/SystemStatus.ts";

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

    async runIndexer(statusUpdateCallback: ((status: SystemStatusNameValue | undefined) => void)){
        this.jobDatasource.runIndexer().then(()=>{
            console.log("Indexer started successfully");
            statusUpdateCallback(SystemStatusNameValue.JOB_INDEXING);
            let intervalId=setInterval(()=>{
            this.systemStatusDatasource.getSystemStatus(SystemStatusName.JOB_INDEXER).then((status)=>{
                console.log("Indexer status",status,intervalId);
                statusUpdateCallback(status?.value);
                if(status?.value===SystemStatusNameValue.JOB_INDEXER_IDLE){
                    clearInterval(intervalId);
                }
            });
            },10000);
        }).catch((err:any)=>{
            console.error("Error running indexer",err);
            alert("Error running indexer");
        });
    }
    
    approveJob(jobId: number | undefined) {
        throw new Error(`Method not implemented. ${jobId}`);
    }
}