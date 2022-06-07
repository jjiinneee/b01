package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //댓글의 숫자와 이미지 처리
    PageResponseDTO<BoardListWithImageDTO> listWithImageDTO(PageRequestDTO pageRequestDTO);


    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();

        if(boardDTO.getFileList() != null && boardDTO.getFileList().size() > 0) {
            boardDTO.getFileList().forEach(imgLink -> board.addImage(imgLink));
        }

        return board;
    }

    default BoardDTO entityToDto(Board board) {

        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title((board.getTitle()))
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        if(board.getImgSet() != null && board.getImgSet().size() > 0) {
            dto.setFileList(
                    board.getImgSet().stream().map(boardImage -> boardImage.getFileLink())
                            .collect(Collectors.toList()));
        }

        return dto;
    }

}
