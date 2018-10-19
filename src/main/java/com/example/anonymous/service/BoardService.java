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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BoardService {

    private static final int CREATED_BOARD = 0;
    private static final int UPDATED_BOARD = 1;
    private static final int DELETED_BOARD = 2;

    private static final int INITIAL_NUM = 0;

    private static final int SUBJECT_MAX_LENGTH = 500;
    private static final int CONTENT_MAX_LENGTH = 2000;

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    public void registerBoardService(Board boardInfo, HttpSession session){

        String memberEmail = boardInfo.getMemberEmail();
        String subject = boardInfo.getBoardSubject();
        String contents = boardInfo.getBoardContents();
        String fileName = boardInfo.getImgFile().getOriginalFilename();
        String memberNick = null;

        Member member = null;

        if(StringUtils.isEmpty(memberEmail)){
            throw new NoAuthException("게시글 작성할 권한이 없습니다.");
        }
        if (StringUtils.isEmpty(subject)) {
            throw new InvalidInputException("제목을 적어주세요.");
        }
        if(subject.length() > SUBJECT_MAX_LENGTH){
            throw new InvalidInputException("제목이 너무 깁니다.");
        }
        if (StringUtils.isEmpty(contents)) {
            throw new InvalidInputException("세부사항을 입력해주세요.");
        }
        if(subject.length() > CONTENT_MAX_LENGTH){
            throw new InvalidInputException("글이 너무 깁니다.");
        }
        if(!isPhotoFile(fileName)){
            throw new InvalidInputException("사진만 입력해주세요.");
        }
        member = memberRepository.findMemberByMemberEmail(boardInfo.getMemberEmail());

        if(member == null){
            throw new NoAuthException("게시글 작성할 권한이 없습니다.");
        }
        memberNick = member.getMemberNick();

        boardInfo.setMemberNick(memberNick);
        boardInfo.setLikeCnt(INITIAL_NUM);
        boardInfo.setViewCnt(INITIAL_NUM);
        boardInfo.setBoardStatus(CREATED_BOARD);

        String filePath = ImgUtil.imgUpload("images", session, boardInfo.getImgFile(), boardInfo.getFilePath());
        boardInfo.setFilePath(filePath);

        try{
            boardRepository.save(boardInfo);
        }catch (DataAccessException e){
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

    public Board getBoardById(Long boardId){

        Board board = boardRepository.findBoardByBoardId(boardId);
        Member member = memberRepository.findMemberByMemberNick(board.getMemberNick());
        LOGGER.info(member.toString());
        board.setMemberEmail(member.getMemberEmail());

        return board;
    }

    public List<Board> getBoardList(int page, long boardId){
        Pageable request = new PageRequest(0,3, Sort.Direction.DESC,"boardRegDate");
        List<Board> responseList = null;

        if(boardId == -1){
            responseList = boardRepository.findAllByBoardStatusLessThan(DELETED_BOARD,request);
        }else{
            responseList = boardRepository.findAllByBoardStatusLessThanAndBoardIdLessThan(DELETED_BOARD,boardId,request);
        }

        return responseList;
    }

    public void updateBoardById(Long boardId, Board inputBoard, HttpSession session){
        String sessionEmail = inputBoard.getMemberEmail();
        String subject = inputBoard.getBoardSubject();
        String contents = inputBoard.getBoardContents();
        String fileName = inputBoard.getImgFile().getOriginalFilename();

        Member member = null;

        if(StringUtils.isEmpty(sessionEmail)){
            throw new NoAuthException("게시글 작성할 권한이 없습니다.");
        }
        if (StringUtils.isEmpty(subject)) {
            throw new InvalidInputException("제목을 적어주세요.");
        }
        if(subject.length() > SUBJECT_MAX_LENGTH){
            throw new InvalidInputException("제목이 너무 깁니다.");
        }
        if (StringUtils.isEmpty(contents)) {
            throw new InvalidInputException("세부사항을 입력해주세요.");
        }
        if(subject.length() > CONTENT_MAX_LENGTH){
            throw new InvalidInputException("글이 너무 깁니다.");
        }
        if(!isPhotoFile(fileName)){
            throw new InvalidInputException("사진만 입력해주세요.");
        }
        member = memberRepository.findMemberByMemberEmail(inputBoard.getMemberEmail());

        if(member == null){
            throw new NoAuthException("게시글 작성할 권한이 없습니다.");
        }

        //수정하기전 게시판 정보
        Board updatedBoard = boardRepository.findBoardByBoardId(boardId);
        // 0: 생성됨 1: 업데이트 2:삭제
        updatedBoard.setBoardStatus(UPDATED_BOARD);
        updatedBoard.setBoardSubject(subject);
        updatedBoard.setBoardContents(contents);

        String filePath = null;

        if(!StringUtils.isEmpty(fileName)){
            filePath = ImgUtil.imgUpload("images", session, inputBoard.getImgFile(), fileName);
            updatedBoard.setFilePath(filePath);
        }
        try{
            boardRepository.save(updatedBoard);
        }catch (DataAccessException e){
            deletePhoto(fileName);
            throw new ServerException("게시글 작성중 문제가 발생했습니다.");
        }
    }

    public void deleteBoardById(Long boardId){
        try {
            Board board = boardRepository.findBoardByBoardId(boardId);
            board.setBoardStatus(DELETED_BOARD);
            boardRepository.save(board);
        }catch (DataAccessException e){
            throw new ServerException("게시 글 삭제 중 문제가 발생");
        }
    }
}
