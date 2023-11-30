import LogoIcon from "../../assets/logoicon/LogoIcon";
import { Button, IconButton } from "@material-tailwind/react";
import  { useState } from 'react';
import Search from "../search/Search";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import Forms from "../forms/Forms";

const NavBar = () => {     
    const [isSearchOpen, setIsSearchOpen] = useState(false);
        
    const [openLogin, setOpenLogin] = useState(false);
    const handleOpenLogin = () => setOpenLogin((cur) => !cur);

    const [openSignUp, setOpenSignUp] = useState(false);
    const handleOpenSignUp = () => setOpenSignUp((cur) => !cur);
    

    const openSearch = () => {
        setIsSearchOpen(!isSearchOpen);
    };
   
    return ( 
            <> 
                <section className="py-3 px-8 flex justify-between items-center text-md">        
                    <a href="/"><LogoIcon/></a>
                    <div className="flex flex-col justify-center items-center">
                        <Button variant="outlined" onClick={openSearch} className="box-border flex items-center gap-2 rounded-3xl border border-gray-300 shadow-md hover:shadow-lg px-3 py-1">  
                            {isSearchOpen ? 
                            "Hide" :
                            <div>
                                <h2 className="hidden md:inline">Destino | Fechas | Huéspedes</h2>
                                <IconButton color="red" size = "sm" className="px-8 md:px-10 lg:px-1 rounded-full m-1.5">
                                    <FontAwesomeIcon icon={faMagnifyingGlass} />
                                </IconButton>
                            </div>}                                                  
                        </Button>                    
                        {isSearchOpen && <Search/>}
                    </div>
                    <div className="flex gap-4">
                        
                        <Button variant="text" className="rounded-full" onClick={handleOpenSignUp}>Registrarse</Button>
                        <Button variant="outlined" className="rounded-full" onClick={handleOpenLogin}>Iniciar Sesión</Button>
                        <Forms
                            openLogin={openLogin}
                            openSignUp={openSignUp}
                            handleOpenLogin={handleOpenLogin}
                            handleOpenSignUp={handleOpenSignUp}
                        />

                    </div>
                </section>  
            </>
        
    );
};


export default NavBar;
