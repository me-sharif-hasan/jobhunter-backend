import SiteIndex from "../features/sites/widget/SiteIndex.tsx";
import DashboardPage from "../features/dashboard/DashboardPage.tsx";
import IndexPage from "../features/common/IndexPage.tsx";
import LoginPage from "../features/login/LoginPage.tsx";
import { ComponentType } from "react";
import JobIndex from "../features/jobs/JobIndex.tsx";
import { StatisticsPage } from "../features/statistics/StatisticsPage.tsx";

export class RouterConfig {
    url: string | undefined;
    component: ComponentType | undefined;
    children?: RouterConfig[] | undefined;
    title?: string | undefined;
    icon?: String | undefined;

    constructor(
        url: string | undefined, 
        component: ComponentType | undefined, 
        children?: RouterConfig[], 
        title?: string | undefined, 
        icon?: String | undefined
    ) {
        this.url = url;
        this.title = title;
        this.component = component;
        this.children = children;
        this.icon = icon;
    }
}

export const route_map: RouterConfig[] = [
    {
        url: '/',
        component: IndexPage
    },
    {
        url: '/login',
        component: LoginPage
    },
    {
        title: 'Dashboard',
        url: '/dashboard',
        component: DashboardPage,
        children: [
            {
                title: 'Statistics',
                url: 'index',
                component: StatisticsPage,
                icon: '<i class="pi pi-fw pi-chart-line" />'
                
            },
            {
                title: 'Sites',
                url: 'sites',
                component: SiteIndex,
                icon: '<i class="pi pi-fw pi-globe" />'
            },
            {
                title: 'Jobs',
                url: 'jobs',
                component: JobIndex,
                icon: '<i class="pi pi-fw pi-briefcase" />'
            }
        ]
    }
];