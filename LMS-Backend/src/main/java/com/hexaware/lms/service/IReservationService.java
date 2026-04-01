package com.hexaware.lms.service;

import com.hexaware.lms.entity.Reservation;
import java.util.List;

public interface IReservationService {

	
	public int reserveBook(int userId, long bookId);

	public List<Reservation> getReservationsByUser(int userId);

    List<Reservation> getAllReservations();

    public int cancelReservation(int reservationId);
}
