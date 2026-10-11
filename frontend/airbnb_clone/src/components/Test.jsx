import { Button } from "@material-tailwind/react"
import { useAuth } from "../context/authContext";
import { deleteProperty, getAllProperties, getImageUrlById, getPropertiesByUserId, getProperty } from "../services/apiRequests";
import { API_URL } from "../services/config";


export const Test = () => {
    const token = localStorage.getItem('token') ? localStorage.getItem('token') : ''

    const { user, setUser } = useAuth()


      const property = {
        "title": "Titulo",
        "description": "Descripcion",
        "images": [],
        "value": "123",
        "location": "Antarctica",
        "category": "casa",
        "subCategory": "cabana",
        "bathroom": "2",
        "bed": "5",
        "bedroom": "1",
        "serviceTypes": [
            "cocina",
            "mascotas"
        ],
        "permanent_availability": "true",
        "availability": [],
        "active": "true",
        "user": "user_owner_placeholder"
    }

      const getUsers = async () => {
        try {
            const response = await fetch(`${API_URL}/user`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
    
            if(response.ok){
                const data = await response.json()
                console.log(data)
            } else {
                console.error('error de respuesta');
            }
            
            } catch (error) {
                console.error('Error de red:', error);
            }
      }

      const id = "1681b20b-813b-40ca-ad21-47b6c5583dee"

      const userId= "c536542a-66f7-4fb5-8e99-884c594e1bcf"
      
      const imgId = getImageUrlById("23a38435-96f0-4d3a-8b0f-4657d95abc9c")

    return (
        <>
            <Button onClick={() => deleteProperty(id)}>Delete property</Button> 
            <Button onClick={getUsers}>Get Data</Button>
            <Button onClick={() => (getProperty(id))}>Get Property</Button>
            <Button onClick={getAllProperties}>Get Properties</Button>
            <Button onClick={() => getPropertiesByUserId(userId)}>Get User Properties</Button>
            <img src={imgId} alt="" />

        </>
    )
}