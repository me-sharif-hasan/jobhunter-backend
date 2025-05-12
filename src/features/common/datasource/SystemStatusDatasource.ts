import {injectable} from "inversify";
import api from "../../../utility/HttpClient.ts";
import {AxiosInstance} from "axios";
import SystemStatus, {SystemStatusName} from "../types/SystemStatus.ts";
import Constants from "../../../values/constants.ts";

@injectable()
export default class SystemStatusDatasource{
    private backend: AxiosInstance;
    constructor() {
        this.backend = api;
    }

    async getSystemStatus(key:SystemStatusName): Promise<SystemStatus | undefined>{
        const {data} = await this.backend.get(`${Constants.getSystemStatus}?name=${key}`);
        if(data?.success&&data?.data){
            return data?.data;
        }
    }
}