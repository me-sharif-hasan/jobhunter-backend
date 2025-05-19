import { Container } from "inversify";
import LoginController from "../features/login/controller/LoginController.ts";
import GoogleAuthDatasource from "../features/login/datasource/GoogleAuthDatasource.ts";
import {UserService} from "../features/common/user/domain/UserService.ts";
import SiteDatasource from "../features/sites/datasource/SiteDatasource.ts";
import SiteController from "../features/sites/controller/SiteController.ts";
import JobController from "../features/jobs/controller/JobController.ts";
import JobDatasource from "../features/jobs/datasource/JobDatasource.ts";
import SystemStatusDatasource from "../features/common/datasource/SystemStatusDatasource.ts";
import { StatsController } from "../features/statistics/controller/StatsController.ts";
import { StatisticsDatasource } from "../features/statistics/datasource/StatisticsDatasource.ts";

const dicontainer = new Container();
dicontainer.bind<LoginController>(LoginController).to(LoginController).inSingletonScope();
dicontainer.bind<GoogleAuthDatasource>(GoogleAuthDatasource).to(GoogleAuthDatasource).inSingletonScope();
dicontainer.bind<UserService>(UserService).to(UserService).inSingletonScope();
dicontainer.bind<SiteDatasource>(SiteDatasource).to(SiteDatasource).inSingletonScope();
dicontainer.bind<SiteController>(SiteController).to(SiteController).inSingletonScope();
dicontainer.bind<JobController>(JobController).to(JobController).inSingletonScope();
dicontainer.bind<JobDatasource>(JobDatasource).to(JobDatasource).inSingletonScope();
dicontainer.bind<SystemStatusDatasource>(SystemStatusDatasource).to(SystemStatusDatasource).inSingletonScope();
dicontainer.bind<StatsController>(StatsController).to(StatsController).inSingletonScope();
dicontainer.bind<StatisticsDatasource>(StatisticsDatasource).to(StatisticsDatasource).inSingletonScope();

export default dicontainer;