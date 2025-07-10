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

    async updateIndexingStrategy(siteId: number, strategy: { type: 'ai' | 'json', jsCode?: string, config?: string }) {
        return this.siteDatasource.updateSiteIndexingStrategy(siteId, strategy);
    }

    async validateIndexingStrategy(siteId: number, strategy: { type: 'ai' | 'json', jsCode?: string, config?: string }) {
        return this.siteDatasource.validateSiteIndexingStrategy(siteId, strategy);
    }
}