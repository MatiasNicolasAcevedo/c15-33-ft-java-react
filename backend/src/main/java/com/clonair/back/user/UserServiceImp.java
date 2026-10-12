
package com.clonair.back.user;

import com.clonair.back.common.error.NotFoundException;
import com.clonair.back.property.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getByUsername(String username, String token) throws Exception{
        Optional<User> userOptional = findByUsername(username);
        User user = userOptional.orElse(null);
        if (user != null) {
            return mapUserToResponse(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserResponse getOne(String id, String token) throws Exception {
        User user = findById(id);
        if (user != null) {
            return mapUserToResponse(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public List<UserResponse> getAll(String token) throws Exception {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapUserToResponse)
                .toList();
    }

    @Override
    public void update(String id, String token, UserRequest request) throws Exception {
        User user = findById(id);
        if (user != null) {
            // Actualizar los campos del usuario con los datos proporcionados en el UserRequest
            user.setFirstname(request.firstname());
            user.setLastname(request.lastname());
            user.setCountry(request.country());
            user.setContact(request.contact());
            user.setDescription(request.description());

            // Guardar los cambios en la base de datos
            userRepository.save(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public void delete(String id, String token) throws Exception {
        User user = findById(id);
        if (user != null) {
            // Realizar aquí cualquier validación adicional necesaria antes de permitir la eliminación del usuario.
            // Por ejemplo, verificar si el usuario tiene permiso para realizar esta acción, tal vez a través del token proporcionado.

            // Si se completan las validaciones, proceder con la eliminación.
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    private UserResponse mapUserToResponse(User user) {
        List<String> propertyIds = user.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toList());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getCountry(),
                user.getRole(),
                propertyIds,
                user.getContact(),
                user.getDescription()
        );
    }

}
