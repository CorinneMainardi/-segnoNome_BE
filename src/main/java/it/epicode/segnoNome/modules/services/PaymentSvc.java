package it.epicode.segnoNome.modules.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.auth.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentSvc {


    @Autowired
    private AppUserRepository appUserRepository;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    public String createPayment(Double total, String jwtToken) throws PayPalRESTException {
        System.out.println("🔵 Creazione pagamento per utente autenticato...");
        System.out.println("🔵 Token JWT ricevuto: " + jwtToken);

        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(String.format(java.util.Locale.US, "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Pagamento per il corso di segni");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // ✅ Aggiunge il token JWT nell'URL di ritorno
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:4200/payment-failed");
        redirectUrls.setReturnUrl("https://6845-2-39-147-198.ngrok-free.app/api/payments/execute-payment?jwtToken=" + jwtToken);



        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        String approvalUrl = createdPayment.getLinks().stream()
                .filter(link -> link.getRel().equalsIgnoreCase("approval_url"))
                .findFirst()
                .get()
                .getHref();

        System.out.println("✅ Payment created. Approval URL: " + approvalUrl);
        return approvalUrl;
    }


    public boolean executePayment(String paymentId, String payerId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            System.out.println("✅ Payment executed successfully! ID: " + executedPayment.getId());

            // 🔥 Verifica se lo stato del pagamento è "approved"
            if ("approved".equalsIgnoreCase(executedPayment.getState())) {
                System.out.println("✅ Il pagamento è stato approvato!");
                return true;
            } else {
                System.err.println("❌ Il pagamento NON è stato approvato! Stato: " + executedPayment.getState());
                return false;
            }
        } catch (PayPalRESTException e) {
            System.err.println("❌ Errore durante l'esecuzione del pagamento: " + e.getMessage());
            return false;
        }
    }


    public void setUserHasPaid(AppUser user) {
        if (user == null) {
            System.err.println("❌ Errore: utente non autenticato!");
            return;
        }

        // Recupera l'utente dal database
        System.out.println("🔍 Tentativo di recupero utente con ID: " + user.getId());
        AppUser userFromDb = appUserRepository.findById(user.getId()).orElse(null);

        if (userFromDb == null) {
            System.err.println("❌ Errore: utente non trovato nel database con ID: " + user.getId());
            return;
        }

        System.out.println("✅ Utente trovato nel database: " + userFromDb.getUsername());
        System.out.println("🔹 Stato attuale hasPaid: " + userFromDb.isHasPaid());

        // Aggiorna lo stato di pagamento
        userFromDb.setHasPaid(true);

        // Salvataggio nel database
        appUserRepository.save(userFromDb);
        System.out.println("✅ Pagamento registrato per: " + userFromDb.getUsername());
        System.out.println("🔹 Nuovo stato hasPaid: " + userFromDb.isHasPaid());

        // Verifica salvataggio
        AppUser checkUser = appUserRepository.findById(user.getId()).orElse(null);
        if (checkUser != null) {
            System.out.println("🔍 Verifica dopo il salvataggio -> hasPaid: " + checkUser.isHasPaid());
        } else {
            System.err.println("❌ Errore: impossibile verificare il salvataggio dell'utente.");
        }
    }



}