import React from 'react'
import 'bootswatch/dist/flatly/bootstrap.css'
import Rotas from './main/rotas'
import NavBar from './components/navbar'
import './custom.css'


class App extends React.Component {

  render() {
    return (
      <>
        <NavBar/>
    <div className='container'>
          
          <Rotas/>
    </div>
      </>
      
    )

  }

}

export default App;
