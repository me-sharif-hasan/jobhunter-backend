import { createHashRouter } from "react-router-dom";
import {route_map, RouterConfig} from "../routing.ts";

// Transform route_map to React Router format
export const router = createHashRouter(
    route_map.map((route: RouterConfig) => ({
        path: route.url,
        element: route.component ? <route.component /> : null,
        children: route.children?.map((child: RouterConfig) => ({
            path: child.url,
            element: child.component ? <child.component /> : null
        }))
    }))
);