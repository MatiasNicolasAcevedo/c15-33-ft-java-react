import { API_URL } from "../../../services/config";

export const HostCardItem = ({title, description, location, value, images}) => {
  return (
    <div
      className="col-span-1 cursor-pointer group"
    >
      <div className="flex flex-col gap-2 w-full">
        <div 
          className="
            aspect-square 
            w-full 
            relative 
            overflow-hidden 
            rounded-xl
          "
        >
          <img
            src={`${API_URL}/img/${images[0]}`}
            alt="image 2"
            className="
              object-cover 
              h-full 
              w-full 
              group-hover:scale-110 
              transition"
          />
          
          <div className="
            absolute
            top-3
            right-3
          ">
          </div>
        </div>

        <div className="font-semibold xl:text-sm md:text-base sm:text-lg truncate">
          { title}
        </div>

        {/* <div className="font-light text-gray-700 xl:text-sm md:text-base sm:text-lg truncate">
          {description}
        </div> */}

        <div className="font-light text-gray-700 xl:text-sm md:text-base sm:text-lg truncate">
          {location.country}
        </div>

        {/* <div className="font-light text-gray-700 xl:text-sm md:text-base sm:text-lg truncate">
          {reservationDate}
        </div> */}
        
        <div className="font-semibold xl:text-sm md:text-base sm:text-lg truncate">
          ${value} por noche
        </div>
      </div>
    </div>
  );
}