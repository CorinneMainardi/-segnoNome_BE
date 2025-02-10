package it.epicode.segnoNome.modules.services;

import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.auth.repositories.AppUserRepository;
import it.epicode.segnoNome.modules.dto.PaymentMethodRequest;
import it.epicode.segnoNome.modules.entities.PaymentMethod;
import it.epicode.segnoNome.modules.repositories.PaymentMethodRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentSvc {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Transactional
    public PaymentMethod addPaymentMethod(AppUser user, PaymentMethodRequest paymentMethodRequest) {
        PaymentMethod paymentMethod = new PaymentMethod();

        // ✅ Copia automaticamente tutte le proprietà con lo stesso nome
        BeanUtils.copyProperties(paymentMethodRequest, paymentMethod);

        paymentMethod.setUser(user); // ✅ Assegna l'utente separatamente perché non è nel DTO

        return paymentMethodRepository.save(paymentMethod);
    }

    public List<PaymentMethod> getUserPayments(Long userId) {
        return paymentMethodRepository.findByUserId(userId);
    }
}

