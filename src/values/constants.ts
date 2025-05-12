export default new class Constants {
    baseUrl:string='http://localhost:8080';
    googleLoginUrl:string='/auth/google/registration';
    userInfoUrl: string='/api/user';
    getAllSitesAsAdminUrl: string="/admin/site";
    addSiteAsAdmin: string='/admin/site';
    getAllJobAdminUrl: string='/admin/job';
    getSystemStatus: string = '/admin/system-status'
}