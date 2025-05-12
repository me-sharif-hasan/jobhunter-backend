import GoogleAuthDatasource from "../datasource/GoogleAuthDatasource.ts";
import {inject,injectable} from "inversify";
import {UserService} from "../../common/user/domain/UserService.ts";

@injectable()
export default class LoginController {
    constructor(
        @inject(GoogleAuthDatasource) private googleAuthDatasource:GoogleAuthDatasource,
        @inject(UserService) private userService:UserService,
    ){}

    async login(googleAuthToken:string){
        const {data} = await this.googleAuthDatasource.printUserInfo(googleAuthToken);
        if(data.success){
            localStorage.setItem("_jobhunteradmintoken", data.data);
        }else{
            localStorage.removeItem("_jobhunteradmintoken");
            throw new Error(data.message);
        }
    }

    async getUserInfo(){
        try {
            return await this.userService.getLoggedInUser();
        }catch(error){
            throw new Error("Could not log in");
        }
    }
}