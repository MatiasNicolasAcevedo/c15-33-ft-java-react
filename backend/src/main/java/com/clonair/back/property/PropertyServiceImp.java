package com.clonair.back.property;

import com.clonair.back.common.error.NotFoundException;
import com.clonair.back.common.error.ForbiddenException;
import org.springframework.security.authentication.BadCredentialsException;
import com.clonair.back.image.Image;
import com.clonair.back.image.ImageService;
import com.clonair.back.location.Location;
import java.util.List;
import java.util.Optional;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.user.Role;
import com.clonair.back.user.User;
import com.clonair.back.user.UserService;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static com.clonair.back.security.utils.Utils.obtenerUserDetails;

@Service
@Data
@RequiredArgsConstructor
public class PropertyServiceImp implements PropertyService {

    private final JwtService jwtService;
    private final PropertyRepository propertyRepository;
    private final ImageService imageService;
    private final UserService userService;

    @Override
    public PropertyResponse getOne(String id) throws Exception {
       
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Property not found"));

            return propertyToResponseMap(property);
                
    }

    @Override
    public List<PropertyResponse> getAll() throws Exception {
         
        return propertyRepository.findAll()
                .stream()
                .map(this::propertyToResponseMap)
                .toList();
                
    }

    @Override
    public List<PropertyResponse> getByUser(String id) throws Exception {
        
        List<PropertyResponse> properties = propertyRepository.findByUser(userService.findById(id))
            .stream().map((prop)->{return propertyToResponseMap(prop);}).toList();
        return properties;

    }

