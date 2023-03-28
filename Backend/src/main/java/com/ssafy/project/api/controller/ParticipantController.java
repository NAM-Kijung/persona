package com.ssafy.project.api.controller;

import com.ssafy.project.api.service.ParticipantService;
import com.ssafy.project.common.db.dto.request.ParticipantAddReqDTO;
import com.ssafy.project.common.db.entity.common.Participant;
import com.ssafy.project.common.util.Msg;
import com.ssafy.project.common.util.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@Api(tags = {"참여자 API"})
@RequiredArgsConstructor
@RequestMapping(value = "/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    //참여자 등록
    @PostMapping("/regist")
    @ApiOperation(value = "참여자 정보 등록")
    public ResponseEntity<ResponseDTO> participantAdd(@RequestBody ParticipantAddReqDTO participantAddReqDTO){
        participantService.addParticipant(participantAddReqDTO);
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_CREATE));
    }
}