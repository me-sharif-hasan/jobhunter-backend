import { inject, injectable } from 'inversify';
import { StatisticsDatasource, JobStatistics, BusinessAnalytics } from '../datasource/StatisticsDatasource';

@injectable()
export class StatsController {
    constructor(
        @inject(StatisticsDatasource) private statisticsDatasource: StatisticsDatasource
    ) {}

    async getStatistics(): Promise<JobStatistics> {
        return await this.statisticsDatasource.getJobStatistics();
    }

    async getBusinessAnalytics(): Promise<BusinessAnalytics> {
        return await this.statisticsDatasource.getBusinessAnalytics();
    }
}