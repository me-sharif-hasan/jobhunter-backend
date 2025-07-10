export default new class Constants {
    // baseUrl:string='https://jobhunterbackend.gentlesmoke-d65a2350.westus2.azurecontainerapps.io';
    baseUrl:string='http://localhost:8080';
    googleLoginUrl:string='/auth/google/registration';
    userInfoUrl: string='/api/user';
    getAllSitesAsAdminUrl: string="/admin/site";
    addSiteAsAdmin: string='/admin/site';
    updateSiteIndexingStrategy: string='/admin/indexing/save-strategy';
    validateSiteIndexingStrategy: string='/admin/indexing/validate-strategy';
    getAllJobAdminUrl: string='/admin/job';
    getSystemStatus: string = '/admin/system-status'
    refreshJobIndexUrl: string= '/admin/job/refresh';
    updateJobDuplicationStatusUrl: string= '/admin/job/mark-duplicate';
}