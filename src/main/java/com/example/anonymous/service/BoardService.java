package com.example.anonymous.service;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.exception.ServerException;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.utils.ImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BoardService {

    private static final int CREATED_BOARD = 0;
    private static final int UPDATED_BOARD = 1;
    private static final int DELETED_BOARD = 2;

    private static final int INITIAL_NUM = 0;

    private static final int FIRST_BOARD_SIGN = -1;

    private static final int ONE_PAGE_SIZE = 5;
    private static final int SUBJECT_MAX_LENGTH = 500;
    private static final int CONTENT_MAX_LENGTH = 2000;

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    public void registerBoardService(Board boardInfo, Principal principal) {

        Member authorMember = null;
        try{
            authorMember =  memberRepository.findMemberByMemberEmail(principal.getName());
        }catch (DataAccessException e){
            throw new NoAuthException("게시글 작성할 권한이 없습니다.");
        }

        boardInfo.setMember(authorMember);

        String subject = boardInfo.getBoardSubject();
        String contents = boardInfo.getBoardContents();
        String fileName = boardInfo.getImgFile().getOriginalFilename();

        if (StringUtils.isEmpty(subject)) {
            throw new InvalidInputException("제목을 적어주세요.");
        }
        if (subject.length() > SUBJECT_MAX_LENGTH) {
            throw new InvalidInputException("제목이 너무 깁니다.");
        }
        if (StringUtils.isEmpty(contents)) {
            throw new InvalidInputException("세부사항을 입력해주세요.");
        }
        if (subject.length() > CONTENT_MAX_LENGTH) {
            throw new InvalidInputException("글이 너무 깁니다.");
        }
        if (!isPhotoFile(fileName)) {
            throw new InvalidInputException("사진만 입력해주세요.");
        }

        boardInfo.setBoardModDate(new Date());
        boardInfo.setMemberNick(authorMember.getMemberNick());
        boardInfo.setSessionEmail(authorMember.getMemberEmail());

        String filePath = ImgUtil.imgUpload("images", boardInfo.getImgFile(), boardInfo.getFilePath());
        boardInfo.setFilePath(filePath);

        try {
            boardRepository.save(boardInfo);
        } catch (DataAccessException e) {
            deletePhoto(filePath);
            throw new ServerException("게시글 작성중 문제가 발생했습니다.");
        }
    }

    private void deletePhoto(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                LOGGER.info(filePath + "삭제 성공");
            } else {
                LOGGER.info(filePath + "삭제 성공");
            }
        } else {
            LOGGER.info(filePath + "가 존재하지 않습니다.");
        }
    }

    private boolean isPhotoFile(String str) {
        String allowPattern = ".+\\.(jpg|png|JPG|PNG)$";
        boolean result = false;

        Pattern p = Pattern.compile(allowPattern);
        Matcher m = p.matcher(str);
        result = m.matches();

        if (StringUtils.isEmpty(str))
            result = true;
        return result;
    }

    public Board getBoardById(long boardId) {
        Board board = boardRepository.findBoardByBoardId(boardId);
        LOGGER.info(board.toString());
        return board;
    }

    public List<Board> getBoardList(long scrollSign, String scrollingType) {
        Pageable request = new PageRequest(0, ONE_PAGE_SIZE, Sort.Direction.DESC, "boardRegDate");
        List<Board> responseList = null;

        if (scrollingType.equals("down")) {
            if (scrollSign == FIRST_BOARD_SIGN) {
                responseList = boardRepository.findAllByBoardStatusLessThan(DELETED_BOARD, request);
            } else {
                responseList = boardRepository.findAllByBoardStatusLessThanAndBoardIdLessThan(DELETED_BOARD, scrollSign, request);
            }
        } else if(scrollingType.equals("up")){
            if(scrollSign == FIRST_BOARD_SIGN){
                throw new InvalidInputException("맨 처음 게시판 입니다.");
            }else{
                responseList = boardRepository.findAllByBoardStatusLessThanAndBoardIdGreaterThan(DELETED_BOARD, scrollSign, request);
            }
        }
        return responseList;
    }

    public void updateBoardById(long boardId, Board inputBoard,Principal principal) {
        Board board = boardRepository.findBoardByBoardId(boardId);
        String subject = inputBoard.getBoardSubject();
        String contents = inputBoard.getBoardContents();
        String fileName = inputBoard.getImgFile().getOriginalFilename();

        Member authorMember = board.getMember();

        //작가와 현재 사용자의 이름이 다른 경우
        if (!authorMember.getMemberEmail().equals(principal.getName())) {
            throw new NoAuthException("게시글 수정할 권한이 없습니다.");
        }
        if (StringUtils.isEmpty(subject)) {
            throw new InvalidInputException("제목을 적어주세요.");
        }
        if (subject.length() > SUBJECT_MAX_LENGTH) {
            throw new InvalidInputException("제목이 너무 깁니다.");
        }
        if (StringUtils.isEmpty(contents)) {
            throw new InvalidInputException("세부사항을 입력해주세요.");
        }
        if (contents.length() > CONTENT_MAX_LENGTH) {
            throw new InvalidInputException("글이 너무 깁니다.");
        }
        if (!isPhotoFile(fileName)) {
            throw new InvalidInputException("사진만 입력해주세요.");
        }

        // 0: 생성됨 1: 업데이트 2:삭제
        board.setBoardSubject(inputBoard.getBoardSubject());
        board.setBoardContents(inputBoard.getBoardContents());
        board.setBoardStatus(UPDATED_BOARD);
        board.setBoardModDate(new Date());


        if (!StringUtils.isEmpty(fileName)) {
            String filePath = ImgUtil.imgUpload("images", inputBoard.getImgFile(), fileName);
            board.setFilePath(filePath);
        }
        try {
            boardRepository.save(board);
        } catch (DataAccessException e) {
            deletePhoto(fileName);
            throw new ServerException("게시글 작성중 문제가 발생했습니다.");
        }
    }

    public void deleteBoardById(long boardId, Principal principal) {
        Board deletedBoard = boardRepository.findBoardByBoardId(boardId);
        Member authorMember = deletedBoard.getMember();

        //게시자와 현재 사용자의 이름이 다를 때
        if(!authorMember.getMemberEmail().equals(principal.getName())){
            throw new NoAuthException("게시글 삭제할 권한이 없습니다.");
        }
        try {
            deletedBoard.setBoardStatus(DELETED_BOARD);
            deletedBoard.setBoardModDate(new Date());

            boardRepository.save(deletedBoard);
        } catch (DataAccessException e) {
            throw new ServerException("게시 글 삭제 중 문제가 발생");
        }
    }

    public List<Board> getBoardListByMemberEmail(String memberEmail){
        List<Board> boardListByMemberEmail = boardRepository.findAllByMemberMemberEmail(memberEmail);
        return boardListByMemberEmail;
    }


}
