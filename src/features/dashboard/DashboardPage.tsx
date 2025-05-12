import { Outlet, NavLink } from "react-router-dom";
import {route_map} from "../../config/routing.ts";

export default function DashboardPage() {
    // Find the dashboard route to access its children
    const dashboardRoute = route_map.find(route => route.url === "/dashboard");
    const sidebarLinks = dashboardRoute?.children?.filter(child => child.title) || [];

    return (
        <div className="flex h-screen bg-gray-100 font-sans">
            {/* Sidebar */}
            <aside className="w-64 bg-white shadow-lg flex flex-col">
                <div className="p-4 border-b">
                    <h1 className="text-2xl font-bold text-teal-600">Dashboard</h1>
                </div>
                <nav className="flex-1 p-4">
                    {sidebarLinks.length > 0 && (
                        <div className="mb-6">
                            <h2 className="text-sm font-semibold text-gray-500 uppercase">Navigation</h2>
                            <ul className="mt-2 space-y-1">
                                {sidebarLinks.map(link => (
                                    <li key={link.url}>
                                        <NavLink
                                            to={`/dashboard/${link.url}`}
                                            className={({ isActive }) =>
                                                `block px-3 py-2 rounded-md ${
                                                    isActive
                                                        ? "bg-teal-100 text-teal-600"
                                                        : "text-gray-700 hover:bg-teal-100 hover:text-teal-600"
                                                }`
                                            }
                                        >
                                            {link.title}
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
                <header className="bg-white shadow-sm p-4 flex justify-between items-center">
                    <div className="flex items-center space-x-4">
                        <button className="text-gray-600 hover:text-teal-600 focus:outline-none">
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path>
                            </svg>
                        </button>
                        <h2 className="text-xl font-semibold ENGINEER-gray-800">Overview</h2>
                    </div>
                    <div className="flex items-center space-x-4">
                        <input
                            type="text"
                            placeholder="Search..."
                            className="px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
                        />
                        <div className="relative">
                            <button className="text-gray-600 hover:text-teal-600 focus:outline-none">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth="2"
                                        d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
                                    ></path>
                                </svg>
                            </button>
                        </div>
                        <div className="flex items-center space-x-2">
                            <img src="https://via.placeholder.com/32" alt="User Avatar" className="w-8 h-8 rounded-full" />
                            <span className="text-gray-700">John Doe</span>
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