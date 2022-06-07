package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testModify() {

        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated....101")
                .content("Updated content 101...")
                .build();

        boardService.modify(boardDTO);

    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

    }

    @Test
    public void testRegisterWithImage() {

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        List<String> files = IntStream.rangeClosed(1,3)
                .mapToObj(i-> UUID.randomUUID().toString()+"_"+i+".jpg")
                .collect(Collectors.toList());

        boardDTO.setFileList(files);

        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testReadWithImage() {

        BoardDTO boardDTO = boardService.readOne(100L);

        log.info(boardDTO);

        log.info(boardDTO.getFileList());

    }


    @Test
    public void testModifyWithImage() {

        //100번 게시물을 수정
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(100L)
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        List<String> files = IntStream.rangeClosed(1,3)
                .mapToObj(i-> UUID.randomUUID().toString()+"_"+i+".jpg")
                .collect(Collectors.toList());

        boardDTO.setFileList(files);

        boardService.modify(boardDTO);

    }


}
