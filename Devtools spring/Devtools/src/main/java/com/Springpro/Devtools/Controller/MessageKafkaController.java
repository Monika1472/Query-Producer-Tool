package com.Springpro.Devtools.Controller;

import com.Springpro.Devtools.Entity.MessageTemplate;
import com.Springpro.Devtools.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/messagetemplates")
public class MessageKafkaController {
    private final MessageService messageService;

    @Autowired
    public MessageKafkaController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/admin/create")
    public MessageTemplate createMessageTemplate(@RequestBody MessageTemplate messageTemplate) {
        return messageService.saveMessageTemplate(messageTemplate);
    }


    @GetMapping("/admin/getall")
    public List<MessageTemplate> getAllMessageTemplatesForAdmin() {
        return messageService.getAllMessageTemplates();
    }

    @GetMapping("/user/getall")
    public List<MessageTemplate> getAllMessageTemplatesForUser() {
        return messageService.getAllMessageTemplates();
    }

    @GetMapping("/admin/get/{id}")
    public MessageTemplate getMessageTemplateByIdForAdmin(@PathVariable Long id) {
        return messageService.getMessageTemplateById(id);
    }

    @GetMapping("/user/get/{id}")
    public MessageTemplate getMessageTemplateByIdForUser(@PathVariable Long id) {
        return messageService.getMessageTemplateById(id);
    }

    @PutMapping("/admin/update/{id}")
    public MessageTemplate updateMessageTemplate(@PathVariable Long id, @RequestBody MessageTemplate updatedTemplate) {
        return messageService.updateMessageTemplate(id, updatedTemplate);
    }

    @DeleteMapping("/admin/delete/{id}")
    public String deleteMessageTemplate(@PathVariable Long id) {
        messageService.deleteMessageTemplate(id);
        return "Message Template with id:" + id + " deleted successfully!";
    }
}
