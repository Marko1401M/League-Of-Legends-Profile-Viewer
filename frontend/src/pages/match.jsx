import 'bootstrap/dist/css/bootstrap.min.css'
import React, {useEffect, useState} from 'react'
import {useNavigate} from 'react-router-dom'


function Match(){
    const [summonerData, setSummonerData] = useState(null);

    const [matchData, setMatchData] = useState(null);

    useEffect( () => {
        const fetchData = async () => {
            try{
                const f1 = await fetch('http://localhost:8080/t/lol/get_account');
                const d1 = await f1.json();
                        
                const f2 = await fetch('http://localhost:8080/t/lol/ranked_summoner/' + d1.gameName + '/' + d1.tagLine);
                const d2 = await f2.json();
                       
                const f3 = await fetch('http://localhost:8080/t/lol/get_match_details');
                const d3 = await f3.json();
        
                setSummonerData(d2);
        
                setMatchData(d3);
                
                //showData(d2, d3);
                console.log(d3);
            }catch(error){
                console.log(error);
            }
            
        }
        fetchData();

    },[]);

    const navigate = useNavigate();

    const viewProfile = async (gameName, tagLine) => {
        
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

   
    return (
        <div>
        {matchData? (<div><span style={{clear:'left'}}>Duration {Math.round(matchData.info.gameDuration / 60) } : {matchData.info.gameDuration % 60}</span>
        <div className='container'>
            {showData(summonerData,matchData, viewProfile)}
        </div></div>) : <div>Loading...</div>}
        </div>
    )
}   

function showData(summonerData, matchData, viewProfile){
    const bansBlue = matchData.info.teams[0].bans;
    const baronsBlue = matchData.info.teams[0].objectives.baron.kills;
    const dragonsBlue = matchData.info.teams[0].objectives.dragon.kills;
    const towersBlue = matchData.info.teams[0].objectives.tower.kills;
    const winBlue = matchData.info.teams[0].win;

    const bansRed = matchData.info.teams[1].bans;
    const baronsRed = matchData.info.teams[1].objectives.baron.kills;
    const dragonsRed = matchData.info.teams[1].objectives.dragon.kills;
    const towersRed = matchData.info.teams[1].objectives.tower.kills;
    const winRed = matchData.info.teams[1].win;

    const playersMapBlue = matchData.info.participants.map((player, index) => {
        if(index < 5){
            return (
                <div className='container' style={{margin:'3px',border:'1px solid black', borderRadius:'8px',width:'300px',overflow:'hidden'}}>
                    <img style={{width:'50px',margin:'5px', float:'left'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + player.championId + '.png'}></img>
                    <span onClick={() => viewProfile(player.riotIdGameName, player.riotIdTagline )} className='gamePlayerLeft'>{player.riotIdGameName + '#' + player.riotIdTagline}</span><br></br>
                    <span>{player.kills + '/' + player.deaths + '/' + player.assists}</span>
                </div>
            )
        }
    })
    const playersMapRed = matchData.info.participants.map((player, index) => {
        if(index >= 5){
            return (
                <div className='container' style={{overflow:'hidden',border:'1px solid black', borderRadius:'8px',width:'300px',margin:'3px'}}>
                    <img style={{width:'50px', float:'left',margin:'5px'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + player.championId + '.png'}></img>
                    <span onClick={() => viewProfile(player.riotIdGameName, player.riotIdTagline )} className='gamePlayerLeft'>{player.riotIdGameName + '#' + player.riotIdTagline}</span><br></br>
                    <span>{player.kills + '/' + player.deaths + '/' + player.assists}</span>
                </div>
            )
        }
    })
    console.log(bansBlue)
    const bansBlueMap = bansBlue.map((ban) => {
        return (
            <img style={{width:'25px', float:'left',margin:'5px',border:'1px solid red'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + ban.championId + '.png'} ></img>
        )
    })
    const bansRedMap = bansRed.map((ban) => {
        return (
            <img style={{width:'25px', float:'left',margin:'5px',border:'1px solid red'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + ban.championId + '.png'} ></img>
        )
    })
    return (
        <div className='container'>
            <div id='blueTeam' className='container' style={{padding:'10px',overflow:'hidden',float:'left',clear:'left',width:'350px',border:'1px solid blue', borderRadius:'8px'}}>
                <h4 style={winBlue?({color:'green'}):({color:'red'})}>{winBlue?('Won'):('Lost')}</h4>
                {playersMapBlue}
                <div className='container'>
                    <h6>Barons:<span> {baronsBlue}</span></h6>
                    <h6>Towers:<span> {towersBlue}</span></h6>
                    <h6>Dragons:<span> {dragonsBlue}</span></h6>
                    <h6>Bans:</h6>
                    {bansBlueMap}
                </div>
            </div>
            <div id='redTeam' className='container' style={{padding:'10px',overflow:'hidden',width:'350px',border:'1px solid red', borderRadius:'8px'}}>
                <h4 style={winRed?({color:'green'}):({color:'red'})}>{winRed?('Won'):('Lost')}</h4>
                {playersMapRed}
                <div className='container'>
                    <h6>Barons:<span> {baronsRed}</span></h6>
                    <h6>Towers:<span> {towersRed}</span></h6>
                    <h6>Dragons:<span> {dragonsRed}</span></h6>
                    <h6>Bans:</h6>
                    {bansRedMap}
                </div>
            </div>
        </div>
    )
}

export default Match;