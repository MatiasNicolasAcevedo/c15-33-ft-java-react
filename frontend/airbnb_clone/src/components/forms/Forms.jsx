import { useEffect, useRef, useState } from 'react'
import {
    Button,
    Dialog,
    Card,
    CardBody,
    CardFooter,
    Typography,
    Input,
    Checkbox,
    Switch,
  } from '@material-tailwind/react'

import { login, register } from "../../services/apiRequests.jsx"

import passwordIcon from '../../assets/password_icon.svg'
import { CountriesSelect } from '../countries/CountryList'

const Forms = ({openLogin, handleOpenLogin, openSignUp, handleOpenSignUp}) => {

    const switchRef = useRef()
    const [registerButtonPressed, setRegisterButtonPressed] = useState(false)

    const [countryValue, setCountryValue] = useState('')
    
    const [dataMissing, setDataMissing] = useState(false)
    const [invalidEmail, setInvalidEmail] = useState(false)
    const [invalidPassword, setInvalidPassword] = useState(false)
    const [showPassword, setShowPassword] = useState(true);
    const handleShowPassword = () => setShowPassword((cur) => !cur);
    
    const initialUserData = {
        username: '',
        password: '',
        firstname: '',
        lastname: '',
        country:'',
        role:'',
    }
    const [userData, setUserData] = useState(initialUserData);

    const initialInputArray = (() => {
        const initialState = {};
        for (const key in initialUserData) {
            initialState[key] = false; 
        }
        return initialState;
    })();

    const [inputArray, setInputArray] = useState(initialInputArray);



    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setUserData({
            ...userData,
            [name] : value
        })

        setInputArray({
            ...inputArray,
            [name]: value.trim() === '',
        });
        
        if(!dataMissing){
            return
        }

        for (const key in userData) {
            if (userData[key]==='' && key!=="role") {
                setDataMissing(true)
                return
            } else{
                setDataMissing(false)
            }
        }
    };


    useEffect(() => {
        
        
        setUserData({
            ...userData,
            ['country'] : countryValue
        })

        setInputArray({
            ...inputArray,
            ['country'] : countryValue === ''
        })

        if(!dataMissing){
            return
        }

        for (const key in userData) {
            if (userData[key]==='' && key!=="role") {
                setDataMissing(true)
                return
            } else{
                setDataMissing(false)
            }
        }



    }, [countryValue])



    const handleLogInButton = (userData) => {
        if(userData.username !== '' && userData.password !== ''){
            sendLogin(userData)
            handleOpenLogin()
            resetData()
        } else{
            showEmptyInput()
            setDataMissing(true)
        }
    }


    const handleRegisterButton = () => {



        if (!validateEmailFormat(userData.username)) {
            setInputArray(prevState => ({
                ...prevState,
                username: true,
            }));
            setInvalidEmail(true);
        } else {
            setInvalidEmail(false);
        }

        if(!validatePasswordFormat(userData.password)){
            setInputArray(prevState => ({
                ...prevState,
                password: true,
            }));
            setInvalidPassword(true);
        } else{
            setInvalidPassword(false);
        }

        for (const key in userData) {
            if(userData[key]==='' && key!=="role"){
                showEmptyInput()
                setDataMissing(true)
                return
            }
        }
        
        setRegisterButtonPressed(true)
        setUserData(prevData => ({
          ...prevData,
          role: switchRef.current.checked ? 'OWNER' : 'USER',
        }));


    }

    useEffect(() => {
        if (registerButtonPressed && !dataMissing && !invalidEmail && !invalidPassword) {
      
            sendRegister(userData);
            handleOpenSignUp();
            resetData();
        }
        setRegisterButtonPressed(false)
    }), [registerButtonPressed]



    const resetData= () =>{
        setInputArray(initialInputArray)
        setUserData(initialUserData)
        setDataMissing(false)
        setInvalidEmail(false)
        setInvalidPassword(false)
    }

    const showEmptyInput = () => {
        const properties = Object.keys(inputArray);

        properties.forEach(property => {
            if (userData[property] === '') {
              setInputArray(prevState => ({
                ...prevState,
                [property]: true,
              }));
            }
            
        });
            
    }


    useEffect(() => {
    
        if(document.getElementsByClassName("password-icon")[0]){
        
          const icon = document.getElementsByClassName("password-icon")[0]
          
          icon.addEventListener("click", function(event){
            event.preventDefault()
            handleShowPassword()
          })
        }

        if(!openLogin && !openSignUp){
            resetData()
        }

        
    }, [openLogin, openSignUp]);


    const validatePasswordFormat = (password) => {
        
        if (password.length < 8) {
            return false;
        }

        if (!/[A-Z]/.test(password)) {
            return false;
        }

        if (!/\d/.test(password)) {
            return false;
        }


        return true;
    }

    const validateEmailFormat = (email) => {
        const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regexEmail.test(email);
    }
    


    const sendLogin = (userData) => {
        const { username, password } = userData;
        const newData = {username, password}
        login(newData)
        console.log("Data enviada al back:", userData)
    }

    const sendRegister = (userData) => {
        register(userData)
        console.log("Data enviada al back:", userData)
    }


    return(
        <>
            <Dialog 
            size="sm"
            open={openLogin}
            handler={handleOpenLogin}
            className="bg-transparent shadow-none"
            
        >
            <Card className="mx-auto w-full max-w-[36rem]"> 
            <CardBody className="flex flex-col gap-4">
                <Typography variant="h4" color="blue-gray">
                Iniciar Sesión
                </Typography>
                <Typography
                className="-mb-3 font-normal"
                variant="paragraph"
                color="gray"
                >
                Ingresa tu mail y contraseña para iniciar sesión
                </Typography>
                <div className="h-2">
                    <Typography className="" variant="small" color="red">
                    {dataMissing ? "* Completar todos los campos" : ""}
                    </Typography>
                </div>
                <Typography className="-mb-2" variant="h6">
                Tu mail
                </Typography>
                <Input name="username" error={inputArray.username} color="indigo" label="Mail" size="lg"
                onChange={(event) => {
                handleInputChange(event);
                }}/>
                <Typography className="-mb-2" variant="h6">
                Tu contraseña
                </Typography>
                <Input name="password" error={inputArray.password}  type={showPassword ? "password" : ""} color="indigo" label="Contraseña" size="lg" icon={<img className="password-icon" src={passwordIcon}/>} onChange={handleInputChange}/>
                <div className="-ml-2.5 -mt-3">
                <Checkbox label="Recordarme" />
                </div>
            </CardBody>
            <CardFooter className="pt-0">
                <Button variant="gradient" onClick={() => {handleLogInButton(userData)}} fullWidth>
                Iniciar sesión
                </Button>
                <Typography variant="small" className="mt-4 flex justify-center">
                ¿No tenés una cuenta?
                <Typography
                    variant="small"
                    color="blue-gray"
                    className="ml-1 font-bold"
                    onClick={() => {
                    handleOpenLogin()
                    handleOpenSignUp()
                    resetData()
                    
                    }}
                >
                    <span className="hover-pointer">Registrate</span>
                </Typography>
                </Typography>
            </CardFooter>
            </Card>
        </Dialog>



        <Dialog
            size="sm"
            open={openSignUp}
            handler={handleOpenSignUp}
            className="bg-transparent shadow-none"
        >
            <Card className="mx-auto w-full max-w-[36rem]">
            <CardBody className="flex flex-col gap-3">
                <Typography className='-mb-2' variant="h4" color="blue-gray">
                Registrarse
                </Typography>
                <Typography
                className="-mb-3 font-normal"
                variant="paragraph"
                color="gray"
                >
                Ingresa tu mail y contraseña para el registro
                </Typography>
                <div className="h-2">
                    <Typography className="" variant="small" color="red">
                    {dataMissing ? "Existen campos incompletos" : ""}
                    </Typography>
                </div>
                <div className='flex gap-2 items-baseline'>
                    <Typography className="-mb-2" variant="h6">
                    Tu mail
                    </Typography>
                    <Typography className="" variant="small" color="red">
                    {invalidEmail ? "* Formato de mail inválido (ejemplo@ejemplo.com)" : ""}
                    </Typography>
                </div>
                <Input error={inputArray.username} name="username" color="indigo" label="Mail" size="lg" onChange={handleInputChange}/>
                <div className='flex gap-2 items-baseline'>
                    <Typography className="-mb-2" variant="h6">
                    Tu contraseña
                    </Typography>
                    <Typography className="" variant="small" color="red">
                    {invalidPassword ? "* Mín. 8 caracteres, 1 mayúscula y 1 número" : ""}
                    </Typography>

                </div>
                <Input error={inputArray.password} name="password" type={showPassword ? "password" : ""} color="indigo" label="Contraseña" size="lg" icon={<img className="password-icon" src={passwordIcon}/>} onChange={handleInputChange}/>
                <Typography className="-mb-2" variant="h6">
                Apellido/s
                </Typography>
                <Input error={inputArray.lastname} name="lastname" color="indigo" label="Apellido/s" size="lg" onChange={handleInputChange}/>
                <Typography className="-mb-2" variant="h6">
                Nombre/s
                </Typography>
                <Input error={inputArray.firstname} name="firstname" color="indigo" label="Nombre/s" size="lg" onChange={handleInputChange}/>
                <Typography className="-mb-2" variant="h6">
                País
                </Typography>
                <CountriesSelect newError={inputArray.country} name="country" newValue={countryValue} setCountryValue={setCountryValue}/> 
                <div className='-mb-3'>
                    <Switch label="Soy propietario" inputRef={switchRef}/>
                </div>
            </CardBody>
            <CardFooter className="pt-0">
                <Button variant="gradient" onClick={handleRegisterButton} fullWidth>
                Registrarse
                </Button>
                <Typography variant="small" className="mt-4 flex justify-center">
                ¿Ya tenés una cuenta?
                <Typography
                    variant="small"
                    color="blue-gray"
                    className="ml-1 font-bold"
                    onClick={() => {
                    handleOpenSignUp()
                    handleOpenLogin()
                    resetData()
                    
                    }}
                >
                    <span className="hover-pointer">Iniciar Sesión</span>
                </Typography>
                </Typography>
                
            </CardFooter>
            </Card>
        </Dialog>



        </>
    )
}

export default Forms
