import 'bootstrap/dist/css/bootstrap.min.css'
import React, {useState} from 'react'
import {useNavigate} from 'react-router-dom'

function Form1(){
    const [formData, setFormData] = useState({
        gameName:'',
        tagLine:'',
    });

    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8080/t/lol/account/' + formData.gameName + '/' + formData.tagLine)
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
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
          ...prevData,
          [name]: value,
        }));
      };
    return (
        <div className='container bg-dark border-top-5' style={{padding:'10px', borderRadius:'8px', width:'400px'}}>
            <h3 className='text-primary' style={{textAlign:'center'}}>Check Your LoL Profile</h3>
            <form onSubmit={handleSubmit}>
                <label htmlFor='gameName' className='form-label text-secondary'>Input Your GameName</label>
                <input type='text' className='form-control' id='gameName' name='gameName' value={formData.gameName} onChange={handleChange} required></input>
                
                <label htmlFor='tagLine' className='form-label text-secondary'>Input Your tagLine</label>
                <input type='text' className='form-control' id='tagLine' name='tagLine' value={formData.tagLine} onChange={handleChange} required></input>
                
                <button type='submit' className='btn btn-primary' style={{marginTop:'5px'}}>Search</button>
            </form>
        </div>
    );
}

export default Form1;