import { Outlet } from "react-router-dom";
import Header from "../components/Header/Header";

export default function MainLayout() {
    return (
        <div>
            <Header />
            <main>
                <Outlet />
            </main>
        </div>
    );
}