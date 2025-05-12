import './App.css'
import {RouterProvider} from "react-router-dom";
import './App.css'
import {router} from "./config/routing_widget/RecursiveRouterRenderer.tsx";
import 'primeicons/primeicons.css';
import 'primereact/resources/primereact.min.css';

function App() {

  return (
    <>
        <RouterProvider router={router} />
    </>
  )
}

export default App
