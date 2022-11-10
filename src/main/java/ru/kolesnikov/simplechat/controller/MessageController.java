package ru.kolesnikov.simplechat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolesnikov.simplechat.service.MessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MessageController {

    private final MessageService messageService;






}
