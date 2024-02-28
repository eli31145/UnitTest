package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Test06ArgumentMatchers {
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
    void should_NotCompleteBooking_When_PriceTooHigh() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 5),
                2, true);
        //Can't use any() matcher for primitive types, if try to substitute anyDouble() with any() will fail
        when(paymentServiceMock.pay(any(), anyDouble())).thenThrow(BusinessException.class);
            //if want mockito to use matchers with specific values, then use eq()
            when(paymentServiceMock.pay(any(), eq(400.0))).thenThrow(BusinessException.class);
            //anyString() will not match a null string. If there is a possibility of returning null string, use any()

        //when
        Executable executable = () -> bookingService.makeBooking(bookingRequest);

        //then
        assertThrows(BusinessException.class, executable);
    }
}
