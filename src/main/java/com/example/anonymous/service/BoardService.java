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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BoardService {

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
        if(subject.length() > 500){
            throw new InvalidInputException("제목이 너무 깁니다.");
        }
        if (StringUtils.isEmpty(contents)) {
            throw new InvalidInputException("세부사항을 입력해주세요.");
        }
        if(subject.length() > 2000){
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
        boardInfo.setLikeCnt(0);
        boardInfo.setViewCnt(0);
        boardInfo.setBoardStatus(0);

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

    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }
}
