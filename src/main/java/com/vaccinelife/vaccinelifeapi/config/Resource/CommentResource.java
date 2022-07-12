package com.vaccinelife.vaccinelifeapi.config.Resource;

import com.vaccinelife.vaccinelifeapi.controller.CommentController;
import com.vaccinelife.vaccinelifeapi.controller.VacBoardController;
import com.vaccinelife.vaccinelifeapi.model.Comment;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class CommentResource extends EntityModel<Comment> {

    public CommentResource(Comment comment, Link... links) {
        super(comment, links);
        add(linkTo(CommentController.class).slash(comment.getId()).withSelfRel());
    }

}
