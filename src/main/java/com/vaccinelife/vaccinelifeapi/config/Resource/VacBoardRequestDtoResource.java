package com.vaccinelife.vaccinelifeapi.config.Resource;

import com.vaccinelife.vaccinelifeapi.controller.VacBoardController;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardRequestDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class VacBoardRequestDtoResource extends EntityModel<VacBoardRequestDto> {
    public VacBoardRequestDtoResource(VacBoardRequestDto vacBoard, Link... links) {
        super(vacBoard, links);
        add(linkTo(VacBoardController.class).slash(vacBoard.getId()).withSelfRel());
    }
}
