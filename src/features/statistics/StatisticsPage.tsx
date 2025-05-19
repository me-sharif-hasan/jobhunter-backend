import React, { useEffect, useState } from 'react';
import { Card } from 'primereact/card';
import { ProgressSpinner } from 'primereact/progressspinner';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, PieChart, Pie, Cell, LineChart, Line, Legend, ResponsiveContainer } from 'recharts';
import { StatsController } from './controller/StatsController';
import { JobStatistics } from './datasource/StatisticsDatasource';
import dicontainer from '../../utility/ioc_registry';

const COLORS = ['#00C4B4', '#775DD0', '#FF9900', '#FF4560', '#008FFB', '#FEB019'];

const StatCard: React.FC<{ title: string; value: string | number; color: string; subtitle?: string }> = ({ title, value, color, subtitle }) => (
    <Card className="h-full">
        <div className="flex flex-column align-items-center text-center">
            <h2 className="text-xl font-bold mb-2" style={{ color }}>{value}</h2>
            <h3 className="text-lg mb-2">{title}</h3>
            {subtitle && <p className="text-sm text-500">{subtitle}</p>}
        </div>
    </Card>
);

interface BusinessAnalytics {
    userRegistrations: {
        date: string;
        count: number;
    }[];
    activeUsers: {
        date: string;
        users: number;
    }[];
    usageTime: {
        hour: number;
        avgMinutes: number;
    }[];
    growthRate: number;
    totalUsers: number;
    avgDailyUsers: number;
}

export const StatisticsPage: React.FC = () => {
    const statsController = dicontainer.get<StatsController>(StatsController);
    const [stats, setStats] = useState<JobStatistics | null>(null);
    const [analytics, setAnalytics] = useState<BusinessAnalytics | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const jobStats = await statsController.getStatistics();
                const businessAnalytics = await statsController.getBusinessAnalytics();
                setStats(jobStats);
                setAnalytics(businessAnalytics);
            } catch (error) {
                console.error('Error fetching statistics:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, [statsController]);

    if (loading) {
        return (
            <div className="flex justify-content-center align-items-center min-h-screen">
                <ProgressSpinner />
            </div>
        );
    }

    if (!stats || !analytics) {
        return (
            <div className="text-center text-red-500 text-xl">
                Error loading statistics
            </div>
        );
    }

    const companyData = Object.entries(stats.applicationsByCompany).map(([name, value]) => ({
        name,
        value,
    }));

    return (
        <div className="p-4">
            <h1 className="text-3xl font-bold mb-4">Business Analytics & Statistics</h1>
            
            {/* Job Application Stats */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
                <StatCard
                    title="Total Applications"
                    value={stats.totalApplications}
                    color="#008FFB"
                    subtitle="All time applications"
                />
                <StatCard
                    title="Accepted"
                    value={stats.acceptedApplications}
                    color="#00C4B4"
                    subtitle={`${((stats.acceptedApplications / stats.totalApplications) * 100).toFixed(1)}% success rate`}
                />
                <StatCard
                    title="Rejected"
                    value={stats.rejectedApplications}
                    color="#FF4560"
                    subtitle="Need improvement"
                />
                <StatCard
                    title="Pending"
                    value={stats.pendingApplications}
                    color="#FEB019"
                    subtitle="Awaiting response"
                />
            </div>

            {/* Business Growth Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                <StatCard
                    title="Total Users"
                    value={analytics.totalUsers}
                    color="#775DD0"
                    subtitle="Platform users"
                />
                <StatCard
                    title="Daily Active Users"
                    value={analytics.avgDailyUsers}
                    color="#008FFB"
                    subtitle="30-day average"
                />
                <StatCard
                    title="Growth Rate"
                    value={`${analytics.growthRate}%`}
                    color="#00C4B4"
                    subtitle="Month over month"
                />
            </div>

            {/* Charts Section */}
            <div className="grid">
                {/* User Registration Trend */}
                <div className="col-12 lg:col-6 mb-4">
                    <Card title="User Registration Trend">
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart data={analytics.userRegistrations}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="date" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Line type="monotone" dataKey="count" stroke="#775DD0" name="New Users" />
                            </LineChart>
                        </ResponsiveContainer>
                    </Card>
                </div>

                {/* Active Users Over Time */}
                <div className="col-12 lg:col-6 mb-4">
                    <Card title="Active Users Trend">
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart data={analytics.activeUsers}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="date" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Line type="monotone" dataKey="users" stroke="#008FFB" name="Active Users" />
                            </LineChart>
                        </ResponsiveContainer>
                    </Card>
                </div>

                {/* App Usage by Hour */}
                <div className="col-12 lg:col-6 mb-4">
                    <Card title="App Usage by Hour">
                        <ResponsiveContainer width="100%" height={300}>
                            <BarChart data={analytics.usageTime}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="hour" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Bar dataKey="avgMinutes" fill="#00C4B4" name="Avg. Minutes" />
                            </BarChart>
                        </ResponsiveContainer>
                    </Card>
                </div>

                {/* Applications by Company */}
                <div className="col-12 lg:col-6 mb-4">
                    <Card title="Applications by Company">
                        <ResponsiveContainer width="100%" height={300}>
                            <PieChart>
                                <Pie
                                    data={companyData}
                                    cx="50%"
                                    cy="50%"
                                    labelLine={false}
                                    label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}
                                    outerRadius={100}
                                    fill="#8884d8"
                                    dataKey="value"
                                >
                                    {companyData.map((_, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip />
                            </PieChart>
                        </ResponsiveContainer>
                    </Card>
                </div>
            </div>
        </div>
    );
};