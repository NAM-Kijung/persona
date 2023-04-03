package com.ssafy.project.api.controller;

import com.ssafy.project.api.service.BoardService;
import com.ssafy.project.common.db.dto.request.BoardAddReqDTO;
import com.ssafy.project.common.db.dto.request.BoardModifyReqDTO;
import com.ssafy.project.common.db.dto.response.BoardAllResDTO;
import com.ssafy.project.common.constant.Msg;
import com.ssafy.project.common.db.dto.common.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@Api(tags = {"게시판 API"})
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/all")
    @ApiOperation(value="전체 게시물 조회")
    public ResponseEntity<ResponseDTO> boardAllList(@RequestParam int page, String sort, String keyword){
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_READ, boardService.findAllBoard(page, sort, keyword)));
    }

    @GetMapping("/top")
    @ApiOperation(value = "인기게시글 조회")
    public ResponseEntity<ResponseDTO> boardTopList(){
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_READ, boardService.findTopBoard()));
    }

    @GetMapping("my")
    @ApiOperation(value = "내 게시물 조회")
    public ResponseEntity<ResponseDTO> boardMyList(@RequestParam int page){
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_READ, boardService.findMyBoard(page)));
    }

    @GetMapping("/detail")
    @ApiOperation(value="게시글 상세 조회")
    public ResponseEntity<ResponseDTO> boardDetail(@RequestParam Long boardId){
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_READ, boardService.detailBoard(boardId)));
    }

    @PostMapping
    @ApiOperation(value = "게시글 등록")
    public ResponseEntity<ResponseDTO> boardAdd(@RequestBody BoardAddReqDTO boardAddReqDTO){
        boardService.addBoard(boardAddReqDTO);
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_CREATE));
    }

    @DeleteMapping
    @ApiOperation(value = "게시글 삭제")
    public ResponseEntity<ResponseDTO> boardRemove(@RequestParam Long boardId) {
        boardService.removeBoard(boardId);
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_DELETE));
    }

    @PutMapping
    @ApiOperation(value = "게시글 수정")
    public ResponseEntity<ResponseDTO> boardModify(@RequestBody BoardModifyReqDTO boardModifyReqDTO){
            boardService.modifyBoard(boardModifyReqDTO);
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_UPDATE));
    }
}
