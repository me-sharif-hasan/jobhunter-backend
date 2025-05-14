import { Outlet, NavLink } from "react-router-dom";
import { route_map } from "../../config/routing.ts";
import { useState } from "react";

export default function DashboardPage() {
    const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
    const [showNotifications, setShowNotifications] = useState(false);
    // Find the dashboard route to access its children
    const dashboardRoute = route_map.find(route => route.url === "/dashboard");
    const sidebarLinks = dashboardRoute?.children?.filter(child => child.title) || [];

    return (
        <div className="flex h-screen bg-white font-sans">
            {/* Sidebar */}
            <aside 
                className={`${
                    isSidebarCollapsed ? 'w-20' : 'w-64'
                } bg-white shadow-xl flex flex-col transition-all duration-300 ease-in-out z-20`}
            >
                <div className="p-4 border-b border-slate-100 flex items-center justify-between">
                    {!isSidebarCollapsed && (
                        <h1 className="!text-xl font-bold bg-gradient-to-r from-indigo-600 to-violet-600 bg-clip-text text-transparent">
                            JobHunter
                        </h1>
                    )}
                    <button 
                        onClick={() => setIsSidebarCollapsed(!isSidebarCollapsed)}
                        className="ml-5 p-1.5 rounded-md hover:bg-indigo-50 text-indigo-500 transition-colors duration-200"
                    >
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" 
                                d={isSidebarCollapsed 
                                    ? "M9 5l7 7-7 7" 
                                    : "M15 19l-7-7 7-7"} 
                            />
                        </svg>
                    </button>
                </div>
                <nav className="flex-1 p-4 overflow-y-auto">
                    {sidebarLinks.length > 0 && (
                        <div className="space-y-2">
                            {!isSidebarCollapsed && (
                                <h2 className="text-xs font-semibold text-slate-400 uppercase tracking-wider px-3 mb-4">
                                    Navigation
                                </h2>
                            )}
                            <ul className="space-y-1">
                                {sidebarLinks.map(link => (
                                    <li key={link.url}>
                                        <NavLink
                                            to={`/dashboard/${link.url}`}
                                            className={({ isActive }) =>
                                                `flex items-center px-3 py-2 rounded-lg transition-all duration-200 ${
                                                    isActive
                                                        ? "bg-gradient-to-r from-indigo-500 to-violet-500 !text-white shadow-lg shadow-indigo-500/20"
                                                        : "text-slate-600 hover:bg-indigo-50"
                                                }`
                                            }
                                        >
                                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" 
                                                    d={link.url === "jobs" 
                                                        ? "M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                                                        : link.url === "sites"
                                                        ? "M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                                                        : "M4 6h16M4 10h16M4 14h16M4 18h16"
                                                    } 
                                                />
                                            </svg>
                                            {!isSidebarCollapsed && (
                                                <span className="ml-3">{link.title}</span>
                                            )}
                                        </NavLink>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </nav>
            </aside>

            {/* Main Content */}
            <div className="flex-1 flex flex-col">
                {/* Top Bar */}
                <header className="bg-white shadow-sm p-4 flex justify-between items-center sticky top-0 z-10">
                    <div className="flex items-center space-x-4">
                        <h2 className="text-xl font-semibold text-slate-800">Overview</h2>
                    </div>
                    <div className="flex items-center space-x-6">
                        <div className="relative">
                            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <svg className="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                </svg>
                            </div>
                            <input
                                type="text"
                                placeholder="Search..."
                                className="pl-10 pr-4 py-2 w-64 bg-slate-50 border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all duration-200"
                            />
                        </div>
                        <div className="relative">
                            <button 
                                onClick={() => setShowNotifications(!showNotifications)}
                                className="relative p-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-colors duration-200"
                            >
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth="2"
                                        d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
                                    />
                                </svg>
                                <span className="absolute top-0 right-0 inline-flex items-center justify-center px-2 py-1 text-xs font-bold leading-none text-white transform translate-x-1/2 -translate-y-1/2 bg-red-500 rounded-full">2</span>
                            </button>
                            {showNotifications && (
                                <div className="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-xl border border-slate-100 py-2 z-50">
                                    <div className="px-4 py-2 border-b border-slate-100">
                                        <h3 className="text-sm font-semibold text-slate-900">Notifications</h3>
                                    </div>
                                    <div className="max-h-96 overflow-y-auto">
                                        <a href="#" className="block px-4 py-3 hover:bg-slate-50 transition-colors duration-200">
                                            <div className="flex items-start">
                                                <div className="flex-shrink-0">
                                                    <span className="inline-block p-2 bg-indigo-100 rounded-lg">
                                                        <svg className="w-5 h-5 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                                        </svg>
                                                    </span>
                                                </div>
                                                <div className="ml-3 w-0 flex-1">
                                                    <p className="text-sm font-medium text-slate-900">New job listings available</p>
                                                    <p className="mt-1 text-sm text-slate-500">15 new positions added in the last hour</p>
                                                    <p className="mt-1 text-xs text-slate-400">2 minutes ago</p>
                                                </div>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="flex items-center space-x-4 border-l border-slate-200 pl-6">
                            <img 
                                src="https://ui-avatars.com/api/?name=John+Doe&background=0694a2&color=fff" 
                                alt="User Avatar" 
                                className="w-9 h-9 rounded-lg shadow-sm" 
                            />
                            <div className="flex flex-col">
                                <span className="text-sm font-medium text-slate-700">John Doe</span>
                                <span className="text-xs text-slate-500">Administrator</span>
                            </div>
                        </div>
                    </div>
                </header>

                {/* Content Area */}
                <main className="flex-1 p-6 overflow-auto">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}