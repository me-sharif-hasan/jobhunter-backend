import {injectable} from "inversify";
import api from "../../../utility/HttpClient.ts";
import {AxiosInstance} from "axios";
import ApiResponse from "../../common/types/ApiResponse.ts";
import Constants from "../../../values/constants.ts";
import Job from "../../common/types/Job.ts";

@injectable()
export default class JobDatasource {
    private backend: AxiosInstance;
    constructor() {
        this.backend=api;
    }

    async getAllJobsAsAdmin(page:number,limit:number,query:string): Promise<{ jobs: Job[],total:number }>{
        const {data} = await this.backend.get<ApiResponse<Job[]>>(Constants.getAllJobAdminUrl+`?page=${page}&limit=${limit}&query=${query}`);
        if(data.success&&data.data){
            return {jobs: data.data,total:data.totalRecords??0};
        }
        throw new Error(`Error getting all jobs as admin.`);
    }
}