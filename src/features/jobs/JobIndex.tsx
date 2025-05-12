import {useEffect, useState} from "react";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import "primereact/resources/themes/lara-light-indigo/theme.css";
import {FloatLabel} from "primereact/floatlabel";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import JobController from "./controller/JobController.ts";
import dicontainer from "../../utility/ioc_registry.ts";
import Job from "../common/types/Job.ts";
import AdminDialog from "../common/widgets/AdminDialog.tsx";

export default function JobIndex(){
    const jobController = dicontainer.get(JobController);
    const [jobs, setJobs] = useState<Job[]>([]);
    const [limit,setLimit]=useState(10);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);

    useEffect(() => {
        jobController.getAllJobs(page,limit,"").then(
            ({jobs,total})=>{
                jobs.sort((a,b)=>a.jobId - b.jobId);
                setJobs(jobs);
                setTotalPage(total);
            }
        );
    }, [page,limit,totalPage]);


    function addJobHandler(e:any){
        e.preventDefault();
        // const form = e.target as HTMLFormElement;
        // const formData:FormData = new FormData(form);
        // const siteHomePage=formData.get("homepage");
        // const siteCareerPage=formData.get("jobListPage");
        // jobController.aiAddNewSite(siteHomePage,siteCareerPage).then(()=>{
        //     alert('Site added successfully.');
        // }).catch(err=>{
        //     alert(err);
        // })
    }

    /**
     * Local states
     */

    return <>
        <AdminDialog hideModalButtonText={'Add new job'} showModalButtonText={'Close dialog'}>
            <form onSubmit={addJobHandler}>
                <div style={{padding:'2em',display:'flex',flexDirection:'column', justifyContent:'center', alignItems: 'center',gap:'30px'}}>
                    <FloatLabel>
                        <InputText name={'homepage'} id="job-home"/>
                        <label htmlFor="job-home">Job Home Page URL</label>
                    </FloatLabel>
                    <FloatLabel>
                        <InputText name={'jobListPage'} id="job-list-page"/>
                        <label htmlFor="job-list-page">Job Job List Page URL</label>
                    </FloatLabel>
                    <Button type={"submit"}>Add Job Handler</Button>
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
            value={jobs}
            lazy
            tableStyle={{ minWidth: '50rem' }}
        >
            <Column sortable field="jobId" header="Job ID"></Column>
            <Column sortable field="title" header="Title"></Column>
            <Column sortable field="company" header="Company"></Column>
            <Column sortable field="jobUrl" header="Job Link"></Column>
            <Column sortable field="location" header="Location"></Column>
        </DataTable>
    </>
}