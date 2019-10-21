package ro.siit.travel.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.travel.model.Trip;
import ro.siit.travel.model.User;
import ro.siit.travel.persistence.TripRepository;
import ro.siit.travel.persistence.UserRepository;
import ro.siit.travel.service.FileService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * The TripController  Class
 *
 * @author Georgiana Simpetreanu
 * @version 1.0
 * Date 10/21/2019.
 */
@Controller
public class TripController {

    private static final Logger logger = LoggerFactory.getLogger(TripController.class);
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;


    /**
     *Shows the home page with all trips
     */

    @RequestMapping(value ="/newhome", method = RequestMethod.GET)
    public ModelAndView newHome(@RequestParam(name = "id", required = false) Integer id){
        SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<Trip> trips = user.getTrips();
        Trip currentTrip = null;
        if(id != null) {
            for(Trip trip: trips) {
                if (trip.getId()==id) {
                    currentTrip = trip;
                    break;
                }
            }
        }
        if(currentTrip == null) {
            if(trips.size()>0) {
                currentTrip = trips.iterator().next();
            } else {
                return new ModelAndView("redirect:/new");
            }
        }

        ModelAndView model = new ModelAndView("home");
        model.addObject("trips", trips);
        model.addObject("selectedId", currentTrip.getId());
        model.addObject("currentTrip", currentTrip);
        return model;
    }

    /**
     * Add a new trip
     * @param model
     * @return new_trip template
     */

    @RequestMapping("/new")
    public String showNewTripPage(Model model){
        Trip trip = new Trip();
        model.addAttribute("newtrip", trip);
        return "new_trip";
    }

    /**
     * Save a new trip
     * @param trip
     * @param result
     * @return after saving, redirect to home page
     */


//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String saveTrip(@ModelAttribute("newtrip") Trip trip,
//                           BindingResult result) {
//        if (result.hasErrors()) {
//            return "error";
//        }
//        trip.setUser(userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName()));
//        Trip saved = tripRepository.save(trip);
//        return "redirect:/newhome";
//    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveTrip(Model model,
                           @ModelAttribute("newtrip") Trip trip,
                           BindingResult result,
                           @RequestParam("file") MultipartFile[] file) {
        if (result.hasErrors()) {
            return "error";
        }
        trip.setPhoto1(UUID.randomUUID().toString());
        trip.setPhoto2 (UUID.randomUUID().toString());
        trip.setUser(userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName()));
        Trip saved = tripRepository.save(trip);
        fileService.store(trip.getPhoto1(), file[0]);
        fileService.store(trip.getPhoto2(), file[1]);
//        model.addAttribute("UUID1", trip.getPhoto1());
//        model.addAttribute("UUID2", trip.getPhoto2());
        return "redirect:/newhome";
    }

    /**
     * Edit Trip
     * @param model
     * @param id
     * @return edit_trip template
     * @throws IllegalArgumentException
     */

    @RequestMapping(path = {"/edit/{id}"}, method = RequestMethod.GET)
    public String editTrip(Model model, @PathVariable("id") Integer id)
            throws IllegalArgumentException
    {
            Optional<Trip> trip = tripRepository.findById(id);
            model.addAttribute("editTrip", trip.get());
        return "edit_trip";
    }

    @PostMapping("/edit/{id}")
    String updateTrip(@ModelAttribute("editTrip") Trip newTrip, @PathVariable Integer id,
                    BindingResult result) {

        if (result.hasErrors()) {
            return "error";
        }

        Optional<Trip> optionalTrip =tripRepository.findById(id);

        if(optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            trip.setName(newTrip.getName());
            trip.setImpression(newTrip.getImpression());
            trip.setStartDate(newTrip.getStartDate());
            trip.setEndDate(newTrip.getEndDate());
            trip.setLocation(newTrip.getLocation());
            tripRepository.save(trip);
            return "redirect:/newhome?id="+id;
        } else {
            return "error";
        }
    }

    /**
     * Delete trip
     */

    @RequestMapping ("/delete/{id}")
    public String deleteTrip(@PathVariable(name = "id")int id){
        tripRepository.deleteById(id);
        return "redirect:/newhome";
    }

}
