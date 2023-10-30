import React, {useState} from "react";

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    console.log(username, password);
  }
  
  return (
    <div className={"container"}>
      <h3>Login</h3>
      
      <form className={"login-form"} onSubmit={handleSubmit}>
        <label className={"form-label"}>Username: </label>
        <input type={"text"} value={username} onChange={(e) => setUsername(e.target.value)} className={"form-input"}/>
        <label className={"form-label"}>Password: </label>
        <input type={"password"} value={password} onChange={(e) => setPassword(e.target.value)} className={"form-input"}/>
      </form>
    </div>
  )
}

export default Login;