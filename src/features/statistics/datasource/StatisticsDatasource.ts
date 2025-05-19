import { injectable } from 'inversify';

export interface JobStatistics {
    totalApplications: number;
    acceptedApplications: number;
    rejectedApplications: number;
    pendingApplications: number;
    applicationsByCompany: { [key: string]: number };
    applicationsByMonth: { [key: string]: number };
    successRate: number;
}

export interface BusinessAnalytics {
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

@injectable()
export class StatisticsDatasource {
    async getJobStatistics(): Promise<JobStatistics> {
        // Dummy data
        return {
            totalApplications: 150,
            acceptedApplications: 15,
            rejectedApplications: 45,
            pendingApplications: 90,
            applicationsByCompany: {
                'Google': 10,
                'Microsoft': 8,
                'Amazon': 12,
                'Meta': 7,
                'Apple': 9,
                'Others': 104
            },
            applicationsByMonth: {
                'Jan 2025': 20,
                'Feb 2025': 25,
                'Mar 2025': 30,
                'Apr 2025': 35,
                'May 2025': 40
            },
            successRate: 10 // 10%
        };
    }

    async getBusinessAnalytics(): Promise<BusinessAnalytics> {
        // Generate realistic dummy data
        const today = new Date();
        const userRegistrations = Array.from({ length: 30 }, (_, i) => {
            const date = new Date();
            date.setDate(today.getDate() - (29 - i));
            return {
                date: date.toISOString().split('T')[0],
                count: Math.floor(Math.random() * 20) + 10 // 10-30 new users per day
            };
        });

        const activeUsers = Array.from({ length: 30 }, (_, i) => {
            const date = new Date();
            date.setDate(today.getDate() - (29 - i));
            return {
                date: date.toISOString().split('T')[0],
                users: Math.floor(Math.random() * 100) + 150 // 150-250 active users per day
            };
        });

        const usageTime = Array.from({ length: 24 }, (_, hour) => ({
            hour,
            avgMinutes: Math.floor(Math.random() * 30) + 
                       (hour >= 9 && hour <= 17 ? 30 : 10) // More usage during work hours
        }));

        return {
            userRegistrations,
            activeUsers,
            usageTime,
            growthRate: 15.5, // 15.5% month-over-month growth
            totalUsers: 2500,
            avgDailyUsers: 180
        };
    }
}