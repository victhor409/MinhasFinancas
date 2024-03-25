import React from "react";

import {Route, Switch, HashRouter} from 'react-router-dom'
import Login from "../views/login/login";
import CadastroUsuario from "../views/cadastroUsuario";

function Rotas(){
    return(
        <HashRouter>
            <Switch>
                <Route path="/login" component={Login}/>
                <Route path="/cadastro-usuario" component={CadastroUsuario}/>
            </Switch>
        </HashRouter>
    )
}

export default Rotas