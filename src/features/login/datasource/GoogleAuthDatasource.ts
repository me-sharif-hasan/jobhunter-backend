import api from "../../../utility/HttpClient.ts";
import {AxiosInstance} from "axios";
import Constants from "../../../values/constants.ts";
import { injectable } from 'inversify';

@injectable()
export default class GoogleAuthDatasource {
    private backendApi: AxiosInstance;
    constructor() {
        this.backendApi = api;
    }
    async printUserInfo(googleAuthToken:string){
        return await this.backendApi.post(Constants.googleLoginUrl,{
            token:googleAuthToken,
        })
    }
}