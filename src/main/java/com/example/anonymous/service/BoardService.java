package com.example.anonymous.service;

import com.example.anonymous.DTO.BoardDTO;
import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.LikeTable;
import com.example.anonymous.domain.Member;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.exception.ServerException;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.LikeRepository;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.repository.ReplyRepository;
import com.example.anonymous.restcontroller.BoardController;
import com.example.anonymous.status.BoardStatus;
import com.example.anonymous.status.LikeStatus;
import com.example.anonymous.utils.ImgUtil;
import javafx.beans.InvalidationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BoardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);

    private static final int ONE_PAGE_SIZE = 5;
    private static final int FIRST_BOARD_SIGN = -1;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    LikeRepository likeRepository;

    public void registerBoardService(BoardDTO boardInfo, MultipartFile file, Principal principal) {
        String fileName = file.getOriginalFilename();

        if (!isPhotoFile(fileName)) {
            throw new InvalidInputException("사진만 입력해주세요.");
        }

        String filePath = ImgUtil.imgUpload("images", file, boardInfo.getFilePath());
        Member poster = memberRepository.findMemberByMemberEmail(principal.getName());
        Board newBoard = boardInfo.toEntity(filePath, poster);

        try {
            boardRepository.save(newBoard);
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

        if (StringUtils.isEmpty(str)) {
            result = true;
        }
        return result;
    }

    public List<BoardDTO> getBoardList(long scrollSign, String scrollingType, Principal principal) {
        Pageable request = null;
        List<Board> responseList = null;

        switch (scrollingType) {
            case "down":
                request = PageRequest.of(0, ONE_PAGE_SIZE, Sort.Direction.DESC, "boardRegDate");
                responseList = getBoardListByScrollDown(scrollSign, request);
                break;
            case "up":
                request = PageRequest.of(0, ONE_PAGE_SIZE, Sort.Direction.ASC, "boardRegDate");
                responseList = getBoardListByScrollUp(scrollSign, request);
                break;
            default:
                throw new InvalidInputException("잘못된 스크롤 접근입니다.");
        }

        return setLikeStatusToBoardListByMemberEmail(responseList,principal.getName());
    }

    private List<Board> getBoardListByScrollDown(long scrollSign, Pageable request) {
        if (scrollSign == FIRST_BOARD_SIGN) {
            return boardRepository.findAllByBoardStatusIsNot(BoardStatus.DELETED, request);
        } else {
            return boardRepository.findAllByBoardStatusIsNotAndBoardIdLessThan(BoardStatus.DELETED, scrollSign, request);
        }
    }

    private List<Board> getBoardListByScrollUp(long scrollSign, Pageable request) {
        if (scrollSign == FIRST_BOARD_SIGN) {
            throw new InvalidInputException("게시판이 존재하지 않습니다.");
        } else {
            List<Board> responseList = boardRepository.findAllByBoardStatusIsNotAndBoardIdGreaterThan(BoardStatus.DELETED, scrollSign, request);
            Collections.reverse(responseList);

            return responseList;
        }
    }

    /**
     * 로그인한 사용자를 기준으로 전체 게시물에 대한 좋아요 상태를 세팅하는 함수
     * Set<Board>에 해당 사용자가 좋아하는 게시물들을 넣고
     * 해당 게시물이 set에 포함 되면 like 아니면 unlike로 표시
     *
     * @param responseList
     * @param memberEmail
     * @return
     */
    private List<BoardDTO> setLikeStatusToBoardListByMemberEmail(List<Board> responseList, String memberEmail) {
        Member member = memberRepository.findMemberByMemberEmail(memberEmail);
        List<LikeTable> likeListByMember = member.getLikeTableList();
        List<BoardDTO> boardList = new ArrayList<>();

        Set<Board> boardSet = new HashSet<>();

        for (LikeTable likeTable : likeListByMember) {
            boardSet.add(likeTable.getBoard());
        }

        for (Board board : responseList) {
            BoardDTO boardDTO = new BoardDTO(board);

            if (boardSet.contains(board)) {
                boardDTO.setLikeStatus(LikeStatus.LIKE);
            } else {
                boardDTO.setLikeStatus(LikeStatus.UNLIKE);
            }
            boardList.add(boardDTO);
        }
        return boardList;
    }
    /*
    public Board getBoardById(long boardId) {
        Board board = boardRepository.findBoardByBoardId(boardId);
        LOGGER.info(board.toString());
        return board;
    }



    public void updateBoardById(long boardId, Board inputBoard, Principal principal) {
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
        if (!authorMember.getMemberEmail().equals(principal.getName())) {
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

    public List<Board> getBoardListByMemberEmail(String memberEmail) {
        List<Board> boardListByMemberEmail = boardRepository.findAllByMemberMemberEmail(memberEmail);
        return boardListByMemberEmail;
    }

    @Transactional
    public int processLikeByBoardIdAndMemberEmail(long boardId, String memberEmail) {
        LikeTable likeTable = likeRepository.findByBoardIdAndMemberEmail(boardId, memberEmail);
        Board likeBoard = boardRepository.findBoardByBoardId(boardId);

        int likeStatus = 0;

        if(likeBoard == null){
            throw new InvalidInputException("잘못된(존재하지 않은) 게시글 접근 입니다.");
        }

        if (likeTable == null) {
            likeTable = new LikeTable();
            likeTable.setBoardId(boardId);
            likeTable.setMemberEmail(memberEmail);

            processLike(likeTable, LIKE_ACTIVE_STATUS, likeBoard, 1);
        }
        else if (likeTable.getCheckLike() == LIKE_CANCEL_STATUS) {
            processLike(likeTable, LIKE_ACTIVE_STATUS, likeBoard, 1);
        }
        else if (likeTable.getCheckLike() == LIKE_ACTIVE_STATUS) {
            processLike(likeTable, LIKE_CANCEL_STATUS, likeBoard, -1);
            likeStatus = 1;
        }
        else {
            throw new InvalidInputException("잘못된 접근입니다.");
        }

        boardRepository.save(likeBoard);
        likeRepository.save(likeTable);

        return likeStatus;
    }

    private void processLike(LikeTable likeTable, int likeStatus, Board likeBoard, int variation) {
        likeTable.setCheckLike(likeStatus);
        likeBoard.setLikeCnt(likeBoard.getLikeCnt() + variation);
    }
    */
}
