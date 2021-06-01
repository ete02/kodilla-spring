package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.validator.TrelloValidator;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrelloFacadeTest {
    @InjectMocks
    private TrelloFacade trelloFacade;
    @Mock
    private TrelloService trelloService;
    @Mock
    private TrelloMapper trelloMapper;
    @Mock
    private TrelloValidator trelloValidator;

    @Test
    void shouldFetchEmptyList() {
        //Given
        List<TrelloListDto> trelloLists = List.of(
                new TrelloListDto("1", "test_list", false));
        List<TrelloBoardDto> trelloBoards = List.of(
                new TrelloBoardDto("1", "test", trelloLists));
        List<TrelloList> mappedTrelloList = List.of(
                new TrelloList("1", "test_list", false));
        List<TrelloBoard> mappedTrelloBoards = List.of(
                new TrelloBoard("1", "test", mappedTrelloList));
        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        when(trelloMapper.mapToBoards(trelloBoards)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(trelloBoards);
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(mappedTrelloBoards);
        //When
        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();
        //Then
        assertThat(trelloBoardDtos).isNotNull();
        assertThat(trelloBoardDtos.size()).isEqualTo(1);

        trelloBoardDtos.forEach(trelloBoardDto -> {
            assertThat(trelloBoardDto.getId()).isEqualTo("1");
            assertThat(trelloBoardDto.getName()).isEqualTo("test");

            trelloBoardDto.getLists().forEach(
                    trelloListDto -> {
                        assertThat(trelloListDto.getId()).isEqualTo("1");
                        assertThat(trelloListDto.getName()).isEqualTo("test_list");
                        assertThat(trelloListDto.isClosed()).isFalse();
                    }
            );
        });

    }
    @Test
    public void shouldCreateCard() {
        //GIVEN
        TrelloCard trelloCard = new TrelloCard("Name", "Description", "Pos", "ListId");
        TrelloCardDto trelloCardDto = new TrelloCardDto("Name", "Description", "Pos", "ListId");
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("1", "Name", "http://test.com/cards/");

        given(trelloService.createTrelloCard(trelloCardDto)).willReturn(createdTrelloCardDto);
        given(trelloMapper.mapToCard(trelloCardDto)).willReturn(trelloCard);
        given(trelloMapper.mapToCardDto(trelloCard)).willReturn(trelloCardDto);
        //WHEN
        CreatedTrelloCardDto card = trelloFacade.createCard(trelloCardDto);
        //THEN
        assertEquals("1", card.getId());
        assertEquals("Name", card.getName());
        assertEquals("http://test.com/cards/", card.getShortUrl());
    }
}
