//package com.vaccinelife.vaccinelifeapi.config.Resource;
//
//import com.vaccinelife.vaccinelifeapi.controller.CommentController;
//import com.vaccinelife.vaccinelifeapi.dto.CommentRequestDto;
//import com.vaccinelife.vaccinelifeapi.model.Comment;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.Link;
//
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//
//public class CommentRequestDtoResource extends EntityModel<CommentRequestDto> {
//
//    public CommentRequestDtoResource(CommentRequestDto comment, Link... links) {
//        super(comment, links);
//        add(linkTo(CommentController.class).slash(comment.getId()).withSelfRel());
//    }
//
//}
