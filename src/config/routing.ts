import SiteIndex from "../features/sites/widget/SiteIndex.tsx";
import DashboardPage from "../features/dashboard/DashboardPage.tsx";
import IndexPage from "../features/common/IndexPage.tsx";
import LoginPage from "../features/login/LoginPage.tsx";
import { ComponentType } from "react";
import JobIndex from "../features/jobs/JobIndex.tsx";

export class RouterConfig {
    url: string | undefined;
    component: ComponentType | undefined;
    children?: RouterConfig[] | undefined;
    title?: string | undefined;

    constructor(url: string | undefined, component: ComponentType | undefined, children?: RouterConfig[], title?: string | undefined) {
        this.url = url;
        this.title = title;
        this.component = component;
        this.children = children;
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
                title: 'Sites',
                url: 'sites',
                component: SiteIndex
            },
            {
                title: 'Jobs',
                url: 'jobs',
                component: JobIndex,
            }
        ]
    }
];