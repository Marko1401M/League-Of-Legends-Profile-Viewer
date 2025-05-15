import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Button from 'react-bootstrap/Button'
import Form1 from './pages/home'
import Profile from './pages/profile'
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Navbar from './components/navbar'
import Match from './pages/match'
import LogIn from './pages/login'
import MyProfile from './pages/myProfile'
import Register from './pages/register'
import LeaderBoards from './pages/leaderboards'
import LeaderBoard from './pages/leaderboard'
function App() {
  return( 

    <Router>
      <Navbar/>
      <div >
        <Routes>
          <Route path='/' element={<Form1/>} />
          <Route path = '/profile' element = {<Profile/>} />
          <Route path='/home' element = {<Form1/>}/>
          <Route path='/match' element = {<Match/>} />
          <Route path='/login' element = {<LogIn/>}/>
          <Route path='/register' element = {<Register/>}/>
          <Route path ='/myProfile' element ={<MyProfile/>}/>
          <Route path='/leaderboards' element = {<LeaderBoards/>}/>
          <Route path='/leaderboard' element = {<LeaderBoard/>}/>
        </Routes>
      </div>
    </Router>
  )
}

export default App
