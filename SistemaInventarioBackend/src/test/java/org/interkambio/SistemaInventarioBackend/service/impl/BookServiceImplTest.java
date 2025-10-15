package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.interkambio.SistemaInventarioBackend.mapper.BookMapper;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Clock fixedClock;
    private Book bookActiveValid;
    private Book bookActiveExpired;
    private Book bookInactive;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Fijamos "hoy" como 2025-10-15
        fixedClock = Clock.fixed(LocalDate.of(2025, 10, 15)
                .atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        // Inicializamos manualmente el service con Clock
        bookService = new BookServiceImpl(
                bookRepository,
                bookMapper,
                null, // bookImporter
                null, // stockLocationRepository
                null, // warehouseRepository
                null, // userRepository
                fixedClock
        );

        // Oferta activa y vigente
        bookActiveValid = new Book();
        bookActiveValid.setId(1L);
        bookActiveValid.setIsOfferActive(true);
        bookActiveValid.setOfferEndDate(LocalDate.of(2025, 10, 17));

        // Oferta activa y expirada
        bookActiveExpired = new Book();
        bookActiveExpired.setId(2L);
        bookActiveExpired.setIsOfferActive(true);
        bookActiveExpired.setOfferEndDate(LocalDate.of(2025, 10, 14));

        // Libro sin oferta activa
        bookInactive = new Book();
        bookInactive.setId(3L);
        bookInactive.setIsOfferActive(false);
    }

    @Test
    void testCheckAndDeactivateIfExpired_works() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(bookActiveValid, bookActiveExpired, bookInactive));

        bookService.deactivateExpiredOffers();

        assertTrue(bookActiveValid.getIsOfferActive(), "Libro vigente no debe desactivarse");
        assertFalse(bookActiveExpired.getIsOfferActive(), "Libro expirada debe desactivarse");
        assertFalse(bookInactive.getIsOfferActive(), "Libro sin oferta permanece inactivo");

        verify(bookRepository, times(1)).save(bookActiveExpired);
        verify(bookRepository, never()).save(bookActiveValid);
        verify(bookRepository, never()).save(bookInactive);
    }

    @Test
    void testDeactivateExpiredOffers_WhenNoBooks_ShouldNotFail() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> bookService.deactivateExpiredOffers());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testDeactivateExpiredOffers_WhenNullEndDate_ShouldIgnoreBook() {
        Book bookWithoutEnd = new Book();
        bookWithoutEnd.setId(4L);
        bookWithoutEnd.setIsOfferActive(true);
        bookWithoutEnd.setOfferEndDate(null);

        when(bookRepository.findAll()).thenReturn(Collections.singletonList(bookWithoutEnd));

        bookService.deactivateExpiredOffers();

        assertTrue(bookWithoutEnd.getIsOfferActive());
        verify(bookRepository, never()).save(bookWithoutEnd);
    }

    @Test
    void testFindById_DeactivatesExpiredOffer() {
        when(bookRepository.findById(2L)).thenReturn(Optional.of(bookActiveExpired));
        when(bookMapper.toDTO(bookActiveExpired)).thenReturn(new BookDTO());

        bookService.findById(2L);

        assertFalse(bookActiveExpired.getIsOfferActive());
        verify(bookRepository, times(1)).save(bookActiveExpired);
    }

    @Test
    void testFindBySku_DeactivatesExpiredOffer() {
        when(bookRepository.findBySku("EXP123")).thenReturn(Optional.of(bookActiveExpired));
        when(bookMapper.toDTO(bookActiveExpired)).thenReturn(new BookDTO());

        bookService.findBySku("EXP123");

        assertFalse(bookActiveExpired.getIsOfferActive());
        verify(bookRepository, times(1)).save(bookActiveExpired);
    }

    @Test
    void testDeactivateExpiredOffers_OfferEndsToday_ShouldRemainActive() {
        Book bookEndsToday = new Book();
        bookEndsToday.setId(5L);
        bookEndsToday.setIsOfferActive(true);
        bookEndsToday.setOfferEndDate(LocalDate.of(2025, 10, 15)); // "hoy" del Clock

        when(bookRepository.findAll()).thenReturn(Collections.singletonList(bookEndsToday));

        bookService.deactivateExpiredOffers();

        assertTrue(bookEndsToday.getIsOfferActive(), "Libro que expira hoy no debe desactivarse");
        verify(bookRepository, never()).save(bookEndsToday);
    }

    @Test
    void testDeactivateExpiredOffers_InactiveExpiredOffer_ShouldNotChange() {
        Book inactiveExpired = new Book();
        inactiveExpired.setId(6L);
        inactiveExpired.setIsOfferActive(false);
        inactiveExpired.setOfferEndDate(LocalDate.of(2025, 10, 14));

        when(bookRepository.findAll()).thenReturn(Collections.singletonList(inactiveExpired));

        bookService.deactivateExpiredOffers();

        assertFalse(inactiveExpired.getIsOfferActive());
        verify(bookRepository, never()).save(inactiveExpired);
    }

    @Test
    void testDeactivateExpiredOffersJob_CallsRepositoryWithToday() {
        BookRepository repoMock = mock(BookRepository.class);
        BookServiceImpl serviceWithRepoMock = new BookServiceImpl(
                repoMock, bookMapper, null, null, null, null, fixedClock
        );

        when(repoMock.deactiveExpiredOffers(LocalDate.of(2025, 10, 15))).thenReturn(2);

        serviceWithRepoMock.deactivateExpiredOffersJob();

        verify(repoMock, times(1)).deactiveExpiredOffers(LocalDate.of(2025, 10, 15));
    }



}
