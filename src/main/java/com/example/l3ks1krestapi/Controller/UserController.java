package com.example.l3ks1krestapi.Controller;

import com.example.l3ks1krestapi.DTO.ErrorCodes;
import com.example.l3ks1krestapi.DTO.Message;
import com.example.l3ks1krestapi.Exceptions.UserDoesntExistsException;
import com.example.l3ks1krestapi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String token){
        try{
            return ResponseEntity.ok(userService.getMe(token));
        } catch (UserDoesntExistsException e){
            return ResponseEntity.ok(Message.builder()
                    .message(ErrorCodes.E1005.getValue())
                    .errorCode(ErrorCodes.E1005.name())
                    .build());
        }
    }
    @GetMapping("/{user}")
    public ResponseEntity<?> getUser(@PathVariable String user){
        try {
            return ResponseEntity.ok(userService.getUser(user));
        } catch(RuntimeException e){
            return ResponseEntity.ok(Message.builder()
                    .message(ErrorCodes.E1005.getValue())
                    .errorCode(ErrorCodes.E1005.name())
                    .build());
        }
    }

    @GetMapping("/keys/{user}")
    public ResponseEntity<?> getKeys(@PathVariable String user){
        try{
            return ResponseEntity.ok(userService.getKeys(user));
        } catch (RuntimeException e){
            return ResponseEntity.ok(Message.builder()
                    .message(ErrorCodes.E1005.getValue())
                    .errorCode(ErrorCodes.E1005.name())
                    .build());
        }
    }
}
