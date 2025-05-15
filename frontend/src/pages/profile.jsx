import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom'



function Profile(){

    const [data, setData] = useState(null)

    const [rankedSummoner, setRankedSummoner] = useState(null)

    const [matchHistory, setMatchHistory] = useState(null)

    

    let matchMap;
    
    const navigate = useNavigate();



    useEffect(() => {
        const fetchData = async () => {
            try{
                
                const f1 = await fetch('http://localhost:8080/t/lol/get_account');
                const d1 = await f1.json();
                
                if(f1.status != 200) {
                    navigate('/home')
                    return;
                }
                
                
                const f2 = await fetch('http://localhost:8080/t/lol/ranked_summoner/' + d1.gameName + '/' + d1.tagLine);
                const d2 = await f2.json();
            
                const f3 = await fetch('http://localhost:8080/t/lol/get_match_history/' + d1.puuid)
                const d3 = await f3.json();

                console.log(d3);

                setMatchHistory(d3);
                setData(d1);
                setRankedSummoner(d2);
                
                document.title = d1.gameName + '#' + d1.tagLine + `'s Profile`;
                
                
            } catch(error){
                console.error(error);
            };
        }
        fetchData();
    },[])
    
    const showNextProfile = async (gameName, tagLine) => {  
        setMatchHistory(null);
        setData(null);
        setRankedSummoner(null);

        const t1 = await fetch('http://localhost:8080/t/lol/account/' + gameName + '/' + tagLine)

        const f1 = await fetch('http://localhost:8080/t/lol/get_account');
        const d1 = await f1.json();
        
        const f2 = await fetch('http://localhost:8080/t/lol/ranked_summoner/' + d1.gameName + '/' + d1.tagLine);
        const d2 = await f2.json();
        
        const f3 = await fetch('http://localhost:8080/t/lol/get_match_history/' + d1.puuid)
        const d3 = await f3.json();

        setMatchHistory(d3);
        setData(d1);
        setRankedSummoner(d2);
        
        document.title = d1.gameName + '#' + d1.tagLine + `'s Profile`;
    }
    
    const showMatchData = async(matchId) => {
        const t1 = await fetch('http://localhost:8080/t/lol/set_match_id/' + matchId);
        navigate('/match');
    }

   

    let img = 'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/' + rankedSummoner?.summonerIconId + '.jpg';
    return (
        <div className="container mt-4">
            {data && rankedSummoner ? (
                <div className='container-fluid'>
                    <div className='container bg-dark' style={{overflow:'hidden', padding:'5px'}}>
                        <div className='container' style={{overflow:'hidden',width:'350px',float:'left'}}>
                            <img className='img rounded' style={{width:'100px', float:'left',marginRight:'5px'}} src = {img}></img>
                            <span className='text-info'>gameName: {data?.gameName}</span><br></br>
                            <span className='text-info'>tagLine: {data?.tagLine}</span><br></br>
                            <span className='text-info'>level: {rankedSummoner?.level}</span>
                        </div>
                        <div className='container'>
                            <img style={{width:'100px',float:'left',marginRight:'5px'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-shared-components/global/default/' + rankedSummoner.tier.toLowerCase() + '.png'}></img>
                            <span className='text-info'>Tier: {rankedSummoner.tier}</span><br></br>
                            <span className='text-info'>Rank: {rankedSummoner.rank}</span><br></br>
                            <span className='text-info'>LP: {rankedSummoner.leaguePoints}</span>
                        </div>
                    </div> 
        
                    <div style={{padding:'0'}} className='container' id='matchHistory'>
                        {matchHistory && data?(<div>{loadMatchHistory(matchHistory,data, showNextProfile, showMatchData)}</div>):(<div>Loading...</div>)}
                    </div>

                </div>
            ) : (
            <p>Loading profile...</p>
            )}
        </div>
    );

}

function loadMatchHistory(matchHistory, data, showNextProfile, showMatchData){
        
        let matchMap = matchHistory.map((match) => {
        let win = false;
        let champId = '';
        let champName = ''
        let items = []
        let kills = 0;
        let assists = 0;
        let deaths = 0;
        match.playerStats.forEach(element =>{
            if(element.puuid == data.puuid){
                kills = element.kills;
                assists = element.assists;
                deaths = element.deaths;
                if(element.win) win = true;
                champId = element.championId;
                champName = element.championName;
                element.items.forEach(el => {
                    items.push(el);
                })
            }
        });
        let playersMapLeft = match.playerStats.map((player, index) => {
            
            if(index < 5) return (<div onClick={() => showNextProfile(player.gameName,player.tagLine)} className='gamePlayerLeft' key={player.gameName} style={{width:'250px'}}>
                <img style={{width:'25px'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + player.championId + '.png'}></img>
                <span>{player.gameName + '#' + player.tagLine}</span>
            </div>)

        })
        let playersMapRight = match.playerStats.map((player, index) => {
            if(index>=5) return (<div onClick={() => showNextProfile(player.gameName,player.tagLine)} className='gamePlayerRight' key={player.gameName} style={{width:'250px'}}>
                <img style={{width:'25px'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + player.championId + '.png'}></img>
                <span>{player.gameName + '#' + player.tagLine}</span>
            </div>)
        })
        let itemsMap = items.map((item) =>{
            return (
                <img style={{width:'25px'}} key={item} src={'https://static.bigbrain.gg/assets/lol/riot_static/15.1.1/img/item/' + item + '.png'}></img>
            )
        });
        console.log(champId);
        return (<div key={match.matchId} style={{overflow:'hidden',padding:'5px',marginTop:'5px'}} className={win?'container bg-success':'container bg-danger'}>
            <img style={{float:'left', width:'75px'}} src={'https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/' + champId + '.png'} ></img>
            <div className='container' id='stats'>
                <span>{kills}/{deaths}/{assists}</span>
            </div>
            <div className='container' style={{width:'200px',overflow:'hidden',float:'left'}}>
            {itemsMap}
            </div>
            <div style={{float:'left',width:'300px'}} className='container'>
                {playersMapLeft}
            </div>
            <div style={{width:'300px',overflow:'hidden'}} className='container'>
                {playersMapRight}
            </div>
            <button onClick={() => showMatchData(match.matchId)} className='btn btn-primary'>Show Match Details</button>
        </div>)
        
    })
    console.log(matchMap)
    return matchMap;
}


export default Profile;