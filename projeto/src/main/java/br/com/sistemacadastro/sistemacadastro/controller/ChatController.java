package br.com.sistemacadastro.sistemacadastro.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChatController {

    private final OllamaChatModel ollamaChatModel;

    public ChatController(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    @GetMapping("/")
    public String chatForm() {
        return "chat"; // chat.html no templates
    }

    @PostMapping("/chat")
    public String chatSubmit(@RequestParam String prompt, Model model) {
        Prompt mensagem = new Prompt(List.of(new UserMessage(prompt)));
        ChatResponse resposta = ollamaChatModel.call(mensagem);

        model.addAttribute("prompt", prompt);
        model.addAttribute("resposta", resposta.getResult().getOutput().getText());

        return "chat";
    }
}
