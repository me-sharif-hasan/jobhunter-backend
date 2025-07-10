import dicontainer from "../../../utility/ioc_registry.ts";
import SiteController from "../controller/SiteController.ts";
import {useEffect, useState} from "react";
import Site from "../../common/types/Site.ts";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import AdminDialog from "../../common/widgets/AdminDialog.tsx";
import {FloatLabel} from "primereact/floatlabel";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import { ProgressSpinner } from 'primereact/progressspinner';
import { Skeleton } from 'primereact/skeleton';
import { TabView, TabPanel } from 'primereact/tabview';
import { Dialog } from 'primereact/dialog';
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import { json } from '@codemirror/lang-json';
import { oneDark } from '@codemirror/theme-one-dark';

export default function SiteIndex(){
    const siteController = dicontainer.get(SiteController);
    const [sites, setSites] = useState<Site[]>([]);
    const [limit,setLimit]=useState(10);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [loading, setLoading] = useState(true);
    
    // Indexing strategy dialog state
    const [indexingDialogVisible, setIndexingDialogVisible] = useState(false);
    const [selectedSite, setSelectedSite] = useState<Site | null>(null);
    const [activeTabIndex, setActiveTabIndex] = useState(0);
    const [aiJsCode, setAiJsCode] = useState('');
    const [jsonConfig, setJsonConfig] = useState('');
    const [validationResults, setValidationResults] = useState('');
    const [isValidating, setIsValidating] = useState(false);
    const [jobCount, setJobCount] = useState<number | null>(null);

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

    // Handler for opening indexing strategy dialog
    const openIndexingDialog = (site: Site) => {
        setSelectedSite(site);
        setIndexingDialogVisible(true);
        setActiveTabIndex(0);
        setAiJsCode('');
        setJsonConfig('');
        setValidationResults('');
    };

    // Handler for closing indexing dialog
    const closeIndexingDialog = () => {
        setIndexingDialogVisible(false);
        setSelectedSite(null);
        setAiJsCode('');
        setJsonConfig('');
        setValidationResults('');
    };

    // Handler for testing configuration
    const testConfiguration = async () => {
        if (!selectedSite) {
            alert('No site selected.');
            return;
        }

        const strategy = activeTabIndex === 0
            ? { type: 'ai' as const, jsCode: aiJsCode }
            : { type: 'json' as const, config: jsonConfig };

        // Validate that at least one field has content
        if (strategy.type === 'ai' && !strategy.jsCode.trim()) {
            alert('Please enter JavaScript code before testing.');
            return;
        }

        if (strategy.type === 'json' && !strategy.config.trim()) {
            alert('Please enter JSON configuration before testing.');
            return;
        }

        setIsValidating(true);
        setValidationResults('');
        setJobCount(null);

        try {
            const jobs = await siteController.validateIndexingStrategy(selectedSite.id, strategy);
            const jobsJson = JSON.stringify(jobs, null, 2);
            setValidationResults(jobsJson);
            setJobCount(jobs.length);
        } catch (error: any) {
            console.error('Error validating indexing strategy:', error);
            setValidationResults(`Error: ${error.message || 'Failed to validate indexing strategy'}`);
            setJobCount(null);
        } finally {
            setIsValidating(false);
        }
    };

    // Handler for saving configuration
    const saveConfiguration = async () => {
        if (!selectedSite) {
            alert('No site selected.');
            return;
        }

        const strategy = activeTabIndex === 0
            ? { type: 'ai' as const, jsCode: aiJsCode }
            : { type: 'json' as const, config: jsonConfig };

        // Validate that at least one field has content
        if (strategy.type === 'ai' && !strategy.jsCode.trim()) {
            alert('Please enter JavaScript code before saving.');
            return;
        }

        if (strategy.type === 'json' && !strategy.config.trim()) {
            alert('Please enter JSON configuration before saving.');
            return;
        }

        try {
            await siteController.updateIndexingStrategy(selectedSite.id, strategy);
            alert('Indexing strategy saved successfully!');
            closeIndexingDialog();
        } catch (error: any) {
            console.error('Error saving indexing strategy:', error);
            alert(`Error: ${error.message || 'Failed to save indexing strategy'}`);
        }
    };

    // Actions column template
    const actionsTemplate = (rowData: Site) => {
        if (loading) {
            return loadingTemplate();
        }

        return (
            <div className="flex gap-2">
                <Button
                    icon="pi pi-cog"
                    className="p-button-rounded p-button-text p-button-sm"
                    tooltip="Add Indexing Strategy"
                    tooltipOptions={{ position: 'top' }}
                    onClick={() => openIndexingDialog(rowData)}
                />
            </div>
        );
    };

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
                    <Column sortable field="id" header="ID" className="w-[8%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'id')} />
                    <Column sortable field="name" header="Name" className="w-[18%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'name')} />
                    <Column sortable field="jobListPageUrl" header="Job List Location" className="w-[30%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'jobListPageUrl')} />
                    <Column sortable field="lastCrawledAt" header="Last Crawled At" className="w-[18%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'lastCrawledAt')} />
                    <Column sortable field="description" header="Description" className="w-[16%]"
                           body={(rowData: Site) => cellTemplate(rowData, 'description')} />
                    <Column header="Actions" className="w-[10%]"
                           body={actionsTemplate} />
                </DataTable>
            </div>

            {/* Indexing Strategy Dialog */}
            <Dialog
                header={`Indexing Strategy - ${selectedSite?.name || 'Site'}`}
                visible={indexingDialogVisible}
                onHide={closeIndexingDialog}
                style={{ width: '70vw' }}
                footer={
                    <div className="flex justify-end gap-1">
                        <Button
                            label="Close"
                            icon="pi pi-times"
                            onClick={closeIndexingDialog}
                            className="p-button-text p-button-sm"
                            size="small"
                        />
                        <Button
                            label="Test"
                            icon="pi pi-check"
                            onClick={testConfiguration}
                            className="p-button-primary p-button-sm"
                            size="small"
                            loading={isValidating}
                        />
                        <Button
                            label="Save"
                            icon="pi pi-save"
                            onClick={saveConfiguration}
                            className="p-button-success p-button-sm"
                            size="small"
                        />
                    </div>
                }
            >
                <div className="p-4">
                    <TabView activeIndex={activeTabIndex} onTabChange={(e) => setActiveTabIndex(e.index)}>
                        <TabPanel header="By AI">
                            <div className="flex flex-col gap-4 pt-4">
                                <p className="text-sm text-gray-600 mb-2">
                                    Write JavaScript code for extracting job IDs using AI assistance. This field is optional and not required by the backend.
                                </p>
                                <div className="w-full">
                                    <CodeMirror
                                        value={aiJsCode}
                                        onChange={(value) => setAiJsCode(value)}
                                        height="300px"
                                        extensions={[javascript({ jsx: true })]}
                                        theme={oneDark}
                                        placeholder="return window.location.href;"
                                    />
                                </div>
                            </div>
                        </TabPanel>
                        <TabPanel header="By JSON">
                            <div className="flex flex-col gap-4 pt-4">
                                <p className="text-sm text-gray-600 mb-2">
                                    Provide a JSON configuration that defines the indexing strategy structure and rules.
                                </p>
                                <div className="w-full">
                                    <CodeMirror
                                        value={jsonConfig}
                                        onChange={(value) => setJsonConfig(value)}
                                        height="300px"
                                        extensions={[json()]}
                                        theme={oneDark}
                                        placeholder='{"selector": ".job-item", "attributes": ["data-id", "href"], "rules": []}'
                                    />
                                </div>
                            </div>
                        </TabPanel>
                    </TabView>

                    {/* Validation Results Section */}
                    {(validationResults || isValidating) && (
                        <div className="mt-6">
                            <div className="flex justify-between items-center mb-3">
                                <h4 className="text-lg font-semibold text-gray-700">Validation Results</h4>
                                {isValidating && (
                                    <ProgressSpinner style={{width: '20px', height: '20px'}} strokeWidth="4" />
                                )}
                            </div>

                            {isValidating ? (
                                <div className="p-4 bg-blue-50 border border-blue-200 rounded-md">
                                    <p className="text-blue-700 m-0">Testing configuration with backend...</p>
                                </div>
                            ) : validationResults.startsWith('Error:') ? (
                                <div className="p-4 bg-red-50 border border-red-200 rounded-md">
                                    <p className="text-red-700 m-0">{validationResults}</p>
                                </div>
                            ) : (
                                <div className="w-full">
                                    <p className="text-sm text-gray-600 mb-2">
                                        Jobs found by the indexing strategy:
                                    </p>
                                    <CodeMirror
                                        value={validationResults}
                                        height="200px"
                                        extensions={[json()]}
                                        theme={oneDark}
                                        editable={false}
                                    />
                                    <p className="text-sm text-gray-500 mt-2">
                                        Total Jobs: {jobCount}
                                    </p>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </Dialog>
        </div>
    </>;
}