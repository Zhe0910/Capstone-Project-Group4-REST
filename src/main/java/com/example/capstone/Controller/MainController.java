package com.example.capstone.Controller;

import com.example.capstone.DataAccess.*;
import com.example.capstone.POJOS.*;
import com.example.capstone.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = RESTNamebook.VERSION_1)
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private AutoRepository autoRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private HomeownerRepository homeownerRepository;
    @Autowired
    private AutoQuoteRepository autoQuoteRepository;
    @Autowired
    private HomeQuoteRepository homeQuoteRepository;
    @Autowired
    private AutoPolicyRepository autoPolicyRepository;
    @Autowired
    private HomePolicyRepository homePolicyRepository;

    /**
     * Get Mapping for all Users - read all users
     *
     * @return Iterable of all users
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS)
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get Mapping for User based on ID
     *
     * @param user_id user id
     * @return user object
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}")
    public @ResponseBody Optional<User> getUserWithId(@PathVariable Integer user_id) {
        return userRepository.findById(user_id);
    }

    /**
     * Post Mapping for User - create user
     *
     * @param name  name of user
     * @param email email of user
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS)
    public @ResponseBody Integer addNewUser(@RequestParam String name, @RequestParam String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * Put Mapping for User based on ID - update user
     *
     * @param user_id user id
     * @param name    name of user
     * @param email   email of user
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PutMapping(path = RESTNamebook.USERS + "/{user_id}")
    public @ResponseBody String updateUser(@PathVariable Integer user_id, @RequestParam String name, @RequestParam String email) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
            return user.getName() + " has been updated.";
        } else {
            return "User not found.";
        }
    }

    /**
     * Delete Mapping for User based on ID - delete user
     *
     * @param user_id user id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}")
    public @ResponseBody String deleteUser(@PathVariable Integer user_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.deleteById(user_id);
            return user.getName() + " has been deleted from the database.";
        } else {
            return "User not found.";
        }
    }

    /**
     * Get Mapping for Home - read all homes
     *
     * @return all homes
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.HOMES)
    public @ResponseBody Iterable<Home> getAllHomes() {
        return homeRepository.findAll();
    }

    /**
     * Get Mapping for Home based on ID - read homes by user id
     *
     * @param user_id user id
     * @return home object
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMES)
    public @ResponseBody Iterable<Home> getAllHomesByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<Home> homes = new LinkedList<>();

        if (user.isPresent()) {
            homes = homeRepository.getAllByUserId(user.get().getId());
        }
        return homes;
    }

    /**
     * Post Mapping for Home - create home
     *
     * @param user_id      user id
     * @param dateBuilt    date built
     * @param value        value
     * @param dwellingType dwelling type
     * @param heatingType  heating type
     * @param location     location
     * @return home id or 0 if user not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMES)
    public @ResponseBody Integer addNewHome(
            @PathVariable(name = "user_id") Integer user_id,
            @RequestParam LocalDate dateBuilt,
            @RequestParam double value,
            @RequestParam("dwellingType") String dwellingType,
            @RequestParam("heatingType") String heatingType,
            @RequestParam("location") String location) {
        Home home = new Home();
        home.setDateBuilt(dateBuilt);
        home.setValue(value);
        home.setDwellingType(DwellingType.valueOf(dwellingType));
        home.setHeatingType(HeatingType.valueOf(heatingType));
        home.setLocation(Location.valueOf(location));

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            home.setUser(optionalUser.get());
            homeRepository.save(home);
            return home.getId();
        } else {
            return 0;
        }
    }

    /**
     * Put Mapping for Home based on ID - update home
     *
     * @param user_id      user id
     * @param home_id      home id
     * @param dateBuilt    date built
     * @param value        value
     * @param dwellingType dwelling type
     * @param heatingType  heating type
     * @param location     location
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PutMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMES + "/{home_id}")
    public @ResponseBody String updateHome(@PathVariable Integer user_id, @PathVariable Integer home_id,
                                           @RequestParam LocalDate dateBuilt,
                                           @RequestParam double value,
                                           @RequestParam("dwellingType") String dwellingType,
                                           @RequestParam("heatingType") String heatingType,
                                           @RequestParam("location") String location) {

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<Home> optionalHome = homeRepository.findById(home_id);
            if (optionalHome.isPresent()) {
                Home home = optionalHome.get();
                home.setDateBuilt(dateBuilt);
                home.setValue(value);
                home.setDwellingType(DwellingType.valueOf(dwellingType));
                home.setHeatingType(HeatingType.valueOf(heatingType));
                home.setLocation(Location.valueOf(location));
                homeRepository.save(home);
                return "The home has been updated in the database.";
            } else {
                return "Home not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Delete Mapping for Home based on ID - delete home
     *
     * @param user_id user id
     * @param home_id home id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMES + "/{home_id}")
    public @ResponseBody String deleteHome(@PathVariable Integer user_id, @PathVariable Integer home_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<Home> optionalHome = homeRepository.findById(home_id);
            if (optionalHome.isPresent()) {
                homeRepository.deleteById(home_id);
                return "The home has been deleted from the database.";
            } else {
                return "Home not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Get Mapping for Auto - read all autos
     *
     * @return Iterable of all autos in database
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.AUTOS)
    public @ResponseBody Iterable<Vehicle> getAllAutos() {
        return autoRepository.findAll();
    }

    /**
     * Get Mapping for Auto based on ID - read autos by user id
     *
     * @param user_id user id
     * @return Iterable of all autos by user
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOS)
    public @ResponseBody Iterable<Vehicle> getAllAutosByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<Vehicle> autos = new LinkedList<>();

        if (user.isPresent()) {
            autos = autoRepository.getAllByUserId(user.get().getId());
        }
        return autos;
    }

    /**
     * Post Mapping for Auto - create auto
     *
     * @param user_id user id
     * @param year    year
     * @param model   model
     * @param make    make
     * @return auto ID or 0 if user not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOS)
    public @ResponseBody Integer addNewAuto(@PathVariable(name = "user_id") Integer user_id,
                                           @RequestParam int year,
                                           @RequestParam String model,
                                           @RequestParam String make) {
        Vehicle vehicle = new Vehicle();
        vehicle.setYear(year);
        vehicle.setModel(model);
        vehicle.setMake(make);

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            vehicle.setUser(optionalUser.get());
            autoRepository.save(vehicle);
            return vehicle.getId();
        } else {
            return 0;
        }
    }

    /**
     * Put Mapping for Auto based on ID - update auto
     *
     * @param user_id user id
     * @param auto_id auto id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PutMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOS + "/{auto_id}")
    public @ResponseBody String updateAuto(@PathVariable(name = "user_id") Integer user_id,
                                           @PathVariable(name = "auto_id") Integer auto_id,
                                           @RequestParam int year,
                                           @RequestParam String model,
                                           @RequestParam String make) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<Vehicle> optionalAuto = autoRepository.findById(auto_id);
            if (optionalAuto.isPresent()) {
                Vehicle vehicle = optionalAuto.get();
                vehicle.setYear(year);
                vehicle.setModel(model);
                vehicle.setMake(make);
                autoRepository.save(vehicle);
                return "The auto has been updated in the database.";
            } else {
                return "Auto not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Delete Mapping for Auto based on ID - delete auto
     *
     * @param user_id user id
     * @param auto_id auto id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOS + "/{auto_id}")
    public @ResponseBody String deleteAuto(@PathVariable Integer user_id,
                                           @PathVariable Integer auto_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<Vehicle> optionalAuto = autoRepository.findById(auto_id);
            if (optionalAuto.isPresent()) {
                autoRepository.deleteById(auto_id);
                return "The auto has been deleted from the database.";
            } else {
                return "Auto not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Get all drivers - read all drivers
     *
     * @return return all drivers
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.DRIVERS)
    public @ResponseBody Iterable<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    /**
     * Get drivers by ID - read drivers by user id
     *
     * @param user_id user id
     * @return return all drivers by user ID
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.DRIVERS)
    public @ResponseBody Iterable<Driver> getAllDriversByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<Driver> drivers = new LinkedList<>();

        if (user.isPresent()) {
            drivers = driverRepository.getAllByUserId(user.get().getId());
        }
        return drivers;
    }

    /**
     * Add a new driver - create a new user
     *
     * @param user_id         user id
     * @param age             age
     * @param address         address
     * @param numberAccidents number of accidents
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.DRIVERS)
    public @ResponseBody String addNewDriver(@PathVariable(name = "user_id") Integer user_id,
                                             @RequestParam int age,
                                             @RequestParam String address,
                                             @RequestParam int numberAccidents) {
        Driver driver = new Driver();
        driver.setAge(age);
        driver.setAddress(address);
        driver.setNumberAccidents(numberAccidents);

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            driver.setUser(optionalUser.get());
            driverRepository.save(driver);
            return "The driver has been saved into the database.";
        } else {
            return "The driver failed to be saved into the database.";
        }
    }

    /**
     * Put Mapping for Driver based on ID - update driver
     *
     * @param user_id   user id
     * @param driver_id driver id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PutMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.DRIVERS + "/{driver_id}")
    public @ResponseBody String updateDriver(@PathVariable(name = "user_id") Integer user_id,
                                             @PathVariable(name = "driver_id") Integer driver_id,
                                             @RequestParam int age,
                                             @RequestParam String address,
                                             @RequestParam int numberAccidents) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<Driver> optionalDriver = driverRepository.findById(driver_id);
            if (optionalDriver.isPresent()) {
                Driver driver = optionalDriver.get();
                driver.setAge(age);
                driver.setAddress(address);
                driver.setNumberAccidents(numberAccidents);
                driverRepository.save(driver);
                return "The driver has been updated in the database.";
            } else {
                return "Driver not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Delete Mapping for Driver based on ID - delete driver
     *
     * @param user_id   user id
     * @param driver_id driver id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.DRIVERS + "/{driver_id}")
    public @ResponseBody String deleteDriver(@PathVariable Integer user_id,
                                             @PathVariable Integer driver_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<Driver> optionalDriver = driverRepository.findById(driver_id);
            if (optionalDriver.isPresent()) {
                driverRepository.deleteById(driver_id);
                return "The driver has been deleted from the database.";
            } else {
                return "Driver not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Get all homeowners - read all homeowners
     * @return return all homeowners
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.HOMEOWNERS)
    public @ResponseBody Iterable<HomeOwner> getAllHomeowners() {
        return homeownerRepository.findAll();
    }

    /**
     * Get homeowners by ID - read homeowners by user id
     * @param user_id user id
     * @return return all homeowners by user ID
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEOWNERS)
    public @ResponseBody Iterable<HomeOwner> getAllHomeownersByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<HomeOwner> homeowners = new LinkedList<>();

        if (user.isPresent()) {
            homeowners = homeownerRepository.getAllByUserId(user.get().getId());
        }
        return homeowners;
    }

    /**
     * Post Mapping for Homeowner - create a new homeowner
     * @param user_id user id
     * @param age age
     * @param address address
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEOWNERS)
    public @ResponseBody String addNewHomeowner(@PathVariable(name = "user_id") Integer user_id,
                                                @RequestParam int age,
                                                @RequestParam String address) {
        HomeOwner homeowner = new HomeOwner();
        homeowner.setAge(age);
        homeowner.setAddress(address);

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            homeowner.setUser(optionalUser.get());
            homeownerRepository.save(homeowner);
            return "The homeowner has been saved into the database.";
        } else {
            return "The homeowner failed to be saved into the database.";
        }
    }

    /**
     * Put Mapping for Homeowner based on ID - update homeowner
     * @param user_id user id
     * @param homeowner_id homeowner id
     * @param age age
     * @param address address
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @PutMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEOWNERS + "/{homeowner_id}")
    public @ResponseBody String updateHomeowner(@PathVariable(name = "user_id") Integer user_id,
                                                @PathVariable(name = "homeowner_id") Integer homeowner_id,
                                                @RequestParam int age,
                                                @RequestParam String address) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<HomeOwner> optionalHomeowner = homeownerRepository.findById(homeowner_id);
            if (optionalHomeowner.isPresent()) {
                HomeOwner homeowner = optionalHomeowner.get();
                homeowner.setAge(age);
                homeowner.setAddress(address);
                homeownerRepository.save(homeowner);
                return "The homeowner has been updated in the database.";
            } else {
                return "Homeowner not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Delete Mapping for Homeowner based on ID - delete homeowner
     * @param user_id user id
     * @param homeowner_id homeowner id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEOWNERS + "/{homeowner_id}")
    public @ResponseBody String deleteHomeowner(@PathVariable Integer user_id,
                                                @PathVariable Integer homeowner_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            Optional<HomeOwner> optionalHomeowner = homeownerRepository.findById(homeowner_id);
            if (optionalHomeowner.isPresent()) {
                homeownerRepository.deleteById(homeowner_id);
                return "The homeowner has been deleted from the database.";
            } else {
                return "Homeowner not found.";
            }
        } else {
            return "User not found.";
        }
    }

    /**
     * Get all auto quotes - read all auto quotes
     * @return return all auto quotes
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS  + RESTNamebook.AUTOQUOTES)
    public @ResponseBody Iterable<AutoQuote> getAllAutoQuotes() {
        return autoQuoteRepository.findAll();
    }

    /**
     * Get all auto quotes by user - read all auto quotes by user ID
     * @param user_id user id
     * @return return all auto quotes by user
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOQUOTES)
    public @ResponseBody Iterable<AutoQuote> getAllAutoQuotesByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<AutoQuote> autoQuotes = new LinkedList<>();

        if (user.isPresent()) {
            autoQuotes = autoQuoteRepository.getAllByUserId(user.get().getId());
        }
        return autoQuotes;
    }

    /**
     * Get an auto quotes by quote ID - read an auto quote by quote ID
     * @param autoquote_id auto id
     * @return return an auto quote by quote ID
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS  + RESTNamebook.AUTOQUOTES + "/{autoquote_id}")
    public @ResponseBody Optional<AutoQuote> getAutoQuoteByID(@PathVariable(name = "autoquote_id") Integer autoquote_id){
        return autoQuoteRepository.findById(autoquote_id);
    }

    /**
     * Post Mapping for AutoQuote - Add a new auto quote
     * @param user_id user id
     * @param auto_id auto id
     * @return auto quote id or 0 if auto is not found, or -1 if driver is not found, or -2 if user is not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOQUOTES + "/{auto_id}")
    public @ResponseBody Integer addNewAutoQuote(@PathVariable(name = "user_id") Integer user_id,
                                                @PathVariable(name = "auto_id") Integer auto_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Driver> driver = driverRepository.getDriverByUserId(user_id);
        Optional<Vehicle> auto = autoRepository.findById(auto_id);
        if (user.isPresent()){
            if (driver.isPresent()){
                if (auto.isPresent()){
                    AutoQuote autoQuote = AutoQuoteFactory.createAutoQuote(auto.get(), driver.get());
                    autoQuote.setUser(user.get());
                    autoQuoteRepository.save(autoQuote);
                    return autoQuote.getId();
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    /**
     * Delete Mapping for AutoQuote based on ID - delete auto quote
     * @param user_id user id
     * @param autoquote_id auto quote id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOQUOTES + "/{autoquote_id}")
    public @ResponseBody String deleteAutoQuote(@PathVariable(name = "user_id") Integer user_id,
                                                @PathVariable(name = "autoquote_id") Integer autoquote_id) {
        Optional<AutoQuote> autoQuote = autoQuoteRepository.findById(autoquote_id);
        if (autoQuote.isPresent()){
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent() && Objects.equals(autoQuote.get().getInsuredPerson().getUser().getId(), user.get().getId())){
                autoQuoteRepository.deleteById(autoquote_id);
                return "The auto quote has been cancelled.";
            } else {
                return "User not found.";
            }
        } else {
            return "Auto quote not found.";
        }
    }

    /**
     * Get all home quotes - read all home quotes
     * @return return all home quotes
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS  + RESTNamebook.HOMEQUOTES)
    public @ResponseBody Iterable<HomeQuote> getAllHomeQuotes() {
        return homeQuoteRepository.findAll();
    }

    /**
     * Get all home quotes by user - read all home quotes by user ID
     * @param user_id user id
     * @return return all home quotes by user
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEQUOTES)
    public @ResponseBody Iterable<HomeQuote> getAllHomeQuotesByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<HomeQuote> homeQuotes = new LinkedList<>();

        if (user.isPresent()) {
            homeQuotes = homeQuoteRepository.getAllByUserId(user.get().getId());
        }
        return homeQuotes;
    }

    /**
     * Get home quote by id - read a home quote by id
     * @param homequote_id home quote id
     * @return return home quote by id
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS  + RESTNamebook.HOMEQUOTES + "/{homequote_id}")
    public @ResponseBody Optional<HomeQuote> getHomeQuoteByID(@PathVariable(name = "homequote_id") Integer homequote_id){
        return homeQuoteRepository.findById(homequote_id);
    }

    /**
     * Post Mapping for HomeQuote - create a new home quote
     * @param user_id user id
     * @param home_id home id
     * @return home quote id or 0 if home is not found, or -1 if homeowner is not found, or -2 if user is not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEQUOTES + "/{home_id}")
    public @ResponseBody Integer addNewHomeQuote(@PathVariable(name = "user_id") Integer user_id,
                                                @PathVariable(name = "home_id") Integer home_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<HomeOwner> homeowner = homeownerRepository.getHomeOwnerByUserId(user_id);
        Optional<Home> home = homeRepository.findById(home_id);
        if (user.isPresent()){
            if (homeowner.isPresent()){
                if (home.isPresent()){
                    HomeQuote homeQuote = HomeQuoteFactory.createHomeQuote(home.get(), homeowner.get());
                    homeQuote.setUser(user.get());
                    homeQuoteRepository.save(homeQuote);
                    return homeQuote.getId();
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    /**
     * Delete Mapping for HomeQuote based on ID - delete home quote
     * @param user_id user id
     * @param homequote_id home quote id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEQUOTES + "/{homequote_id}")
    public @ResponseBody String deleteHomeQuote(@PathVariable(name = "user_id") Integer user_id,
                                                @PathVariable(name = "homequote_id") Integer homequote_id) {
        Optional<HomeQuote> homeQuote = homeQuoteRepository.findById(homequote_id);
        if (homeQuote.isPresent()) {
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent() && Objects.equals(homeQuote.get().getInsuredPerson().getUser().getId(), user.get().getId())) {
                homeQuoteRepository.deleteById(homequote_id);
                return "The home quote has been cancelled.";
            } else {
                return "User not found.";
            }
        } else {
            return "Home quote not found.";
        }
    }

    /**
     * Get all auto policies - read all auto policies
     * @return return all auto policies
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.AUTOPOLICIES)
    public @ResponseBody Iterable<AutoPolicy> getAllAutoPolicies() {
        return autoPolicyRepository.findAll();
    }

    /**
     * Get all auto policies by user - read all auto policies by user ID
     * @param user_id user id
     * @return return all auto policies by user
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOPOLICIES)
    public @ResponseBody Iterable<AutoPolicy> getAllAutoPoliciesByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<AutoPolicy> autoPolicies = new LinkedList<>();

        if (user.isPresent()) {
            autoPolicies = autoPolicyRepository.getAllByUserId(user.get().getId());
        }
        return autoPolicies;
    }

    /**
     * Get auto policy by id - read an auto policy by id
     * @param autopolicy_id auto policy id
     * @return return auto policy by id
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.AUTOPOLICIES + "/{autopolicy_id}")
    public @ResponseBody Optional<AutoPolicy> getAutoPolicyById(@PathVariable(name = "autopolicy_id") Integer autopolicy_id) {
        return autoPolicyRepository.findById(autopolicy_id);
    }

    /**
     * Post Mapping for AutoPolicy - Add a new auto policy
     * @param user_id user id
     * @param autoquote_id auto quote id
     * @return auto policy id or 0 if auto quote is not found, or -1 if user is not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOPOLICIES + "/{autoquote_id}")
    public @ResponseBody Integer addNewAutoPolicy(@PathVariable(name = "user_id") Integer user_id,
                                                 @PathVariable(name = "autoquote_id") Integer autoquote_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<AutoQuote> autoQuote = autoQuoteRepository.findById(autoquote_id);
        if (user.isPresent()){
            if (autoQuote.isPresent()){
                AutoPolicy autoPolicy = AutoPolicyFactory.createAutoPolicy(autoQuote.get());
                autoPolicy.setUser(user.get());
                autoPolicyRepository.save(autoPolicy);
                return autoPolicy.getId();
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * Post Mapping for AutoPolicy based on ID - renew auto policy
     * @param user_id user id
     * @param autopolicy_id auto policy id
     * @return auto policy id or 0 if new auto policy and current auto policy is the same, or -1 if auto policy is not found,
     * or -2 if user is not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOPOLICIES + RESTNamebook.RENEW + "/{autopolicy_id}")
    public @ResponseBody Integer renewAutoPolicy(@PathVariable(name = "user_id") Integer user_id,
                                                 @PathVariable(name = "autopolicy_id") Integer autopolicy_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<AutoPolicy> autoPolicy = autoPolicyRepository.findById(autopolicy_id);
        if (user.isPresent()){
            if (autoPolicy.isPresent()){
                AutoPolicy newAutoPolicy = AutoPolicyFactory.renewAutoPolicy(autoPolicy.get());
                if (!Objects.equals(newAutoPolicy.getId(), autoPolicy.get().getId())) {
                    autoPolicyRepository.delete(autoPolicy.get());
                    autoPolicyRepository.save(newAutoPolicy);
                    return newAutoPolicy.getId();
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    /**
     * Delete Mapping for AutoPolicy based on ID - delete auto policy
     * @param user_id user id
     * @param autopolicy_id auto policy id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.AUTOPOLICIES + "/{autopolicy_id}")
    public @ResponseBody String deleteAutoPolicy(@PathVariable(name = "user_id") Integer user_id,
                                                 @PathVariable(name = "autopolicy_id") Integer autopolicy_id) {
        Optional<AutoPolicy> autoPolicy = autoPolicyRepository.findById(autopolicy_id);
        if (autoPolicy.isPresent()) {
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent() && Objects.equals(autoPolicy.get().getInsuredPerson().getUser().getId(), user.get().getId())) {
                autoPolicyRepository.deleteById(autopolicy_id);
                return "The auto policy has been cancelled.";
            } else {
                return "User not found.";
            }
        } else {
            return "Auto policy not found.";
        }
    }

    /**
     * Get all home policies - read all home policies
     * @return return all home policies
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.HOMEPOLICIES)
    public @ResponseBody Iterable<HomePolicy> getAllHomePolicies() {
        return homePolicyRepository.findAll();
    }

    /**
     * Get all home policies by user - read all home policies by user
     * @param user_id user id
     * @return return all home policies by user ID
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEPOLICIES)
    public @ResponseBody Iterable<HomePolicy> getAllHomePoliciesByUser(@PathVariable(name = "user_id") Integer user_id) {
        Optional<User> user = userRepository.findById(user_id);
        Iterable<HomePolicy> homePolicies = new LinkedList<>();

        if (user.isPresent()) {
            homePolicies = homePolicyRepository.getAllByUserId(user.get().getId());
        }
        return homePolicies;
    }

    /**
     * Get Mapping for HomePolicy based on ID - read home policy by policy id
     * @param homepolicy_id home policy id
     * @return home policy
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = RESTNamebook.USERS + RESTNamebook.HOMEPOLICIES + "/{homepolicy_id}")
    public @ResponseBody Optional<HomePolicy> getHomePolicyById(@PathVariable(name = "homepolicy_id") Integer homepolicy_id) {
        return homePolicyRepository.findById(homepolicy_id);
    }

    /**
     * Post Mapping for HomePolicy based on ID - create a home policy
     * @param user_id user id
     * @param homequote_id home policy id
     * @return home policy id or 0 if home quote is not found, or -1 if user is not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEPOLICIES + "/{homequote_id}")
    public @ResponseBody Integer addNewHomePolicy(@PathVariable(name = "user_id") Integer user_id,
                                                 @PathVariable(name = "homequote_id") Integer homequote_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<HomeQuote> homeQuote = homeQuoteRepository.findById(homequote_id);
        if (user.isPresent()){
            if (homeQuote.isPresent()){
                HomePolicy homePolicy = HomePolicyFactory.createHomePolicy(homeQuote.get());
                homePolicy.setUser(user.get());
                homePolicyRepository.save(homePolicy);
                return homePolicy.getId();
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * Post Mapping for HomePolicy based on ID - renew home policy
     * @param user_id user id
     * @param homepolicy_id home policy id
     * @return new hom policy id, or 0 if the new policy id and the current policy id are the same, or
     * -1 if the home policy is not found, or -2 if the user is not found
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEPOLICIES + RESTNamebook.RENEW + "/{homepolicy_id}")
    public @ResponseBody Integer renewHomePolicy(@PathVariable(name = "user_id") Integer user_id,
                                                  @PathVariable(name = "homepolicy_id") Integer homepolicy_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<HomePolicy> homePolicy = homePolicyRepository.findById(homepolicy_id);
        if (user.isPresent()){
            if (homePolicy.isPresent()){
                HomePolicy newHomePolicy = HomePolicyFactory.renewHomePolicy(homePolicy.get());
                if (!Objects.equals(newHomePolicy.getId(), homePolicy.get().getId())) {
                    homePolicyRepository.delete(homePolicy.get());
                    homePolicyRepository.save(newHomePolicy);
                    return newHomePolicy.getId();
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    /**
     * Delete Mapping for HomePolicy based on ID - delete home policy
     * @param user_id user id
     * @param homepolicy_id home policy id
     * @return message stating success / failure
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = RESTNamebook.USERS + "/{user_id}" + RESTNamebook.HOMEPOLICIES + "/{homepolicy_id}")
    public @ResponseBody String deleteHomePolicy(@PathVariable(name = "user_id") Integer user_id,
                                                 @PathVariable(name = "homepolicy_id") Integer homepolicy_id) {
        Optional<HomePolicy> homePolicy = homePolicyRepository.findById(homepolicy_id);
        if (homePolicy.isPresent()) {
            Optional<User> user = userRepository.findById(user_id);
            if (user.isPresent()) {
                homePolicyRepository.deleteById(homepolicy_id);
                return "The home policy has been cancelled.";
            } else {
                return "User not found.";
            }
        } else {
            return "Home policy not found.";
        }
    }
}