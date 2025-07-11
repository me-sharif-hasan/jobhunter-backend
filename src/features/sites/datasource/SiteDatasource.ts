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

    async getSiteIndexingStrategy(siteId: number) {
        const url = `${Constants.getSiteIndexingStrategy}?site_id=${siteId}`;
        console.log('SiteDatasource: Making GET request to:', url);

        try {
            const { data } = await this.backend.get<ApiResponse<any>>(url);
            console.log('SiteDatasource: Response received:', data);

            if (data.success) {
                console.log('SiteDatasource: Returning data:', data.data);
                return data.data;
            }
            throw new Error(data.message || 'Error fetching site indexing strategy.');
        } catch (error) {
            console.error('SiteDatasource: Network error:', error);
            throw new Error(error.message || 'Error fetching site indexing strategy.');
        }
    }

    async refreshSiteJobsIndex(siteId: number) {
        const url = `${Constants.refreshJobsIndex}?site_id=${siteId}`;
        console.log('SiteDatasource: Making GET request to refresh jobs index:', url);

        try {
            const { data } = await this.backend.get<ApiResponse<any>>(url);
            console.log('SiteDatasource: Refresh response received:', data);

            if (data.success) {
                console.log('SiteDatasource: Refresh successful, message:', data.message);
                return data;
            }
            throw new Error(data.message || 'Error refreshing site jobs index.');
        } catch (error) {
            console.error('SiteDatasource: Refresh network error:', error);
            throw new Error(error.message || 'Error refreshing site jobs index.');
        }
    }

    async updateSiteIndexingStrategy(siteId: number, strategy: { type: 'AI' | 'MANUAL', jsCode?: string, config?: string }) {
        // Transform the strategy to match backend DTO structure
        const requestPayload = {
            type: strategy.type,
            processFlow: strategy.type === 'MANUAL' && strategy.config
                ? JSON.parse(strategy.config)
                : []
        };

        const { data } = await this.backend.post<ApiResponse<any>>(
            `${Constants.updateSiteIndexingStrategy}?site_id=${siteId}`,
            requestPayload
        );

        if (data.success) {
            return data;
        }
        throw new Error(data.message || 'Error updating site indexing strategy.');
    }

    async validateSiteIndexingStrategy(siteId: number, strategy: { type: 'AI' | 'MANUAL', jsCode?: string, config?: string }) {
        // Transform the strategy to match backend DTO structure
        const requestPayload = {
            type: strategy.type,
            processFlow: strategy.type === 'MANUAL' && strategy.config
                ? JSON.parse(strategy.config)
                : []
        };

        const { data } = await this.backend.post<ApiResponse<any[]>>(
            `${Constants.validateSiteIndexingStrategy}?site_id=${siteId}`,
            requestPayload
        );

        if (data.success) {
            return data.data || [];
        }
        throw new Error(data.message || 'Error validating site indexing strategy.');
    }
}