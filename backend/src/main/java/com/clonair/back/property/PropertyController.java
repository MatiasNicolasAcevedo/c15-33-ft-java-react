
package com.clonair.back.property;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/property")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("{id}")
    public ResponseEntity<PropertyResponse> getOne(@PathVariable String id) throws Exception{
        return ResponseEntity.ok(propertyService.getOne(id));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<PropertyResponse>> getByUser(@PathVariable String id) throws Exception{
        return ResponseEntity.ok(propertyService.getByUser(id));
    }

    @GetMapping()
    public ResponseEntity<List<PropertyResponse>> getAll() throws Exception{
        return ResponseEntity.ok(propertyService.getAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("subCategory") String subCategory,
            @RequestParam("description") String description,
            @RequestParam("value") double value,
            @RequestParam("active") boolean active,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("country") String country,
            @RequestParam("city") String city,
            @RequestParam("bathroom") int bathroom,
            @RequestParam("bed") int bed,
            @RequestParam("bedroom") int bedroom,
            @RequestParam("services") List<String> services
    ) throws Exception {
        // Conversión de tipos de datos
        double parsedValue = Double.parseDouble(String.valueOf(value));
        boolean parsedActive = Boolean.parseBoolean(String.valueOf(active));
        int parsedBathroom = Integer.parseInt(String.valueOf(bathroom));
        int parsedBed = Integer.parseInt(String.valueOf(bed));
        int parsedBedroom = Integer.parseInt(String.valueOf(bedroom));

        // Ajuste de categoría y subcategoría a mayúsculas para coincidir con los enums
        Category enumCategory = Category.valueOf(category.toUpperCase());
        SubCategory enumSubCategory = SubCategory.valueOf(subCategory.toUpperCase());

        // Proceso de guardado
        PropertyRequest propertyRequest = new PropertyRequest(title, enumCategory.name(), enumSubCategory.name(), description, parsedValue, parsedActive, images, country, city, parsedBathroom, parsedBed, parsedBedroom, services);
        System.out.println(propertyRequest.toString());
        propertyService.save(token, propertyRequest);
    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable String id,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("subCategory") String subCategory,
            @RequestParam("description") String description,
            @RequestParam("value") double value,
            @RequestParam("active") boolean active,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("country") String country,
            @RequestParam("city") String city,
            @RequestParam("bathroom") int bathroom,
            @RequestParam("bed") int bed,
            @RequestParam("bedroom") int bedroom,
            @RequestParam("services") List<String> services
    ) throws Exception {
        PropertyRequest propertyRequest = new PropertyRequest(title, category, subCategory, description, value, active, images, country, city, bathroom, bed, bedroom, services);
        propertyService.update(id, token, propertyRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token ) throws Exception{
        propertyService.delete(id, token);
    }

}
