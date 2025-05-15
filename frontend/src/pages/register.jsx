import 'bootstrap/dist/css/bootstrap.min.css'
import React, {useState} from 'react'
import {useNavigate} from 'react-router-dom'

function Register(){
    const [formData, setFormData] = useState({
        username:'',
        password:'',
        gameName:'',
        tagLine:'',
    });

    const navigate = useNavigate();

    const handleSubmit = (e) =>{
        e.preventDefault();
        register();
    }

    const register = () => {
        fetch('http://localhost:8080/t/register/' + formData.username + '/' + formData.password + '/' + formData.gameName + '/' + formData.tagLine,{
            method:'POST',
        })
        .then(res => res.json())
        .then(data => {
            console.log(data);
            if(data.result == 'OK'){
                navigate('/home');
            }
            else {
                alert('Error!\nTry again!');
            }
        })
    }

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
          ...prevData,
          [name]: value,
        }));
    };

    return (
        <div className='container bg-dark border-top-5' style={{padding:'10px', borderRadius:'8px', width:'400px'}}>
        <h3 className='text-primary' style={{textAlign:'center'}}>Register a new account!</h3>
        <form onSubmit={handleSubmit}>
            <label htmlFor='username' className='form-label text-secondary'>Input Your Username</label>
            <input type='text' className='form-control' id='username' name='username' value={formData.username} onChange={handleChange} required></input>
            
            <label htmlFor='password' className='form-label text-secondary'>Input Your Password</label>
            <input type='password' className='form-control' id='password' name='password' value={formData.password} onChange={handleChange} required></input>
            
            <label htmlFor='gameName' className='form-label text-secondary'>Input Your GameName</label>
            <input type='text' className='form-control' id='gameName' name='gameName' value={formData.gameName} onChange={handleChange} required></input>
            
            <label htmlFor='tagLine' className='form-label text-secondary'>Input Your TagLine</label>
            <input type='text' className='form-control' id='tagLine' name='tagLine' value={formData.tagLine} onChange={handleChange} required></input>
            

            <button type='submit' className='btn btn-primary' style={{marginTop:'5px'}}>Register</button>
        </form>
    </div>
    )

}

export default Register;