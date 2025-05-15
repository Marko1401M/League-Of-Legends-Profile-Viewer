import 'bootstrap/dist/css/bootstrap.min.css'
import React, {useEffect, useState} from 'react'
import {useNavigate} from 'react-router-dom'


function MyProfile(){

    const [user, setUser] = useState(null);

    const [summoner, setSummoner] = useState(null);

    const navigate = useNavigate();

    useEffect(()=>{
        fetch('http://localhost:8080/t/checkLogin2')
        .then(res => res.json())
        .then(data =>{
            if(data.result == 'OK'){
                fetch('http://localhost:8080/t/checkLogin')
                .then(res => res.json())
                .then(data => {
                    setUser(data);
                    console.log(data);
                    fetch('http://localhost:8080/t/lol/summoner/' + data.player.gameName + '/' + data.player.tagLine)
                    .then(res => res.json())
                    .then(data => {console.log(data);setSummoner(data)});
                })
                
            }
        })
    },[])   

    function showProfile(){
        fetch('http://localhost:8080/t/lol/account/' + user.player.gameName + '/' + user.player.tagLine)
        .then(res=>res.json())
        .then(
            data=> {
                if(!data);
                else {
                    navigate('/profile')
                }
                
            }
        )
    }

    return (
        user?
        (<div className='container-fluid'>
            <div className='container-fluid bg-dark' style={{width:'750px', padding:'5px', overflow:'hidden'}}>
                {summoner?
                (   
                    <>
                        <img style={{width:'75px',float:'left',marginRight:'5px'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/' + summoner.summonerIconId + '.jpg'}></img>
                        <span style={{color:'blueviolet',fontSize:'25px'}}>{summoner.gameName}<span style={{color:'lightgray'}}>#</span>{summoner.tagLine}</span><br></br>
                        <button onClick={() => showProfile()} className='btn btn-primary'>View profile</button>
                    </>
                )
                :
                (<div>Loading...</div>)}
                
            </div>
            <div className='container-fluid bg-dark'>
                
            </div>
        </div>)
        :
        (<div>Not Logged in</div>)
    );
}



export default MyProfile;