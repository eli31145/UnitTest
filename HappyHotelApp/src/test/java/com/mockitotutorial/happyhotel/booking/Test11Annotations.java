package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//use @ExtendWith(MockitoExtension.class) for all @Mock/@InjectMocks etc. annotations to work
//Extensions replaced runners aka @RunWith for JUnit4
@ExtendWith(MockitoExtension.class)
public class Test11Annotations {
    //though name is @InjectMocks, will inject @spy if needed
    @InjectMocks
    private BookingService bookingService;
    @Mock
    private PaymentService paymentServiceMock;
    @Mock
    private RoomService roomServiceMock;
    @Mock
    private BookingDAO bookingDAOMock;
    @Mock
    private MailSender mailSenderMock;
    @Captor
    private ArgumentCaptor<Double> doubleCaptor;

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
        verify(paymentServiceMock, times(2)).pay(any(), doubleCaptor.capture());
        List<Double> capturedArguments = doubleCaptor.getAllValues();

        assertEquals(expectedValues, capturedArguments);
    }

}
