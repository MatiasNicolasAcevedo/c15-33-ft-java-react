import { Option, Select } from '@material-tailwind/react';
import { countries } from 'countries-list'

const countryList = []
for (const key in countries) {
    if (Object.prototype.hasOwnProperty.call(countries, key)) {
      countryList.push(countries[key].name);
    }
  }

countryList.sort()


export const CountriesSelect = ({ countryValue, setCountryValue, newError }) => {



    return(
            <Select label="País" color="indigo" className="w-full" value={countryValue} onChange={(e) =>setCountryValue(e)} error={newError}>
            {countryList.map((country, index) => (
                <Option key={index} value={country}>{country}</Option>
            ))}
            </Select>
    )
}