    @Override
    public void save(String jwtToken, PropertyRequest request) throws Exception {
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            String token = jwtToken.substring(7); // Extrae el token JWT sin el prefijo "Bearer "

            UserDetails userDetails = obtenerUserDetails(); // Obtén los UserDetails necesarios

            if (jwtService.isTokenValid(token, userDetails)) { // Validación del token JWT con los UserDetails
                Optional<User> userOptional = userService.findByUsername(userDetails.getUsername()); // Buscar el usuario por su nombre de usuario(email en este caso)
                User user = userOptional.orElse(null); // Obtener el User del Optional.

                if (user != null && user.getRole() == Role.OWNER) {
                    // Crear una nueva propiedad sin establecer su ID explícitamente.
                    Property property = requestToPropertyMap(request);
                    property.setUser(user);

                    // Asociar las imágenes con la propiedad creada
                    List<Image> images = imageService.saveMulti(request.images().toArray(new MultipartFile[0]));
                    images.forEach(image -> image.setProperty(property)); // Establecer la propiedad en cada imagen.

                    property.setImages(images); // Establecer la lista de imágenes en la propiedad.



                    propertyRepository.save(property); // Guardar la propiedad con las imágenes asociadas.
                } else {
                    throw new ForbiddenException("El usuario no es un OWNER");
                }
            } else {
                throw new BadCredentialsException("Invalid JWT token");
            }
        } else {
            throw new BadCredentialsException("Authorization token is missing or invalid");
        }
    }

    @Override
    public void update(String id, String jwtToken, PropertyRequest request) throws Exception {
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            String token = jwtToken.substring(7); // Extraer el token JWT sin el prefijo "Bearer "

            UserDetails userDetails = obtenerUserDetails(); // Obtener los UserDetails necesarios

            if (jwtService.isTokenValid(token, userDetails)) { // Validación del token JWT con los UserDetails
                Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
                User user = userOptional.orElse(null); // Obtener el User del Optional.

                if (user != null && user.getRole() == Role.OWNER) {
                    Property existingProperty = propertyRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Property not found"));

                    if (!existingProperty.getUser().getUsername().equals(userDetails.getUsername())) {
                        throw new ForbiddenException("No tiene permiso para editar esta propiedad");
                    }

                    // Actualizar la propiedad existente con los nuevos valores del PropertyRequest
                    Property updatedProperty = updatePropertyFromRequest(existingProperty, request);

                    // Actualizar las imágenes asociadas a la propiedad
                    List<Image> updatedImages = imageService.updatePropertyImages(existingProperty, request.images());
                    updatedProperty.setImages(updatedImages);

                    // Guardar la propiedad actualizada
                    propertyRepository.save(updatedProperty);
                } else {
                    throw new ForbiddenException("El usuario no es un OWNER");
                }
            } else {
                throw new BadCredentialsException("Invalid JWT token");
            }
        } else {
            throw new BadCredentialsException("Authorization token is missing or invalid");
        }
    }

    @Override
    public List<PropertyResponse> filtered(String category) throws Exception {
        return propertyRepository.findByCategory(Category.valueOf(category))
                .stream()
                .map(this::propertyToResponseMap)
                .toList();
    }

    @Override
    public void delete(String id, String jwtToken) throws Exception {
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            String token = jwtToken.substring(7); // Extraer el token JWT sin el prefijo "Bearer "

            UserDetails userDetails = obtenerUserDetails(); // Obtener los UserDetails necesarios

            if (jwtService.isTokenValid(token, userDetails)) { // Validar el token JWT con los UserDetails
                Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
                User user = userOptional.orElseThrow(() -> new NotFoundException("User not found"));

                if (user.getRole() == Role.OWNER) {
                    Property property = propertyRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Property not found"));

                    if (property.getUser().getUsername().equals(userDetails.getUsername())) {
                        propertyRepository.delete(property);
                    } else {
                        throw new ForbiddenException("No tiene permiso para eliminar esta propiedad");
                    }
                } else {
                    throw new ForbiddenException("El usuario no tiene permiso para eliminar propiedades");
                }
            } else {
                throw new BadCredentialsException("Invalid JWT token");
            }
        } else {
            throw new BadCredentialsException("Authorization token is missing or invalid");
        }
    }

    private PropertyResponse propertyToResponseMap(Property property) {
        List<String> imageUrls = property.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getUser().getUsername(),
                property.getCategory(),
                property.getSubCategory(),
                property.getDescription(),
                property.getValue(),
                property.isActive(),
                imageUrls, // Enviar solo las URLs de las imágenes
                property.getLocation(),
                property.getBathroom(),
                property.getBed(),
                property.getBedroom()
        );
    }

    private Property requestToPropertyMap(PropertyRequest request) throws Exception {
        Property property = new Property();

        // Crear una nueva instancia de Location para esta propiedad
        Location location = new Location();
        location.setCountry(request.country());
        location.setCity(request.city());

        // Configurar los datos en la entidad Property
        property.setTitle(request.title());
        property.setCategory(Category.valueOf(request.category()));
        property.setDescription(request.description());
        property.setValue(request.value());
        property.setLocation(location); // Asignar la nueva instancia de Location a la Property
        property.setBathroom(request.bathroom());
        property.setBed(request.bed());
        property.setBedroom(request.bedroom());

        // Mapear los servicios de String a Enums Service
        List<ServiceType> serviceTypeEnums = mapServices(request.services());

        property.setServiceTypes(serviceTypeEnums);

        // Manejar subCategory y active (si son nulos, no establecerlos)
        if (request.subCategory() != null) {
            property.setSubCategory(SubCategory.valueOf(request.subCategory()));
        }
        if (request.active() != null) {
            property.setActive(request.active());
        }
        return property;
    }

    private List<ServiceType> mapServices(List<String> servicesAsString) {
        return servicesAsString.stream()
                .map(this::getServiceFromString)
                .collect(Collectors.toList());
    }

    private ServiceType getServiceFromString(String serviceString) {
        try {
            return ServiceType.valueOf(serviceString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid service: " + serviceString);
        }
    }

    private Property updatePropertyFromRequest(Property existingProperty, PropertyRequest request) throws Exception {
        // Crear una nueva instancia de Location para esta propiedad
        Location location = existingProperty.getLocation();
        if (location == null) {
            location = new Location();
        }
        location.setCountry(request.country());
        location.setCity(request.city());

        // Configurar los datos en la entidad Property
        existingProperty.setTitle(request.title());
        existingProperty.setCategory(Category.valueOf(request.category()));
        existingProperty.setDescription(request.description());
        existingProperty.setValue(request.value());
        existingProperty.setLocation(location); // Asignar la nueva instancia de Location a la Property
        existingProperty.setBathroom(request.bathroom());
        existingProperty.setBed(request.bed());
        existingProperty.setBedroom(request.bedroom());

        // Mapear los servicios de String a Enums Service
        List<ServiceType> serviceTypeEnums = mapServices(request.services());
        existingProperty.setServiceTypes(serviceTypeEnums);

        // Manejar subCategory y active (si son nulos, no establecerlos)
        if (request.subCategory() != null) {
            existingProperty.setSubCategory(SubCategory.valueOf(request.subCategory()));
        }
        if (request.active() != null) {
            existingProperty.setActive(request.active());
        }

        return existingProperty;
    }

}
