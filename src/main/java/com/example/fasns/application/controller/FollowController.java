package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.CreateMemberFollowUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
@Slf4j
public class FollowController {

    private final CreateMemberFollowUseCase createMemberFollowUseCase;

    @PostMapping("/{fromMemberId}/{toMemberId}")
    public void register(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
        log.info("fromId={}, toMemberId={}", fromMemberId, toMemberId);
        createMemberFollowUseCase.execute(fromMemberId, toMemberId);
    }
}
