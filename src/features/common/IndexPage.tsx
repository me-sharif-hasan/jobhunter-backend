import { useNavigate} from "react-router-dom";
import {useEffect} from "react";
import dicontainer from "../../utility/ioc_registry.ts";
import {UserService} from "./user/domain/UserService.ts";

export default function IndexPage(){
    const navigate = useNavigate();
    const userService = dicontainer.get<UserService>(UserService);
    useEffect(()=>{
        userService.getLoggedInUser().then(
            (user)=>{
                if(user.email){
                    navigate("/dashboard");
                }else{
                    navigate("/login");
                }
            }
        ).catch(
            ()=>{
                navigate("/login");
            }
        );
    },[])
    return <></>
}