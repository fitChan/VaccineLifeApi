package com.vaccinelife.vaccinelifeapi.config.Resource;

import com.vaccinelife.vaccinelifeapi.controller.VacBoardController;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardSimRequestDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class VacBoardSimResResource extends EntityModel<VacBoardSimRequestDto> {


    public VacBoardSimResResource (VacBoardSimRequestDto vacBoards, Link... links) {
        super(vacBoards, links);
        add(linkTo(VacBoardController.class).slash(vacBoards.getId()).withSelfRel());
    }


}
