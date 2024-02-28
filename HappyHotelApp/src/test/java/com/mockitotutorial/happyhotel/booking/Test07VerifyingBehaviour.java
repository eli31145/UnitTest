package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Test07VerifyingBehaviour {
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
    void should_InvokePayment_When_Prepaid() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, true);

        //when
        bookingService.makeBooking(bookingRequest);

        //then
        verify(paymentServiceMock).pay(bookingRequest, 400.0);
            //verify that a certain function was called X times

        //verify(paymentServiceMock, times(1)).pay(bookingRequest, 400.0);
        //checks to see any other methods from provided obj aka paymentServiceMock is called
        //verify good to check if a certain mock was called or function was called
        verifyNoMoreInteractions(paymentServiceMock);
    }

    @Test
    void should_NotInvokePayment_When_NotPrepaid() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, false);

        //when
        bookingService.makeBooking(bookingRequest);
        //then
            //You can get "Invalid use of argument matchers" error using verify(). Cannot mix matchers with real values,
            //eg. pay(bookingRequest, 400.0). Use either any() or eq() OR both real values
        verify(paymentServiceMock, never()).pay(any(), anyDouble());
        verify(paymentServiceMock, never()).pay(bookingRequest, 400.0);
    }

}
