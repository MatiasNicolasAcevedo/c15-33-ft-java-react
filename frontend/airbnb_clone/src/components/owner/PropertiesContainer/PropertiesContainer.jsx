import { useEffect, useState } from "react";
import { Spinner } from "@material-tailwind/react";
import { Link } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { Container, EmptyState } from "../../shared";
import { PropertiesUser } from "../";
import { getPropertiesByUserId } from "../../../services/apiRequests";


// const properList = [
//   {
//       "id": "15b54a45-d544-408c-9e45-9ba4b9a40acf",
//       "title": "Castillo",
//       "username": "owner@owner.com",
//       "category": "HOTEL",
//       "subCategory": "URBAN",
//       "description": "Piedra",
//       "value": 499,
//       "active": true,
//       "images": [
//           "23a38435-96f0-4d3a-8b0f-4657d95abc9c",
//           "5add47da-a35e-47c3-81b0-2b38f8fde82b",
//           "66113271-2cf9-4f03-ad16-df52dea2e0ad",
//           "e84e64d8-25c0-4da2-beb3-2cba3c56c4f9"
//       ],
//       "services": [
//           "SWIMMING_POOL",
//           "SMOKING_ALLOWED",
//           "CRADLE",
//           "WIFI",
//           "BREAKFAST",
//           "AIR_CONDITIONER"
//       ],
//       "location": {
//           "id": "351e6fc2-6b77-4311-b5c8-ea3a4cf896d5",
//           "country": "Argentina",
//           "city": "ciudad"
//       },
//       "bathroom": 2,
//       "bed": 4,
//       "bedroom": 3
//   },
//   {
//       "id": "1681b20b-813b-40ca-ad21-47b6c5583dee",
//       "title": "Playa en la montaña",
//       "username": "admin@admin.com",
//       "category": "APARTMENT",
//       "subCategory": "BEACH",
//       "description": "Lo mejor de las mejores playas",
//       "value": 150,
//       "active": true,
//       "images": [
//           "eb607197-835a-4a02-844f-8caa2fc38bbf"
//       ],
//       "services": [
//           "WIFI",
//           "WASHING_MACHINE",
//           "HEATING"
//       ],
//       "location": {
//           "id": "03c9e27e-1e0f-4e2e-b895-fbb77849521e",
//           "country": "Algeria",
//           "city": "ciudad"
//       },
//       "bathroom": 2,
//       "bed": 2,
//       "bedroom": 2
//   },
//   {
//       "id": "1fd4d010-d61f-434e-9ff2-f46eb6a0bc1e",
//       "title": "Casa en la playa",
//       "username": "walterperez@gmail.com",
//       "category": "HOUSE",
//       "subCategory": "BEACH",
//       "description": "casa a metros de la mejor playa de la zona",
//       "value": 35,
//       "active": true,
//       "images": [
//           "167808cc-2f5b-4924-8da2-219cee5922a1"
//       ],
//       "services": [
//           "WIFI",
//           "EARLY_CANCELLATION",
//           "WASHING_MACHINE",
//           "BREAKFAST"
//       ],
//       "location": {
//           "id": "7bc49605-6ec1-46a4-9b38-24e3ad2e63fc",
//           "country": "Guatemala",
//           "city": "ciudad"
//       },
//       "bathroom": 1,
//       "bed": 3,
//       "bedroom": 3
//   },
//   {
//       "id": "41cedf06-0eed-495b-ae00-d57de1b4df14",
//       "title": "PROPIEDAD A ELIMINAR",
//       "username": "admin@admin.com",
//       "category": "APARTMENT",
//       "subCategory": "CABIN",
//       "description": "eliminar",
//       "value": 1331,
//       "active": true,
//       "images": [
//           "68c015bd-b043-4cec-92c0-184a9d9714e1"
//       ],
//       "services": [
//           "AIR_CONDITIONER",
//           "PARKING_LOT"
//       ],
//       "location": {
//           "id": "95ae0fed-947b-41c2-8d55-c50832ed21a2",
//           "country": "Antarctica",
//           "city": "ciudad"
//       },
//       "bathroom": 5,
//       "bed": 1,
//       "bedroom": 2
//   },
//   {
//       "id": "5067467b-1323-4c57-baeb-2c964e9ad454",
//       "title": "Casa en la playa",
//       "username": "walterperez@gmail.com",
//       "category": "HOUSE",
//       "subCategory": "BEACH",
//       "description": "casa a metros de la mejor playa de la zona",
//       "value": 35,
//       "active": true,
//       "images": [
//           "87dfca36-f289-49a0-8d0c-d4ffb2bf66e5"
//       ],
//       "services": [
//           "WIFI",
//           "EARLY_CANCELLATION",
//           "WASHING_MACHINE",
//           "BREAKFAST",
//           "PARKING_LOT"
//       ],
//       "location": {
//           "id": "8272129f-d883-40d5-939a-071da868268c",
//           "country": "Guatemala",
//           "city": "ciudad"
//       },
//       "bathroom": 1,
//       "bed": 3,
//       "bedroom": 3
//   },
//   {
//       "id": "616db1da-49ff-4e99-8d48-60914f6cc70c",
//       "title": "Casita",
//       "username": "leandro@mail.com",
//       "category": "HOUSE",
//       "subCategory": "COUNTRYSIDE",
//       "description": "La mejor casa de todas",
//       "value": 19,
//       "active": true,
//       "images": [
//           "3e3ad903-cd3f-4928-9c2a-64fcc3493042"
//       ],
//       "services": [
//           "WIFI",
//           "PETS_ALLOWED",
//           "GRILL",
//           "AIR_CONDITIONER",
//           "PARKING_LOT"
//       ],
//       "location": {
//           "id": "6e5296b5-30a9-4a35-8b93-0f6629169ed2",
//           "country": "Argentina",
//           "city": "ciudad"
//       },
//       "bathroom": 1,
//       "bed": 4,
//       "bedroom": 2
//   },
//   {
//       "id": "6d46d23b-f83c-463a-8101-38cbd6621aef",
//       "title": "Casita",
//       "username": "leandro@mail.com",
//       "category": "HOUSE",
//       "subCategory": "COUNTRYSIDE",
//       "description": "La mejor casa de todas",
//       "value": 19,
//       "active": true,
//       "images": [
//           "0d93548d-e4a9-4896-9e97-e8d918731eb7"
//       ],
//       "services": [
//           "WIFI",
//           "PETS_ALLOWED",
//           "GRILL",
//           "AIR_CONDITIONER",
//           "PARKING_LOT"
//       ],
//       "location": {
//           "id": "252dca26-3f76-40a0-8e0f-068b495ee7c2",
//           "country": "Argentina",
//           "city": "ciudad"
//       },
//       "bathroom": 1,
//       "bed": 4,
//       "bedroom": 2
//   },
//   {
//       "id": "8286272d-7402-4616-9f21-ffaf09545f4d",
//       "title": "Castillo en la playa",
//       "username": "admin@admin.com",
//       "category": "HOUSE",
//       "subCategory": "MOUNTAIN",
//       "description": "Lujoso castillo a orillas del mar",
//       "value": 539,
//       "active": true,
//       "images": [
//           "e6b44c1f-d12e-4254-980a-4c3ea9e1f473"
//       ],
//       "services": [
//           "WIFI",
//           "WASHING_MACHINE",
//           "PETS_ALLOWED"
//       ],
//       "location": {
//           "id": "a1de9010-b13a-46a7-a991-50779df7b4b3",
//           "country": "Aruba",
//           "city": "ciudad"
//       },
//       "bathroom": 2,
//       "bed": 4,
//       "bedroom": 1
//   },
//   {
//       "id": "8ddc21f7-937b-4c94-a47c-45c3ed0ff13f",
//       "title": "MiCasa",
//       "username": "matias@matias.com",
//       "category": "HOUSE",
//       "subCategory": "URBAN",
//       "description": "Azul Grande",
//       "value": 2500,
//       "active": true,
//       "images": [
//           "12acbe5e-293c-4702-b472-7d7645a1a57c",
//           "1ebc9424-d0c6-4740-935f-abf8bf9c8a91",
//           "93a5ce97-aa3f-4c21-a28b-2904aea2cbe1",
//           "a6168475-d962-48d9-a98e-c280b719db3c",
//           "c1a951d8-3674-4456-b4cb-988d40e4f6f9"
//       ],
//       "services": [
//           "WIFI",
//           "KITCHEN",
//           "SWIMMING_POOL",
//           "HEATING",
//           "GRILL",
//           "BREAKFAST"
//       ],
//       "location": {
//           "id": "7f62560a-3429-4f2c-b0c9-ab4aedc7c2b7",
//           "country": "Venezuela",
//           "city": "ciudad"
//       },
//       "bathroom": 3,
//       "bed": 4,
//       "bedroom": 2
//   },
//   {
//       "id": "de7f6413-cf47-4ce8-a813-64c8940c88b3",
//       "title": "La playa en la montaña",
//       "username": "admin@admin.com",
//       "category": "HOUSE",
//       "subCategory": "MOUNTAIN",
//       "description": "Descripcion hermosa",
//       "value": 509,
//       "active": true,
//       "images": [
//           "04e4652a-7575-49e3-ba7d-b407744dcb55"
//       ],
//       "services": [
//           "WIFI",
//           "WASHING_MACHINE",
//           "CRADLE",
//           "EARLY_CANCELLATION"
//       ],
//       "location": {
//           "id": "d94f40cb-2962-404b-bd40-bdd8ffb426dc",
//           "country": "Cayman Islands",
//           "city": "ciudad"
//       },
//       "bathroom": 3,
//       "bed": 5,
//       "bedroom": 2
//   }
// ]

