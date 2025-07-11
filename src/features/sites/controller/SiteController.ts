import {inject, injectable} from "inversify";
import SiteDatasource from "../datasource/SiteDatasource.ts";

@injectable()
export default class SiteController {
    constructor(
        @inject(SiteDatasource) protected siteDatasource: SiteDatasource,
    ) {
    }

    getAllSites(page=0,limit=20,query=""){
        return this.siteDatasource.getAllSitesAsAdmin(page,limit,query);
    }

    async aiAddNewSite(siteHomePage: string | File | null, siteCareerPage: string | File | null) {
        return this.siteDatasource.addNewSiteAutoMode(siteHomePage, siteCareerPage);
    }

    async getSiteIndexingStrategy(siteId: number) {
        console.log('SiteController: getSiteIndexingStrategy called with siteId:', siteId);
        try {
            const result = await this.siteDatasource.getSiteIndexingStrategy(siteId);
            console.log('SiteController: getSiteIndexingStrategy result:', result);
            return result;
        } catch (error) {
            console.error('SiteController: getSiteIndexingStrategy error:', error);
            throw error;
        }
    }

    async refreshSiteJobsIndex(siteId: number) {
        console.log('SiteController: refreshSiteJobsIndex called with siteId:', siteId);
        try {
            const result = await this.siteDatasource.refreshSiteJobsIndex(siteId);
            console.log('SiteController: refreshSiteJobsIndex result:', result);
            return result;
        } catch (error) {
            console.error('SiteController: refreshSiteJobsIndex error:', error);
            throw error;
        }
    }

    async updateIndexingStrategy(siteId: number, strategy: { type: 'AI' | 'MANUAL', jsCode?: string, config?: string }) {
        return this.siteDatasource.updateSiteIndexingStrategy(siteId, strategy);
    }

    async validateIndexingStrategy(siteId: number, strategy: { type: 'AI' | 'MANUAL', jsCode?: string, config?: string }) {
        return this.siteDatasource.validateSiteIndexingStrategy(siteId, strategy);
    }
}