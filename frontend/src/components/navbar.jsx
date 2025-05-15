import React from 'react'
import {Link} from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
const Navbar = () =>{

    const [logged, setLogged] = useState(false);

    const forceRerender = () => {
        setRefresh(prev => !prev);
    }

    const navigate = useNavigate();

    checkLogin();

    return (
        <nav style={{borderBottom:'1px solid lightblue', marginBottom:'3px'}} className='nav justify-content-center'>
            <Link to='/' className="nav-link">Home</Link>
            <Link to='/myProfile' className="nav-link">myProfile</Link>
            <Link to='/leaderboards' className="nav-link">LeaderBoards</Link>
            {!logged?(<Link to='/login' className="nav-link">LogIn</Link>):(<Link to='/home' className='nav-link' onClick={logOut}>LogOut</Link>)}

        </nav>
    )

    function logOut(){
        fetch('http://localhost:8080/t/logout')
        .then(res => {
            setLogged(false);
            navigate('/home');
        })
    }
    async function checkLogin(){
        let res = false;
        await fetch('http://localhost:8080/t/checkLogin2')
        .then(res => res.json())
        .then(data=> {
            res = data.result=='OK'?true:false});
        if(res) setLogged(true)
    }
    
    
}




export default Navbar;