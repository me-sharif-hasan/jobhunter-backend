import {useEffect, useState} from "react";
import {DataTable, DataTableStateEvent} from "primereact/datatable";
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
import { ProgressSpinner } from 'primereact/progressspinner';

export default function JobIndex(){
    const jobController = dicontainer.get(JobController);
    const [jobs, setJobs] = useState<Job[]>([]);
    const [limit,setLimit]=useState(50);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [selectedJob, setSelectedJob] = useState<Job | null>(null);
    const [showViewDialog, setShowViewDialog] = useState(false);
    const [jobIndexingState, setJobIndexingState] = useState<SystemStatusNameValue | undefined>(undefined);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        jobController.getAllJobs(page,limit,"").then(
            ({jobs,total})=>{
                jobs.sort((a,b)=>{
                    if(a.jobParsedAt && b.jobParsedAt){
                        return new Date(b.jobParsedAt).getTime() - new Date(a.jobParsedAt).getTime();
                    }
                    return 0;
                });
                setJobs(jobs);
                setTotalPage(total);
            }
        ).finally(() => {
            setLoading(false);
        });
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
            <div className="break-words line-clamp-2 min-w-0" style={{
                color: rowData.jobLastDate&&new Date(rowData.jobLastDate).getTime() < new Date().getTime() ? "red" : "black",
            }}>
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
        <div className="flex flex-col gap-4">
            {/* Header Section with Actions */}
            <div className="flex justify-between items-center p-4 bg-white rounded-lg shadow-sm">
                <div className="flex items-center gap-4">
                    <Button 
                        className={`${
                            jobIndexingState === SystemStatusNameValue.JOB_INDEXING 
                            ? 'p-button-danger' 
                            : 'p-button-success'
                        } p-button-raised flex items-center gap-2`}
                        icon={jobIndexingState === SystemStatusNameValue.JOB_INDEXING ? "pi pi-stop" : "pi pi-play"}
                        onClick={() => {
                            jobController.runIndexer((status)=>{
                                setJobIndexingState(status);
                            });
                        }}
                    >
                        <span className="font-medium">
                            {jobIndexingState === SystemStatusNameValue.JOB_INDEXING ? "Stop Indexer" : "Start Indexer"}
                        </span>
                    </Button>
                </div>
                <div className="flex items-center gap-4">
                    <AdminDialog 
                        showModalButtonText="Add New Job" 
                        showButton={true}
                    >
                        <form onSubmit={addJobHandler} className="w-full max-w-lg">
                            <div className="p-8 flex flex-col gap-6">
                                <h2 className="text-xl font-semibold text-gray-700 mb-4">Add New Job</h2>
                                <FloatLabel className="w-full">
                                    <InputText 
                                        name="homepage" 
                                        id="job-home"
                                        className="w-full"
                                    />
                                    <label htmlFor="job-home">Job Home Page URL</label>
                                </FloatLabel>
                                <FloatLabel className="w-full">
                                    <InputText 
                                        name="jobListPage" 
                                        id="job-list-page"
                                        className="w-full"
                                    />
                                    <label htmlFor="job-list-page">Job List Page URL</label>
                                </FloatLabel>
                                <Button 
                                    type="submit"
                                    className="p-button-primary w-full mt-4"
                                    icon="pi pi-plus"
                                    label="Add Job"
                                />
                            </div>
                        </form>
                    </AdminDialog>
                </div>
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
                    totalRecords={totalPage??100}
                    onPage={(e: DataTableStateEvent)=>{
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
                        body={(rowData: Job) => textTemplate(rowData, 'title')}
                    />
                    <Column 
                        sortable 
                        field="company" 
                        header="Company" 
                        className="w-[20%]"
                        body={(rowData: Job) => textTemplate(rowData, 'company')}
                    />
                    <Column 
                        sortable 
                        field="jobUrl" 
                        header="Job Link" 
                        className="w-[6%]"
                        body={(rowData: Job) => urlTemplate(rowData)}
                    />
                    <Column 
                        sortable 
                        field="location" 
                        header="Location" 
                        className="w-[15%]"
                        // body={(rowData: Job) => textTemplate(rowData, 'location')}
                    />
                    <Column 
                        sortable 
                        field="jobParsedAt" 
                        header="Parsed At" 
                        className="w-[15%]"
                        // body={(rowData: Job) => textTemplate(rowData, 'jobParsedAt')}
                    />
                    <Column 
                        sortable 
                        field="experienceNeeded" 
                        header="Required Experience" 
                        className="w-[15%]"
                        // body={(rowData: Job) => textTemplate(rowData, 'experienceNeeded')}
                    />
                    <Column 
                        header="Actions" 
                        className="w-[5%]"
                        body={(rowData: Job) => viewButton(rowData)}
                    />
                </DataTable>
            </div>

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
                        <h2 className="text-xl font-bold mb-6 text-gray-800">{selectedJob.title}</h2>
                        <div className="space-y-6">
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Company</h3>
                                <p className="mt-1 text-gray-600">{selectedJob.company}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Location</h3>
                                <p className="mt-1 text-gray-600">{selectedJob.location}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Description</h3>
                                <p className="mt-1 text-gray-600 whitespace-pre-wrap">{selectedJob.jobDescription}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Skills Needed</h3>
                                <p className="mt-1 text-gray-600">{selectedJob.skillsNeeded}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Experience Needed</h3>
                                <p className="mt-1 text-gray-600">{selectedJob.experienceNeeded}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Apply Deadline</h3>
                                <p className="mt-1 text-gray-600">{selectedJob.jobLastDate??"Unavailable"}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700">Job URL</h3>
                                <a 
                                    href={selectedJob.jobUrl} 
                                    target="_blank" 
                                    rel="noopener noreferrer"
                                    className="mt-1 text-blue-600 hover:text-blue-800 underline break-all"
                                >
                                    View Job
                                </a>
                            </div>
                        </div>
                    </div>
                )}
            </AdminDialog>
        </div>
    </>;
}