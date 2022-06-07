package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.BoardListWithImageDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." +i)
                    .content("content..." + i)
                    .writer("user"+ (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect() {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info(board);

    }

    @Test
    public void testUpdate() {

        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update..title 100", "update content 100");

        boardRepository.save(board);

    }

    @Test
    public void testDelete() {
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {

        //1 page order by bno desc
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);


        log.info("total count: "+result.getTotalElements());
        log.info( "total pages:" +result.getTotalPages());
        log.info("page number: "+result.getNumber());
        log.info("page size: "+result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));


    }

    @Test
    public void testSearch1() {

        //2 page order by bno desc
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());

        boardRepository.search1(pageable);

    }

    @Test
    public void testSearchAll() {

        String[] types = {"t","c","w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable );

    }

    @Test
    public void testSearchAll2() {

        String[] types = {"t","c","w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable );

        //total pages
        log.info(result.getTotalPages());

        //pag size
        log.info(result.getSize());

        //pageNumber
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() +": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }


    @Test
    public void testSearchReplyCount() {

        String[] types = {"t","c","w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable );

        //total pages
        log.info(result.getTotalPages());
        //pag size
        log.info(result.getSize());
        //pageNumber
        log.info(result.getNumber());
        //prev next
        log.info(result.hasPrevious() +": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testInsertWithImage() {

        for (int i = 0; i < 100;i++){

            Board board = Board.builder()
                    .title("Image Test..")
                    .content("content..." )
                    .writer("user11")
                    .build();

            //일부 게시물은 첨부파일이 없는 경우도 있도록
            if(i % 10 != 0) {
                board.addImage(UUID.randomUUID().toString() + "_aaa.jpg");
                board.addImage(UUID.randomUUID().toString() + "_bbb.jpg");
            }
            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());

        }//end for
    }

    @Transactional
    @Test
    public void testReadWithImage() {

        //실제 첨부가 있는 번호를 테스트
        Optional<Board> result = boardRepository.findById(2L);

        Board board = result.orElseThrow();

        log.info(board);

        //이 부분때문에 @Tranasctional이 필요
        log.info(board.getImgSet());

    }


    @Test
    public void testReadWithImage2() {

        //실제 첨부가 있는 번호를 테스트
        Optional<Board> result = boardRepository.getWithImage(2L);

        Board board = result.orElseThrow();

        log.info(board);

        //이 부분때문에 @Tranasctional이 필요
        log.info(board.getImgSet());

    }

    @Transactional
    @Commit
    @Test
    public void testUpdateWithImage() {

        Optional<Board> result = boardRepository.findById(2L);

        Board board = result.orElseThrow();


        board.clearImgSet();

        //aaa.jpg bbb.jpg를 ccc.jpg, ddd.jpg로 교체
        board.addImage(UUID.randomUUID().toString() +"ccc.jpg");
        board.addImage(UUID.randomUUID().toString() +"ddd.jpg");


        boardRepository.save(board);
    }


    @Commit
    @Transactional
    @Test
    public void testDeleteWithImage() {

        boardRepository.deleteById(2L);
    }


//    @Test
//    public void testListWithImageDTO(){
//
//        String[] types = null;
//        String keyword = null;
//
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//
//        boardRepository.searchWithImage(types,keyword, pageable);
//
//    }

    @Test
    public void testListWithImageDTO(){

        String[] types = null;
        String keyword = null;

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListWithImageDTO> result = boardRepository.searchWithImage(types,keyword, pageable);

        result.getContent().forEach(boardListWithImageDTO -> log.info(boardListWithImageDTO));

    }


}
