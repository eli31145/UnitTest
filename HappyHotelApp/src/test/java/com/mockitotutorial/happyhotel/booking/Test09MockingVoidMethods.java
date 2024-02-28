package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class Test09MockingVoidMethods {
    private BookingService bookingService;
    private PaymentService paymentServiceMock;

    private RoomService roomServiceMock;
    private BookingDAO bookingDAOMock;
    private MailSender mailSenderMock;

    @BeforeEach
    void setup() {
        this.roomServiceMock = mock(RoomService.class);
        this.paymentServiceMock = mock(PaymentService.class);
        this.bookingDAOMock = mock(BookingDAO.class);
        this.mailSenderMock = mock(MailSender.class);

        this.bookingService = new BookingService(paymentServiceMock, roomServiceMock, bookingDAOMock, mailSenderMock);
    }

    @Test
    void should_ThrowException_When_MailNotReady() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, false);

        //cannot use when() with methods that return void
        //eg. when(mailSenderMock.sendBookingConfirmation(any())).thenThrow(BusinessException.class);
        //use doThrow()
        doThrow(new BusinessException()).when(mailSenderMock).sendBookingConfirmation(any());

        //when
        Executable executable = () -> bookingService.makeBooking(bookingRequest);

        //then
        assertThrows(BusinessException.class, executable);
    }

    @Test
    void should_NotThrowException_When_MailNotReady() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, false);

        //doNothing() is default for void methods so even if you not run that line, makes no difference
        doNothing().when(mailSenderMock).sendBookingConfirmation(any());

        //when
        bookingService.makeBooking(bookingRequest);

        //then
        //no exception thrown
    }
}
