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

export default function SiteIndex(){
    const siteController = dicontainer.get(SiteController);
    const [sites, setSites] = useState<Site[]>([]);
    const [limit,setLimit]=useState(10);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    
    useEffect(() => {
        siteController.getAllSites(page,limit,"").then(
            ({sites,total})=>{
                sites.sort((a,b)=>a.id - b.id);
                setSites(sites);
                setTotalPage(total);
            }
        );
    }, [page,limit,totalPage]);


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

    /**
     * Local states
     */

    return <>
        <AdminDialog hideModalButtonText={'Add new site'} showModalButtonText={'Close dialog'}>
            <form onSubmit={addSiteHandler}>
                <div style={{padding:'2em',display:'flex',flexDirection:'column', justifyContent:'center', alignItems: 'center',gap:'30px'}}>
                    <FloatLabel>
                        <InputText name={'homepage'} id="site-home"/>
                        <label htmlFor="site-home">Site Home Page URL</label>
                    </FloatLabel>
                    <FloatLabel>
                        <InputText name={'jobListPage'} id="job-list-page"/>
                        <label htmlFor="job-list-page">Site Job List Page URL</label>
                    </FloatLabel>
                    <Button type={"submit"}>Add Site Handler</Button>
                </div>
            </form>
        </AdminDialog>

        {page} - {limit}
        <DataTable
            totalRecords={totalPage}
            onPage={(e)=>{
                console.log(e);
                setPage(e.first/e.rows);
                setLimit(e.rows);
            }}
            editMode={"row"}
            first={page*limit}
            paginator
            rows={limit}
            rowsPerPageOptions={[5, 10, 25, 50]}
            value={sites}
            lazy
            tableStyle={{ minWidth: '50rem' }}
        >
            <Column sortable field="id" header="Id"></Column>
            <Column sortable field="name" header="Name"></Column>
            <Column sortable field="jobListingPageUrl" header="Job List Location"></Column>
            <Column sortable field="lastCrawledAt" header="Last Crawled At"></Column>
            <Column sortable field="description" header="Description"></Column>
        </DataTable>
    </>
}