export const PropertiesContainer = () => {
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);

  const { id } = useLocation();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getPropertiesByUserId(id);
        
        setProperties(data);
        console.log(properties);

        setLoading(false);
      } catch (error) {
        console.error('Error al obtener las propiedades:', error);
        setLoading(false);
      }
    };

    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps -- comportamiento existente, ver Backlog
  }, [id]);

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
              title={"Propiedades"}
              subtitle={"No tenes ningúna propiedad publicada... ¡por ahora!"}
              showReset={true}
            />
          )
      }
      </div>
    );
  }

  return (
    <Container>
      {
        loading
          ? 
            (
              <div className="grid items-center justify-center h-screen">
                <Spinner className="h-12 w-12" />
              </div>
            )
          : 
            (
              properties.map((propertie) => (
                <Link
                  key={propertie.id}
                  to={`/property-detail/${propertie.id}`}
                  state={{
                    // propertiesList: properties
                    // id: propertie.id,
                    // title: propertie.title,
                    // username: propertie.username,
                    // category: propertie.category,
                    // subCategory: propertie.subCategory,
                    // description: propertie.description,
                    // value: propertie.value,
                    // active: propertie.active,
                    // images: propertie.images,
                    // location: propertie.location,
                    // bathroom: propertie.bathroom,
                    // bed: propertie.bed,
                    // bedroom: propertie.bedroom,
                  }}
                >
                  <PropertiesUser />
                </Link>
              )
              )
            )
      }
    </Container>   
  )
}