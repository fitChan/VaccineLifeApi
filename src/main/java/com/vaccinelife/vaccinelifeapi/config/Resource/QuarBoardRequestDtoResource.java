package com.vaccinelife.vaccinelifeapi.config.Resource;

import com.vaccinelife.vaccinelifeapi.controller.QuarBoardController;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardRequestDto;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class QuarBoardRequestDtoResource extends EntityModel<QuarBoardRequestDto> {
    public QuarBoardRequestDtoResource(QuarBoardRequestDto quarBoard, Link... links) {
        super(quarBoard, links);
        add(linkTo(QuarBoardController.class).slash(quarBoard.getId()).withSelfRel());
    }
}
