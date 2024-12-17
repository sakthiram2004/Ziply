package com.sr.Ziply.controller.common;

import com.sr.Ziply.dto.UserDto;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.JwtUtils;
import com.sr.Ziply.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common")
public class CommonController {
    @Autowired
    private CommonService commonService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(@RequestHeader ("Authorization") String jwt){
        try {
            String email = jwtUtils.extractUsername(jwt.substring(7));
            UserDto userProfile = commonService.getUserProfile(email);
            return ResponseEntity.ok().body(new ApiResponse("Success",userProfile));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.ok().body(new ApiResponse(e.getMessage(),null));
        }
    }
}
