import 'bootstrap/dist/css/bootstrap.min.css'
import React, {useEffect, useState} from 'react'
import {useNavigate} from 'react-router-dom'

function LeaderBoard(){
    const [lead, setLead] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/t/get_leaderboard')
        .then(res => res.json())
        .then(data => {console.log(data);setLead(data)});
    },[])

    const navigate = useNavigate();
    

    function viewProfile(gameName, tagLine){
        fetch('http://localhost:8080/t/lol/account/' + gameName + '/' + tagLine)
        .then(res=>res.json())
        .then(data=> {
                if(!data){}
                else {
                    navigate('/profile')
                }
            }
        )
    }

    const showLead = () => {
        return lead.map((element, index) =>{
            return (
                <div className='container-fluid' style={{borderRadius:'8px',overflow:'hidden',backgroundColor:index==0?'gold':index==1?'silver':index=='2'?'brown':'lightgray'}}>
                    <span style={{width:'15px', float:'left',margin:'2px',marginTop:'20px'}}>{index + 1}.</span>
                    <img style={{width:'50px',margin:'6px',float:'left',border:'1px solid lightblue'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/' + element.summonerIconId + '.jpg'}></img>
                    <span style={{color:'blue',display:'inline-block',width:'284px'}}>{element.gameName}#{element.tagLine}</span>
                    <button className='btn btn-primary' onClick={() => viewProfile(element.gameName, element.tagLine)} style={{fontSize:'15px',padding:'3px'}}>View profile</button><br></br>
                    <span style={{fontSize:'15px'}}>{element.tier} {element.rank} {element.leaguePoints}lp</span>
                </div>
            )
        })
    }

    return lead?
    (
    <div className='container-fluid' style={{width:'500px',fontSize:'20px'}}>
        {showLead()}
    </div>
    )
    :
    (
    <div>Loading...</div>
);
}

export default LeaderBoard;