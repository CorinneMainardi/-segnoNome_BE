package it.epicode.segnoNome.modules.controllers;

import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.modules.dto.LessonInterestRequest;
import it.epicode.segnoNome.modules.entities.LessonInterest;
import it.epicode.segnoNome.modules.services.LessonInterestSvc;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson-interest")
public class LessonInterestController {

    @Autowired
    private LessonInterestSvc lessonInterestSvc;

    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    @GetMapping("/all")
    public List<LessonInterest> getAllRequests(@AuthenticationPrincipal AppUser user) {
        return lessonInterestSvc.getAllRequests(user.getUsername());
    }

    /**
     * 📌 Gli utenti con ruolo CREATOR o ADMIN possono vedere le richieste GESTITE.
     */
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    @GetMapping("/handled")
    public List<LessonInterest> getHandledRequests(@AuthenticationPrincipal AppUser user) {
        return lessonInterestSvc.getHandledRequests(user.getUsername());
    }

    /**
     * 📌 Gli utenti con ruolo CREATOR o ADMIN possono vedere le richieste ancora DA GESTIRE.
     */
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    @GetMapping("/pending")
    public List<LessonInterest> getPendingRequests(@AuthenticationPrincipal AppUser user) {
        return lessonInterestSvc.getPendingRequests(user.getUsername());
    }

    /**
     * 📌 Solo gli utenti con ruolo USER possono creare una richiesta.
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public LessonInterest createInterest(@AuthenticationPrincipal AppUser user,
                                         @Valid @RequestBody LessonInterestRequest request) {
        return lessonInterestSvc.createInterestRequest(request, user.getUsername());
    }

    /**
     * 📌 Solo gli utenti con ruolo CREATOR possono aggiornare lo stato di una richiesta.
     */
    @PreAuthorize("hasRole('CREATOR')")
    @PutMapping("/{id}/update-status")
    public LessonInterest updateStatus(@AuthenticationPrincipal AppUser user,
                                       @PathVariable Long id,
                                       @RequestParam boolean contacted,
                                       @RequestParam boolean interested,
                                       @RequestParam boolean toBeRecontacted,
                                       @RequestParam(required = false) String note,
                                       @RequestParam boolean handled) {
        return lessonInterestSvc.updateInterestStatus(id, contacted, interested, toBeRecontacted, note, handled, user.getUsername());
    }


    /**
     * 📌 Solo gli utenti con ruolo CREATOR possono eliminare una richiesta.
     */
    @PreAuthorize("hasRole('CREATOR')")
    @DeleteMapping("/{id}")
    public void deleteRequest(@AuthenticationPrincipal AppUser user, @PathVariable Long id) {
        lessonInterestSvc.deleteInterestRequest(id, user.getUsername());
    }
}