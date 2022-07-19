package com.vaccinelife.vaccinelifeapi.config.Resource;

import com.vaccinelife.vaccinelifeapi.controller.CommentController;
import com.vaccinelife.vaccinelifeapi.model.Comment;
import com.vaccinelife.vaccinelifeapi.model.QuarComment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class QuarCommentResource extends EntityModel<QuarComment> {

    public QuarCommentResource(QuarComment comment, Link... links) {
        super(comment, links);
        add(linkTo(CommentController.class).slash(comment.getId()).withSelfRel());
    }

}
