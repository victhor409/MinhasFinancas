import React from "react";
import NavBarItem from "./navBarItem";

function NavBar() {

    return (
        <div className="navbar navbar-expand-lg fixed-top navbar-dark bg-primary" >
            <div className="container">
                <a href="https://bootswatch.com/" className="navbar-brand">Minhas Finanças</a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarResponsive">
                    <ul className="navbar-nav">

                        <NavBarItem href="#/home" label="Home" />
                        <NavBarItem href="#/cadastro-usuario" label="Usuarios" />
                        <NavBarItem href="#/" label="Lancamentos" />
                        <NavBarItem href="#/login" label="Login" />

                    </ul>

                </div>
            </div>
        </div>


    )



}

export default NavBar