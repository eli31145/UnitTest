package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Test10ArgumentCaptors {
    private BookingService bookingService;
    private PaymentService paymentServiceMock;

    private RoomService roomServiceMock;
    private BookingDAO bookingDAOMock;
    private MailSender mailSenderMock;
    private ArgumentCaptor<Double> doubleCaptor;
    //using ArgumentCaptor for objects
    //private ArgumentCaptor<BookingRequest> bookingRequestCaptor;

    @BeforeEach
    void setup() {
        this.roomServiceMock = mock(RoomService.class);
        this.paymentServiceMock = mock(PaymentService.class);
        this.bookingDAOMock = mock(BookingDAO.class);
        this.mailSenderMock = mock(MailSender.class);

        this.bookingService = new BookingService(paymentServiceMock, roomServiceMock, bookingDAOMock, mailSenderMock);
        this.doubleCaptor = ArgumentCaptor.forClass(Double.class);
    }

    @Test
    void should_PayCorrectPrice_When_InputOK() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, true);

        //when
        bookingService.makeBooking(bookingRequest);

        //then
            //argument captors are like argument matchers, so other params need to adjust any()/eq()
        verify(paymentServiceMock).pay(eq(bookingRequest), doubleCaptor.capture());
        double capturedArgument = doubleCaptor.getValue();

        assertEquals(400.0, capturedArgument);
    }

    @Test
    void should_PayCorrectPrices_When_MultipleCalls() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, true);
        BookingRequest bookingRequest2 = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2),
                2, true);
        List<Double> expectedValues = Arrays.asList(400.0, 100.0);

        //when
        bookingService.makeBooking(bookingRequest);
        bookingService.makeBooking(bookingRequest2);

        //then
        //argument captors are like argument matchers, so other params need to adjust any()/eq()
        verify(paymentServiceMock, times(2)).pay(any(), doubleCaptor.capture());
            //if you invoke getValue(), only last arg will be saved aka value from bookingRequest2, not bookingRequest
        //double capturedArgument = doubleCaptor.getValue();
        List<Double> capturedArguments = doubleCaptor.getAllValues();

        assertEquals(expectedValues, capturedArguments);
    }

}
