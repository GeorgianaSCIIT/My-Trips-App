package ro.siit.travel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.siit.travel.model.Trip;
import ro.siit.travel.model.User;
import ro.siit.travel.persistence.TripRepository;
import ro.siit.travel.persistence.UserRepository;
//import ro.siit.travel.utils.PassEncoding;
import ro.siit.travel.utils.PassEncoding;
import ro.siit.travel.utils.Roles;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
/**
 * The UserController  Class
 *
 * @author Georgiana Simpetreanu
 * @version 1.0
 * Date 10/21/2019.
 */

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    GlobalController globalController;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String root(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("root");
        return "login";
    }

    /**
     * Shows the login page
     * @param model
     * @return login template
     */

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("login");
        return "login";
    }

    /**
     * Shows the register page
     * @param model
     * @return register template
     */

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("register");
        return "register";
    }

    @RequestMapping(value = {"/user/register"}, method = RequestMethod.POST)
    public String register(@ModelAttribute("reqUser") User reqUser,
                           final RedirectAttributes redirectAttributes) {

        logger.info("/user/register");
        User user = userRepository.findByUserName(reqUser.getUserName());
        if (user != null) {
            redirectAttributes.addFlashAttribute("saveUser", "exist-name");
            return "redirect:/register";
        }
        user = userRepository.findByEmail(reqUser.getEmail());
        if (user != null) {
            redirectAttributes.addFlashAttribute("saveUser", "exist-email");
            return "redirect:/register";
        }

        reqUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(reqUser.getPassword()));
        reqUser.setRole(Roles.ROLE_USER.getValue());
        if (userRepository.save(reqUser) != null) {
            redirectAttributes.addFlashAttribute("saveUser", "success");
        } else {
            redirectAttributes.addFlashAttribute("saveUser", "fail");
        }

        return "redirect:/register";
    }

    /**
     * Shows user profile page
     * @param authentication
     * @param model
     * @return user_profile template
     */

    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public String currentUserName(Authentication authentication, Model model) {
        logger.info("/user/profile");
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User currentUser = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            model.addAttribute("currentUser", currentUser);
        }
        return "user_profile";
    }

    @RequestMapping(path = {"/editUser/{id}"}, method = RequestMethod.GET)
    public String editUser(Model model, @PathVariable("id") Integer id)
            throws IllegalArgumentException
    {
        User user = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("editUser", user);
        return "edit_user";
    }

    /**
     * Edit user
     * @param newUser
     * @param id
     * @param result
     * @return redirect to the user profile page
     */

    @PostMapping("/editUser/{id}")
    String updateUser(@ModelAttribute("editUser") User newUser, @PathVariable Integer id,
                      BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/user_profile";
        }
        User user = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setUserName(newUser.getUserName());
            user.setEmail(newUser.getEmail());
            user.setPassword(PassEncoding.getInstance().passwordEncoder.encode(newUser.getPassword()));
            user.setReconfirmPassword(newUser.getReconfirmPassword());
            user.setCity(newUser.getCity());
            user.setAdress(newUser.getAdress());
            user.setPhoneNumber(newUser.getPhoneNumber());
            userRepository.save(user);
            return "redirect:/user_profile";
    }


}
