import { useLocation, useParams } from "react-router-dom";
import { Container } from "../../shared";
import { DetailCardHead, DetailCardInfo, DetailCardReservation } from "../";
import { API_URL } from "../../../services/config";

export const DetailContainerCard = () => {

  const id = useParams();

  let { state } = useLocation();
  const { title, username, category, description, subCategory, value, active, images, location, bedroom, bed, bathroom } = state;


  let totalPrice = value;

  return (
    <Container>
      <div 
        className="
          max-w-screen-lg 
          mx-auto
        "
      >
        <div className="flex flex-col gap-6">
          <DetailCardHead
            title={title}
            country={location.country}
            image={`${API_URL}/img/${images[0]}`}
          />
          <div 
            className="
              grid 
              grid-cols-1 
              md:grid-cols-7 
              md:gap-10 
              mt-6
            "
          >
            <DetailCardInfo
              userName={username}
              bedroom={bedroom}
              bed={bed}
              bathroom={bathroom}
              description={description}

              category={category}
              subCategory={subCategory}
              active={active}
            />
            <div 
              className="
                order-first 
                mb-10 
                md:order-last 
                md:col-span-3
              "
            >
              <DetailCardReservation
                id={id}
                value={value}
                totalPrice={totalPrice}
              />
            </div>
          </div>
        </div>
      </div>
    </Container>
   );
}
