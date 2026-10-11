import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { Spinner } from "@material-tailwind/react";
import { Container, EmptyState, HostCardItem } from "..";
import { getAllProperties } from "../../services/apiRequests";


const FilteredFilters = () => {
    const [properties, setProperties] = useState([]);
    const [loading, setLoading] = useState(true);
    
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const minValue = queryParams.get('minValue');
    const maxValue = queryParams.get('maxValue');
    const selectedTypes = queryParams.get('propType').split(',');
    const selectedRooms = queryParams.get('selectedRooms');
    const selectedBeds = queryParams.get('selectedBeds');
    const selectedBaths = queryParams.get('selectedBaths');    
    const checkboxString = queryParams.get('services');
    const initialCheckboxes = checkboxString ? JSON.parse(checkboxString) : {};

    const [checkboxes] = useState(initialCheckboxes);
   

    useEffect(() => {         
             
        const propertiesFetch = async () => {
            try {           
                const properties = await getAllProperties();                
                const filteredProperties = properties.filter(property => {                
                    const priceRange = property.value >= parseInt(minValue) && property.value <= parseInt(maxValue);                    
                    const propertyTypeSelected = selectedTypes.length === 0 || selectedTypes.includes(property.category);                    
                    const roomsMatch = selectedRooms === 'cualquiera' || property.bedroom == selectedRooms;
                    const bedsMatch = selectedBeds === 'cualquiera' || property.bed == selectedBeds;
                    const bathsMatch = selectedBaths === 'cualquiera' || property.bathroom == selectedBaths;
                                   
                    const servicesMatch = Object.keys(checkboxes).every(key => {
                        const lowercaseKey = key.toLowerCase();
                        return !checkboxes[key] || property.services.some(service => service.toLowerCase() === lowercaseKey);
                    });                         
                   
                    return priceRange && propertyTypeSelected && roomsMatch && bedsMatch && bathsMatch &&servicesMatch;
                });  
                    setProperties(filteredProperties);
                    setLoading(false);               
    
            } catch (error) {
                console.error('Error al obtener propiedades:', error);
                setLoading(false);          
            }            
        }
        propertiesFetch();
        // eslint-disable-next-line react-hooks/exhaustive-deps -- comportamiento existente, ver Backlog
    }, [location.search]);
   
   
    if (properties.length === 0) {
      return (
        <div className="grid items-center justify-center h-screen">
          {
            loading
          ? 
            (<Spinner className="h-12 w-12" />)
          :
            (
              <EmptyState
                title={"Alojamientos"}
                subtitle={"No hay ningún alojamiento publicado... ¡por ahora!"}
                showReset={true}
              />
            )
        }
        </div>
      );
    }
  
    return (
      <Container>
        <div className="pt-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 gap-8">
            {loading ? 
                  (<div className="grid items-center justify-center h-screen">
                      <Spinner className="h-12 w-12" /></div>)
                : 
                  (properties.map((propertie) => (
                        <Link   key={propertie.id}
                                to={`/property-detail/${propertie.id}`}
                                state={{
                                title: propertie.title,
                                username: propertie.username,
                                category: propertie.category,
                                subCategory: propertie.subCategory,
                                description: propertie.description,
                                value: propertie.value,
                                active: propertie.active,
                                images: propertie.images,
                                location: propertie.location,
                                bathroom: propertie.bathroom,
                                bed: propertie.bed,
                                bedroom: propertie.bedroom,}}>
                            <HostCardItem
                            title={propertie.title}
                            description={propertie.description}
                            location={propertie.location}                        
                            value={propertie.value}
                            images={propertie.images}/>
                        </Link>
                    )
                    )
                  )
            }
          </div>
      </Container>
    )
};

export default FilteredFilters;