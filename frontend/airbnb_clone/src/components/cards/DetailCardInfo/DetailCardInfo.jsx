import { DetailCardCategory } from "../";

export const DetailCardInfo = ({ 
  description,
  userName,
  bed,
  bedroom,
  bathroom,
  category,
  subCategory,
  active
}) => {

  // category={category}
  // subCategory={subCategory}
  // active={active}
  return (
    <div className="col-span-4 flex flex-col gap-8">
      <div className="flex flex-col gap-2">
        <div 
          className="
          text-xl 
          font-semibold 
          flex 
          flex-row 
          items-center
          gap-2"
        >
          <div>Anfitrión: {userName}</div>
        </div>

        <div className="
          flex 
          flex-row 
          items-center 
          gap-4 
          font-light
          text-neutral-500"
        >
          <div>
            {bed} Camas
          </div>
          <div>
            {bedroom} habitaciones
          </div>
          <div>
            {bathroom} baños
          </div>
        </div>
      </div>

      <hr />
        <DetailCardCategory
          category={category}
          subCategory={subCategory}
          active={active}
        />
      <hr />
      <div className="text-lg font-light text-neutral-500">
        {description}
      </div>
      <hr />
    </div>
  );
}
