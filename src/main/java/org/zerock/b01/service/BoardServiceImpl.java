package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;

//    @Override
//    public Long register(BoardDTO boardDTO) {
//
//        Board board = modelMapper.map(boardDTO, Board.class);
//
//        Long bno = boardRepository.save(board).getBno();
//
//        return bno;
//    }
//
//    @Override
//    public BoardDTO readOne(Long bno) {
//
//        Optional<Board> result = boardRepository.findById(bno);
//
//        Board board = result.orElseThrow();
//
//        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
//
//        return boardDTO;
//    }

    @Override
    public Long register(BoardDTO boardDTO) {

        //기존 코드
        //Board board = modelMapper.map(boardDTO, Board.class);

        Board board = dtoToEntity(boardDTO);

        log.info("----------------------------");
        log.info(board);
        log.info(board.getImgSet());
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {

        //Optional<Board> result = boardRepository.findById(bno);
        Optional<Board> result = boardRepository.getWithImage(bno);//@EntityGraph

        Board board = result.orElseThrow();

        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO = entityToDto(board);


        return boardDTO;
    }


//    @Override
//    public void modify(BoardDTO boardDTO) {
//
//        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
//
//        Board board = result.orElseThrow();
//
//        board.change(boardDTO.getTitle(), boardDTO.getContent());
//
//        boardRepository.save(board);
//
//    }

    @Override
    public void modify(BoardDTO boardDTO) {

        //Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Optional<Board> result = boardRepository.getWithImage(boardDTO.getBno());

        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        //첨부파일 처리
        board.clearImgSet(); //기존 데이터 삭제
        if(boardDTO.getFileList() != null){
            boardDTO.getFileList().forEach(fileStr -> board.addImage(fileStr));
        }

        boardRepository.save(board);

    }


    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);

    }

//    @Override
//    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
//
//        String[] types = pageRequestDTO.getTypes();
//        String keyword = pageRequestDTO.getKeyword();
//        Pageable pageable = pageRequestDTO.getPageable("bno");
//
//        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//
//        return null;
//    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());


        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListWithImageDTO> listWithImageDTO(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListWithImageDTO> result =
                boardRepository.searchWithImage(types, keyword, pageable);

        return PageResponseDTO.<BoardListWithImageDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

}
