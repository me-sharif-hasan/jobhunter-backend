import dicontainer from "../../../utility/ioc_registry.ts";
import SiteController from "../controller/SiteController.ts";
import {useEffect, useState} from "react";
import Site from "../../common/types/Site.ts";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import "primereact/resources/themes/lara-light-indigo/theme.css";
import AdminDialog from "../../common/widgets/AdminDialog.tsx";
import {FloatLabel} from "primereact/floatlabel";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import { ProgressSpinner } from 'primereact/progressspinner';
import { Skeleton } from 'primereact/skeleton';

export default function SiteIndex(){
    const siteController = dicontainer.get(SiteController);
    const [sites, setSites] = useState<Site[]>([]);
    const [limit,setLimit]=useState(10);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [loading, setLoading] = useState(true);
    
    useEffect(() => {
        let mounted = true;
        setLoading(true);
        
        // Create placeholder data while loading
        const placeholderData = Array(limit).fill(null).map((_, index) => ({
            id: index,
            name: '',
            homepage: '',
            description: '',
            iconUrl: '',
            jobListPageUrl: '',
            lastCrawledAt: new Date()
        }));
        setSites(placeholderData);
        
        siteController.getAllSites(page,limit,"").then(
            ({sites,total})=>{
                if (mounted) {
                    sites.sort((a,b)=>a.id - b.id);
                    setSites(sites);
                    setTotalPage(total);
                    setLoading(false);
                }
            }
        ).catch(() => {
            if (mounted) {
                setLoading(false);
            }
        });

        return () => {
            mounted = false;
        };
    }, [page,limit]);

    const loadingTemplate = () => {
        return <Skeleton width="100%" height="2rem"></Skeleton>;
    };

    const cellTemplate = (rowData: Site, field: keyof Site) => {
        if (loading) {
            return loadingTemplate();
        }
        let value = rowData[field];
        if (field === 'lastCrawledAt' && value instanceof Date) {
            value = value.toLocaleString();
        }
        return <span>{value?.toString()}</span>;
    };

    function addSiteHandler(e:any){
        e.preventDefault();
        const form = e.target as HTMLFormElement;
        const formData:FormData = new FormData(form);
        const siteHomePage=formData.get("homepage");
        const siteCareerPage=formData.get("jobListPage");
        siteController.aiAddNewSite(siteHomePage,siteCareerPage).then(()=>{
            alert('Site added successfully.');
        }).catch(err=>{
            alert(err);
        })
    }

    return <>
        <div className="flex flex-col gap-4">
            {/* Add Site Dialog */}
            <div className="flex justify-end">
                <AdminDialog showModalButtonText={'Add New Site'}>
                    <form onSubmit={addSiteHandler}>
                        <div className="p-8 flex flex-col gap-6">
                            <h2 className="text-xl font-semibold text-gray-700 mb-4">Add New Site</h2>
                            <FloatLabel className="w-full">
                                <InputText 
                                    name="homepage" 
                                    id="site-home"
                                    className="w-full"
                                />
                                <label htmlFor="site-home">Site Home Page URL</label>
                            </FloatLabel>
                            <FloatLabel className="w-full">
                                <InputText 
                                    name="jobListPage" 
                                    id="job-list-page"
                                    className="w-full"
                                />
                                <label htmlFor="job-list-page">Site Job List Page URL</label>
                            </FloatLabel>
                            <Button 
                                type="submit"
                                className="p-button-primary w-full mt-4"
                                icon="pi pi-plus"
                                label="Add Site"
                            />
                        </div>
                    </form>
                </AdminDialog>
            </div>

            {/* DataTable Section */}
            <div className="card bg-white rounded-lg shadow-sm relative">
                {loading && (
                    <div className="absolute inset-0 bg-white bg-opacity-60 z-10 flex items-center justify-center">
                        <ProgressSpinner 
                            style={{width: '50px', height: '50px'}}
                            strokeWidth="4"
                            fill="var(--surface-ground)"
                            animationDuration=".5s"
                        />
                    </div>
                )}
                <DataTable
                    loading={loading}
                    totalRecords={totalPage}
                    onPage={(e)=>{
                        setPage(e.first/e.rows);
                        setLimit(e.rows);
                    }}
                    editMode="row"
                    first={page*limit}
                    paginator
                    rows={limit}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                    value={sites}
                    lazy
                    className="p-datatable-sm"
                    tableStyle={{ width: '100%' }}
                    scrollable
                    scrollHeight="calc(100vh - 200px)"
                >
                    <Column sortable field="id" header="ID" className="w-[10%]" 
                           body={(rowData: Site) => cellTemplate(rowData, 'id')} />
                    <Column sortable field="name" header="Name" className="w-[20%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'name')} />
                    <Column sortable field="jobListPageUrl" header="Job List Location" className="w-[35%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'jobListPageUrl')} />
                    <Column sortable field="lastCrawledAt" header="Last Crawled At" className="w-[20%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'lastCrawledAt')} />
                    <Column sortable field="description" header="Description" className="w-[15%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'description')} />
                </DataTable>
            </div>
        </div>
    </>;
}