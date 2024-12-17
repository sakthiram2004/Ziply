package com.sr.Ziply.controller.admin;

import com.sr.Ziply.model.Category;
import com.sr.Ziply.model.User;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.service.UserService;
import com.sr.Ziply.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category ,
                                                      @RequestHeader("Authorization") String jwt)throws Exception{
        User user = userService.findUserByJwt(jwt.substring(7));
        Category createdCategory = categoryService.createCategory(category.getName(),user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("category created ",createdCategory));
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getRestaurantCategory(
                                                      @RequestHeader("Authorization") String jwt)throws Exception{
        User user = userService.findUserByJwt(jwt.substring(7));
        List<Category> createdCategory = categoryService.findCategoryByRestaurantId(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Categories",createdCategory));
    }
}
