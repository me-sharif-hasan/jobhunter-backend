import {GoogleLogin} from "@react-oauth/google";
import '../login.css'
import dicontainer from "../../../utility/ioc_registry.ts";
import LoginController from "../controller/LoginController.ts";
import {useNavigate} from "react-router-dom";

export default function CustomGoogleLoginButton(){
    const loginController = dicontainer.get(LoginController);
    const navigate = useNavigate();
    return <>
        <GoogleLogin onSuccess={credentialResponse=>{
            if(credentialResponse){
                loginController.login(credentialResponse.credential??"").then(
                    ()=>{
                        navigate("/dashboard");
                    }
                )
            }
        }
        } useOneTap/>
    </>
}