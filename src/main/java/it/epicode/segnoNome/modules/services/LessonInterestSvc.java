package it.epicode.segnoNome.modules.services;

import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.auth.enums.Role;
import it.epicode.segnoNome.auth.repositories.AppUserRepository;
import it.epicode.segnoNome.modules.dto.LessonInterestRequest;
import it.epicode.segnoNome.modules.entities.LessonInterest;
import it.epicode.segnoNome.modules.enums.LessonType;
import it.epicode.segnoNome.modules.exceptions.InternalServerErrorException;
import it.epicode.segnoNome.modules.exceptions.UnauthorizedException;
import it.epicode.segnoNome.modules.repositories.LessonInterestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonInterestSvc {

    @Autowired
    private LessonInterestRepository lessonInterestRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserRoleSvc userRoleSvc;

    /**
     * Restituisce tutte le richieste di interesse per le lezioni.
     * SOLO utenti con ruolo CREATOR possono accedere a tutte le richieste.
     */
    public List<LessonInterest> getAllRequests(String username) {
        userRoleSvc.allowedToCreator(username);
        return lessonInterestRepository.findAll();
    }

    /**
     * Restituisce solo le richieste GESTITE.
     */
    public List<LessonInterest> getHandledRequests(String username) {
        userRoleSvc.allowedToCreator(username);
        return lessonInterestRepository.findByHandledTrue();
    }

    /**
     * Restituisce solo le richieste DA GESTIRE.
     */
    public List<LessonInterest> getPendingRequests(String username) {
        userRoleSvc.allowedToCreator(username);
        return lessonInterestRepository.findByHandledFalse();
    }

    /**
     * Aggiorna lo stato della richiesta e la marca come "gestita".
     */
    @Transactional
    public LessonInterest updateInterestStatus(Long id, boolean contacted, boolean interested, boolean toBeRecontacted, String note, boolean handled, String username) {
        AppUser user = userRoleSvc.allowedToCreator(username);

        LessonInterest lessonInterest = lessonInterestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        lessonInterest.setContacted(contacted);
        lessonInterest.setInterested(interested);
        lessonInterest.setToBeRecontacted(toBeRecontacted);
        lessonInterest.setHandled(handled);

        if (note != null) {
            lessonInterest.setNote(note);
        }

        return lessonInterestRepository.save(lessonInterest);
    }


    /**
     * Creazione di una richiesta di interesse.
     * SOLO utenti con ruolo USER possono creare una richiesta.
     */
    @Transactional
    public LessonInterest createInterestRequest(@Valid LessonInterestRequest lessonInterestRequest, String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Controllo che solo un USER possa creare una richiesta
        if (!user.getRoles().contains(Role.ROLE_USER)) {
            throw new UnauthorizedException("Solo gli utenti con ruolo USER possono creare una richiesta di interesse.");
        }

        LessonInterest lessonInterest = new LessonInterest();
        BeanUtils.copyProperties(lessonInterestRequest, lessonInterest);
        lessonInterest.setUser(user); // Associa la richiesta all'utente

        // Controllo per la città se la lezione è IN_PERSON
        if (lessonInterestRequest.getLessonType() == LessonType.IN_PERSON) {
            if (lessonInterestRequest.getCity() == null || lessonInterestRequest.getCity().isBlank()) {
                throw new IllegalArgumentException("La città è obbligatoria per le lezioni in presenza!");
            }
            lessonInterest.setCity(lessonInterestRequest.getCity());
        }

        return lessonInterestRepository.save(lessonInterest);
    }

    /**



    /**
     * Elimina una richiesta di interesse.
     * SOLO utenti con ruolo CREATOR possono eliminare una richiesta.
     */
    @Transactional
    public void deleteInterestRequest(Long id, String username) {
        // Controllo che solo un CREATOR possa eliminare richieste
        AppUser user = userRoleSvc.allowedToCreator(username);

        LessonInterest lessonInterest = lessonInterestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        lessonInterestRepository.delete(lessonInterest);
    }
}
