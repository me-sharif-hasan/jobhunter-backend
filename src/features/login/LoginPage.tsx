import CustomGoogleLoginButton from "./component/CustomGoogleLoginButton.tsx";
import {GoogleOAuthProvider} from "@react-oauth/google";
import dicontainer from "../../utility/ioc_registry.ts";
import LoginController from "./controller/LoginController.ts";
import {useEffect} from "react";

export default function LoginPage() {
    const loginController = dicontainer.get(LoginController);
    useEffect(()=>{
        loginController.getUserInfo();
    },[])
    return <>
        <GoogleOAuthProvider clientId={"742827419889-pigebjtdbe1vuu15dgl5808o5jd7ink3.apps.googleusercontent.com"}>
            <CustomGoogleLoginButton/>
        </GoogleOAuthProvider>
    </>
}