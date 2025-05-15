import 'bootstrap/dist/css/bootstrap.min.css'
import React, {useEffect, useState} from 'react'
import {useNavigate} from 'react-router-dom'

function LeaderBoards(){

    const [leads, setLeads] = useState(null);

    const [user, setUser] = useState(null);

    const [formData, setFormData] = useState({
        leadName:'',
    });

    const [refresh, setRefresh] = useState(true);

    const navigate = useNavigate();

    //TODO
    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8080/t/create_leaderboard/' + user.username + '/' + formData.leadName)
        .then(res=>res.json())
        .then(
            data=> {
                if(data.result == 'OK'){
                    fetch('http://localhost:8080/t/get_leaderboards')
                    .then(res => res.json())
                    .then(data => {
                        console.log(data);
                        setLeads(data); 
                    })
                    
                    //navigate('/leaderboards')
                }
                else {
                    alert('Error!\nTry again');
                }
                
            }
        )
    }
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
          ...prevData,
          [name]: value,
        }));
    };


    useEffect(() =>{
        fetch('http://localhost:8080/t/get_leaderboards')
        .then(res => res.json())
        .then(data => {
            console.log(data);
            setLeads(data); 
        })

        fetch('http://localhost:8080/t/checkLogin2')
        .then(res => res.json())
        .then(data=>{
            if(data.result == 'OK'){
                fetch('http://localhost:8080/t/checkLogin')
                .then(res => res.json())
                .then(data => setUser(data));
            }
        })
    },[])

    const showLeads = () =>{
        return leads.map((lead)=>{
            return(
                <div style={{border:'1px solid lightblue',margin:'5px',borderRadius:'8px'}} className='container'>
                    <span className='text' style={{width:'75px',display:'inline-block'}}>{lead.name}</span>
                    <button onClick={() => viewLead(lead.id)} className='btn' style={{backgroundColor:'lightyellow', border:'1px solid lightblue',width:'60px',margin:'3px'}}>View</button>
                    {user?
                    (<button onClick={() => joinLead(lead.id)} className='btn' style={{backgroundColor:'lightgreen', border:'1px solid lightblue',margin:'3px',width:'60px'}}>Join</button>)
                    :
                    (<></>)    
                }
                    <span style={{width:'200px',display:'inline-block'}}>Created by: {lead.createdBy}</span>
                </div>
                
            )
        })
    }

    const joinLead = (id) => {
        fetch('http://localhost:8080/t/join_leaderboard/' + user.username + '/' + id)
    }

    const viewLead = (id) =>{
        fetch('http://localhost:8080/t/set_leaderboard/' + id)
        .then(() => {
                navigate('/leaderboard')
            }
        )
    }

    return (
        <div>
            <div className='container-fluid' style={{overflow:'hidden',border:'1px solid lightblue',borderRadius:'8px',width:'350px'}}>
                <form onSubmit={handleSubmit}>
                    <label htmlFor='leadName' className='form-label text-secondary'>Input the name for the new leaderboard</label>
                    <input type='text' className='form-control' id='leadName' name='leadName' value={formData.leadName} onChange={handleChange} required></input>
                    
    
                    <button type='submit' className='btn btn-primary' style={{margin:'5px 5px 5px 0'}}>Create</button>
                    
                </form>
            </div>
            {leads?
            (<div>{showLeads()}</div>)
            :
            (<div>Loading...</div>)}
        </div>
    )
}

export default LeaderBoards;