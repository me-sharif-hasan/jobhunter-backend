import {injectable} from "inversify";
import api from "../../../utility/HttpClient.ts";
import {AxiosInstance} from "axios";
import Constants from "../../../values/constants.ts";
import Site from "../../common/types/Site.ts";
import ApiResponse from "../../common/types/ApiResponse.ts";

@injectable()
export default class SiteDatasource {
    private backend: AxiosInstance;
    constructor() {
        this.backend = api;
    }

    async getAllSitesAsAdmin(page:number,limit:number,query:string): Promise<{ sites: Site[],total:number }>{
        const {data} = await this.backend.get<ApiResponse<Site[]>>(Constants.getAllSitesAsAdminUrl+`?page=${page}&limit=${limit}&query=${query}`);
        if(data.success&&data.data){
            return {sites: data.data,total:data.totalRecords??0};
        }
        throw new Error(`Error getting all sites as admin.`);
    }

    addNewSiteAutoMode(siteHomePage: string | File | null, siteCareerPage: string | File | null) {
        return this.backend.post(
            Constants.addSiteAsAdmin,
            {
                "homepage":siteHomePage,
                "jobListPageUrl":siteCareerPage
            }
        )
    }
}