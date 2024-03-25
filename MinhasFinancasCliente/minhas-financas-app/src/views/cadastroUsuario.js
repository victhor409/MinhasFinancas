import React from "react";
import Card from "../components/card";
import FormGroup from "../components/form-group";


class CadastroUsuario extends React.Component {

    state = {
        nome:'',
        email:'',
        senha:'',
        senhaRepeticao: ''
    }

    cadastrar = () =>{
        console.log(this.state)
    }



    render() {
        return (

            <div className="container">
                <Card title="Cadastro de Usuarios">
                    <div className="row">
                        <div className="col-lg-12">
                            <div className="bs-component">
                                <FormGroup label="Nome *" htmlFor="inputName">
                                    <input  type="text" id="inputName" name="nome" className="form-control"
                                    onChange={e => this.setState({nome: e.target.value})}/>
                                </FormGroup>
                                <FormGroup label="Email *" htmlFor="inputEmail">
                                <input  type="text" id="inputEmail" name="email" className="form-control"
                                    onChange={e => this.setState({email: e.target.value})}/>
                                </FormGroup>
                                <FormGroup label="Senha *" htmlFor="inputSenha">
                                <input  type="password" id="inputSenha" name="senha" className="form-control"
                                    onChange={e => this.setState({senha: e.target.value})}/>
                                </FormGroup>
                                <FormGroup label="Repita a Senha *" htmlFor="inputRepitaSenha">
                                <input  type="password" id="inputRepitaSenha" name="senha" className="form-control"
                                    onChange={e => this.setState({senhaRepeticao: e.target.value})}/>
                                </FormGroup>
                                <button onClick={this.cadastrar}  className="btn btn-success">Entrar</button>
                                <button className="btn btn-danger">Cancelar</button>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>

        )
    }



}

export default CadastroUsuario