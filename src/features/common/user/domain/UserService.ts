import {injectable} from "inversify";
import User from "../../types/User.ts";
import httpClient from "../../../../utility/HttpClient.ts";
import {AxiosInstance} from "axios";
import Constants from "../../../../values/constants.ts";

@injectable()
export class UserService {
    private api: AxiosInstance;
    constructor() {
        this.api=httpClient;
    }
    async getLoggedInUser(): Promise<User>{
        const {data} = await this.api.get(Constants.userInfoUrl);
        if(data.success){
            return data.data;
        }
        throw new Error("Not logged in");
    }
}