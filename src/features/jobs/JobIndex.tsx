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
import {SystemStatusNameValue} from "../common/types/SystemStatus.ts";

export default function JobIndex(){
    const jobController = dicontainer.get(JobController);
    const [jobs, setJobs] = useState<Job[]>([]);
    const [limit,setLimit]=useState(10);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [selectedJob, setSelectedJob] = useState<Job | null>(null);
    const [showViewDialog, setShowViewDialog] = useState(false);
    const [jobIndexingState, setJobIndexingState] = useState<SystemStatusNameValue | undefined>(undefined);

    useEffect(() => {
        jobController.getAllJobs(page,limit,"").then(
            ({jobs,total})=>{
                jobs.sort((a,b)=>a.jobId - b.jobId);
                setJobs(jobs);
                setTotalPage(total);
            }
        );
    }, [page,limit,totalPage]);

    useEffect(() =>{
        jobController.getJobIndexingStatus().then(
            status=>setJobIndexingState(status?.value)
        )
    }, []);

    const truncateText = (text: string, maxLength: number = 255) => {
        if (!text) return '';
        return text.length > maxLength ? `${text.substring(0, maxLength)}...` : text;
    };

    const viewButton = (rowData: Job) => {
        return (
            <Button 
                icon="pi pi-eye" 
                className="p-button-text p-button-rounded p-button-info"
                onClick={() => {
                    setSelectedJob(rowData);
                    setShowViewDialog(true);
                }}
            />
        );
    };

    const textTemplate = (rowData: Job, field: keyof Job) => {
        return (
            <div className="break-words line-clamp-2 min-w-0">
                {truncateText(rowData[field] as string)}
            </div>
        );
    };

    const urlTemplate = (rowData: Job) => {
        return (
            <a 
                href={rowData.jobUrl} 
                target="_blank" 
                rel="noopener noreferrer"
                className="text-blue-600 hover:text-blue-800 break-words line-clamp-2 min-w-0"
            >
                <Button
                    icon="pi pi-link"
                    className="p-button-text p-button-rounded p-button-info"
                    onClick={(e) => {
                        e.stopPropagation(); // Prevent row click event
                    }}
                />
            </a>
        );
    };

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

    return <>
    {JSON.stringify(jobIndexingState)}
        <AdminDialog 
            showModalButtonText="Close Dialog" 
            hideModalButtonText="Add New Job Manually"
            showButton={true}
        >
            <form onSubmit={addJobHandler}>
                <div className="p-8 flex flex-col justify-center items-center gap-8">
                    <FloatLabel>
                        <InputText name="homepage" id="job-home"/>
                        <label htmlFor="job-home">Job Home Page URL</label>
                    </FloatLabel>
                    <FloatLabel>
                        <InputText name="jobListPage" id="job-list-page"/>
                        <label htmlFor="job-list-page">Job List Page URL</label>
                    </FloatLabel>
                    <Button type="submit">Add Job</Button>
                </div>
            </form>
        </AdminDialog>

        {/* View Job Details Modal */}
        <AdminDialog 
            visible={showViewDialog}
            onHide={() => setShowViewDialog(false)}
            hideModalButtonText="Close"
            showModalButtonText="View Job Details"
            showButton={false}
        >
            {selectedJob && (
                <div className="p-6 max-w-2xl">
                    <h2 className="text-xl font-bold mb-4">{selectedJob.title}</h2>
                    <div className="space-y-4">
                        <div>
                            <h3 className="font-semibold">Company</h3>
                            <p>{selectedJob.company}</p>
                        </div>
                        <div>
                            <h3 className="font-semibold">Location</h3>
                            <p>{selectedJob.location}</p>
                        </div>
                        <div>
                            <h3 className="font-semibold">Description</h3>
                            <p className="whitespace-pre-wrap">{selectedJob.jobDescription}</p>
                        </div>
                        <div>
                            <h3 className="font-semibold">Skills Needed</h3>
                            <p>{selectedJob.skillsNeeded}</p>
                        </div>
                        <div>
                            <h3 className="font-semibold">Experience Needed</h3>
                            <p>{selectedJob.experienceNeeded}</p>
                        </div>
                        <div>
                            <h3 className="font-semibold">Job URL</h3>
                            <a 
                                href={selectedJob.jobUrl} 
                                target="_blank" 
                                rel="noopener noreferrer"
                                className="text-blue-600 hover:text-blue-800 underline break-all"
                            >
                                View Job
                            </a>
                        </div>
                    </div>
                </div>
            )}
        </AdminDialog>

        <div className="card p-4">
            <DataTable
                totalRecords={totalPage??100}
                onPage={(e)=>{
                    setPage(e.first/e.rows);
                    setLimit(e.rows);
                }}
                editMode="row"
                first={page*limit}
                paginator
                rows={limit}
                rowsPerPageOptions={[5, 10, 25, 50]}
                value={jobs}
                lazy
                className="p-datatable-sm"
                tableStyle={{ width: '100%' }}
                scrollable
                scrollHeight="calc(100vh - 200px)"
            >
                <Column 
                    sortable 
                    field="title" 
                    header="Title" 
                    className="w-[25%]"
                    body={(rowData) => textTemplate(rowData, 'title')}
                />
                <Column 
                    sortable 
                    field="company" 
                    header="Company" 
                    className="w-[20%]"
                    body={(rowData) => textTemplate(rowData, 'company')}
                />
                <Column 
                    sortable 
                    field="jobUrl" 
                    header="Job Link" 
                    className="w-[25%]"
                    body={urlTemplate}
                />
                <Column 
                    sortable 
                    field="location" 
                    header="Location" 
                    className="w-[15%]"
                    body={(rowData) => textTemplate(rowData, 'location')}
                />
                <Column 
                    header="Actions" 
                    className="w-[5%]"
                    body={viewButton}
                />
            </DataTable>
        </div>
    </>;
}