package com.hexaware.lms.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.service.IReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    IReservationService reservationService;

    @PostMapping("/reserve")
    public org.springframework.http.ResponseEntity<?> reserveBook(@RequestParam int userId,
                                           @RequestParam long bookId) {
    	
    	Reservation reservation = new Reservation();

    	reservation.setReservationDate(LocalDate.now());
    	reservation.setStatus("ACTIVE");

        int result = reservationService.reserveBook(userId, bookId);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Book Reserved Successfully");
        else
            return org.springframework.http.ResponseEntity.badRequest().body(java.util.Map.of("message", "can't reserve/borrow more than 5 books remove unwanted to add new"));

        return org.springframework.http.ResponseEntity.ok(response);
    }

    @GetMapping("/getuser/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable int userId) {

        return reservationService.getReservationsByUser(userId);
    }

    @GetMapping("/getallusers")
    public List<Reservation> getAllReservations() {

        return reservationService.getAllReservations();
    }

    @DeleteMapping("/canceluser/{reservationId}")
    public Map<String, String> cancelReservation(@PathVariable int reservationId) {

    	Reservation reservation = new Reservation();
    	reservation.setStatus("CANCELLED");
    	
        int result = reservationService.cancelReservation(reservationId);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Reservation Cancelled Successfully");
        else
            response.put("message", "Cancellation Failed");

        return response;
    }
}