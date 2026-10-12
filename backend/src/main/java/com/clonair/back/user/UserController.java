
package com.clonair.back.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) throws Exception {
        return ResponseEntity.ok(userService.getByUsername(username, token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getOne(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) throws Exception {
        return ResponseEntity.ok(userService.getOne(id, token));
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAll(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) throws Exception {
        return ResponseEntity.ok(userService.getAll(token));
    }

    /*
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable String id,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestBody UserRequest userRequest) {
        try {
            userService.update(id, token, userRequest);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) throws Exception {
        userService.delete(id, token);
        return ResponseEntity.noContent().build();
    }

}
