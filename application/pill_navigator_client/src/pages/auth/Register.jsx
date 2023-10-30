import React, {useState} from "react";

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    console.log(username, password)
  }
  
  return (
    <div className={"container"}>
      <form className={"register-form"} onSubmit={handleSubmit}>
        <h3>Register</h3>
        
        <label className={"form-label"}>Username: </label>
        <input
          value={username}
          type={"text"}
          onChange={(e) => setUsername(e.target.value)}
          className={"form-input"}
        />
        
        <label className={"form-label"}>Password: </label>
        <input
          value={password}
          type={"password"}
          onChange={(e) => setPassword(e.target.value)}
          className={"form-input"}
        />
      </form>
    </div>
  )
}

export default Register;