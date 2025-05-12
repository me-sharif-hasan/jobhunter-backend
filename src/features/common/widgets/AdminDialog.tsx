import {Dialog} from "primereact/dialog";
import {ReactNode, useState} from "react";
import {Button} from "primereact/button";

export default function AdminDialog({children, showModalButtonText='Show Modal', hideModalButtonText='Hide Modal'}: {children: ReactNode,showModalButtonText:string,hideModalButtonText:string}) {
    const [showModal, setShowModal] = useState(false);
    return<>
        <Button onClick={()=>setShowModal(true)} type={"button"}>{showModal?showModalButtonText:hideModalButtonText}</Button>
        <Dialog header="Header" visible={showModal} style={{ width: '50vw' }} onHide={() => {setShowModal(false) }}>
            {children}
        </Dialog>
    </>
}