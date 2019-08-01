package techcourse.myblog.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.user.dto.UserCreateDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.dto.UserUpdateDto;
import techcourse.myblog.user.exception.InvalidEditFormException;
import techcourse.myblog.user.exception.InvalidSignUpFormException;
import techcourse.myblog.user.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String renderSignUpPage() {
        return "signup";
    }

    @PostMapping("/users")
    public RedirectView createUser(@Valid UserCreateDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSignUpFormException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.save(userDto);
        return new RedirectView("/login");
    }

    @GetMapping("/users")
    public String readUsers(Model model) {
        List<UserResponseDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/mypage/{userId}")
    public String renderMyPage(@PathVariable long userId, Model model) {
        UserResponseDto user = userService.findById(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/mypage/{userId}/edit")
    public String renderEditMyPage(@PathVariable long userId, UserSession session, Model model) {
        if (session.getId() !=  userId) {
            return "redirect:/";
        }
        model.addAttribute("user", userService.findById(userId));
        return "mypage-edit";
    }

    @PutMapping("/users/{userId}")
    public RedirectView updateUser(@PathVariable long userId, HttpSession session,
                                   @Valid UserUpdateDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidEditFormException(result.getFieldError().getDefaultMessage());
        }
        UserResponseDto updatedUser = userService.update(userId, userDto);
        session.setAttribute("user", updatedUser);
        return new RedirectView("/mypage/" + userId);
    }

    @DeleteMapping("/users/{userId}")
    public RedirectView deleteUser(@PathVariable long userId, UserSession session) {
        if (session.getId() != userId) {
            return new RedirectView("/");
        }
        userService.deleteById(userId);
        return new RedirectView("/logout");
    }
}