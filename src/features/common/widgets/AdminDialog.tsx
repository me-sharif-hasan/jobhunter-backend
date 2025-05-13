import {Dialog} from "primereact/dialog";
import {ReactNode, useState} from "react";
import {Button} from "primereact/button";

type AdminDialogProps = {
    children: ReactNode;
    showModalButtonText?: string;
    hideModalButtonText?: string;
    visible?: boolean;
    onHide?: () => void;
    showButton?: boolean;
};

export default function AdminDialog({
    children, 
    showModalButtonText='Show Modal', 
    visible: externalVisible,
    onHide: externalOnHide,
    showButton = true
}: AdminDialogProps) {
    const [internalShowModal, setInternalShowModal] = useState(false);
    
    const showModal = externalVisible !== undefined ? externalVisible : internalShowModal;
    const hideModal = externalOnHide || (() => setInternalShowModal(false));

    return <>
        {showButton && (
            <Button disabled={showModal} onClick={() => setInternalShowModal(true)} type="button">
                {showModalButtonText}
            </Button>
        )}
        <Dialog 
            header="Header" 
            visible={showModal} 
            style={{ width: '50vw' }} 
            onHide={hideModal}
            closable={true}
        >
            {children}
        </Dialog>
    </>
}