package com.example.demo.colleagues.api;

import java.util.List;

import com.example.demo.colleagues.Colleague;
import com.example.demo.colleagues.ColleagueService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/colleagues")
public class ColleagueController {

    private final ColleagueService colleagueService;

    public ColleagueController(ColleagueService colleagueService) {
        this.colleagueService = colleagueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ColleagueResponse addColleague(@Valid @RequestBody CreateColleagueRequest request) {
        Colleague colleague = colleagueService.addColleague(request.name().trim());
        return toResponse(colleague);
    }

    @PostMapping("/{id}/cups")
    public ColleagueResponse addCups(@PathVariable long id, @Valid @RequestBody AddCupsRequest request) {
        Colleague colleague = colleagueService.addCups(id, request.cups());
        return toResponse(colleague);
    }

    @GetMapping("/toplist")
    public List<ToplistEntry> toplist() {
        return colleagueService.getToplist().stream()
                .map(colleague -> new ToplistEntry(colleague.getId(), colleague.getName(), colleague.getCups()))
                .toList();
    }

    private static ColleagueResponse toResponse(Colleague colleague) {
        return new ColleagueResponse(colleague.getId(), colleague.getName(), colleague.getCups());
    }
}
