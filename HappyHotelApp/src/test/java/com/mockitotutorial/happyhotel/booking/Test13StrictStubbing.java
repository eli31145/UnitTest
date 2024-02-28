package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

//Using MockitoExtension turns on Strict Stubbing which is deactivated generally by default
//Stubbing aka defining some sort of behaviour
@ExtendWith(MockitoExtension.class)
public class Test13StrictStubbing {
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

    //with Strict Stubbing, it ensures you have to use the mocked methods defined in the "given" section
    //Test Will Fail
    //@Test
    void should_InvokePayment_When_Prepaid() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, false);

        //when() will fail as this function will never be called since prepaid = false
            //if define some behaviour, but never use it later, Mockito (with the MockitoExtension) will detect such
            //unnecessary behaviour definition aka stubbing and throw exception, failing the test
            //to bypass, can use Lenient().when().then() BUT not good practice
        when(paymentServiceMock.pay(any(), anyDouble())).thenReturn("1");

        //when
        bookingService.makeBooking(bookingRequest);

        //then
        //no exception is thrown
    }

}
