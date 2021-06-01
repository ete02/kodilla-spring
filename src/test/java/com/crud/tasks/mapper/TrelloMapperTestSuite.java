package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrelloMapperTestSuite {
    @InjectMocks
    private TrelloMapper trelloMapper;

    @Test
    public void testMapToBoard() {
        //Given
        List<TrelloListDto> trelloListDto = new ArrayList<>();
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("1", "name", trelloListDto);
        //When
        TrelloBoard mappedBoard = trelloMapper.mapToBoard(trelloBoardDto);
        //Then
        assertEquals("name", mappedBoard.getName());
    }

    @Test
    public void testMapToBoards() {
        //Given
        List<TrelloListDto> trelloListDto = new ArrayList<>();
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("1", "name", trelloListDto);
        List<TrelloBoardDto> trelloBoardDtoList = new ArrayList<>();
        trelloBoardDtoList.add(trelloBoardDto);
        //When
        List<TrelloBoard> mappedTrelloBoardDto = trelloMapper.mapToBoards(trelloBoardDtoList);
        //Then
        assertEquals(1, mappedTrelloBoardDto.size());
    }

    @Test
    public void testMapToList() {
        //Given
        TrelloListDto trelloListDto = new TrelloListDto("1", "name", true);
        List<TrelloListDto> trelloListDtos = new ArrayList<>();
        trelloListDtos.add(trelloListDto);
        //When
        List<TrelloList> mappedList = trelloMapper.mapToList(trelloListDtos);
        //Then
        assertEquals(1, mappedList.size());
        assertEquals("name", mappedList.get(0).getName());
    }

    @Test
    public void testMapToBoardsDto() {
        //Given
        List<TrelloList> trelloList = new ArrayList<>();
        TrelloBoard trelloBoard = new TrelloBoard("id", "name", trelloList);
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(trelloBoard);
        //When
        List<TrelloBoardDto> mappedTrelloBoards = trelloMapper.mapToBoardsDto(trelloBoards);
        //Then
        assertEquals("name", mappedTrelloBoards.get(0).getName());
        assertEquals(1, mappedTrelloBoards.size());
    }

    @Test
    public void testMapToListDto() {
        //Given
        List<TrelloList> trelloLists = new ArrayList<>();
        TrelloList trelloList = new TrelloList("1", "name", false);
        trelloLists.add(trelloList);
        //When
        List<TrelloListDto> mappedListDto = trelloMapper.mapToListDto(trelloLists);
        //Then
        assertEquals("name", mappedListDto.get(0).getName());
    }

    @Test
    public void testMapToCartDto() {
        //Given
        TrelloCard trelloCard = new TrelloCard("name", "test", "first", "123");
        //When
        TrelloCardDto mappedToCartDto = trelloMapper.mapToCardDto(trelloCard);
        //Then
        assertEquals("test", mappedToCartDto.getDescription());
    }

    @Test
    public void testMapToCart() {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("name", "test", "first", "123");
        //When
        TrelloCard mappedToCart = trelloMapper.mapToCard(trelloCardDto);
        //Then
        assertEquals("test", mappedToCart.getDescription());
    }
}
