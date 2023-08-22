package ec.edu.espe.banquito.requirements.service;

import ec.edu.espe.banquito.requirements.controller.DTO.LoanTransactionRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.LoanTransactionRS;
import ec.edu.espe.banquito.requirements.model.LoanTransaction;
import ec.edu.espe.banquito.requirements.repository.LoanTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LoanTransactionService {
    private final LoanTransactionRepository loanTransactionRepository;

    public LoanTransactionRS getLoanTransaction(String uuid){
        LoanTransaction optionalLoanTransaction = loanTransactionRepository.findByUniqueKey(uuid);
        return this.transformtoLoanTransactionRS(optionalLoanTransaction);
    }

    public List<LoanTransactionRS> getAll(){
        return this.transactionToListLoanTransactionRS(loanTransactionRepository.findAll());
    }

    @Transactional
    public LoanTransactionRS create(LoanTransactionRQ loanTransactionRQ){
        LoanTransaction newLoanTransaction = this.transformLoanTransactionRQ(loanTransactionRQ);
        LoanTransaction loanTransaction = loanTransactionRepository.findByUniqueKey(newLoanTransaction.getUniqueKey());
        if(loanTransaction != null){
            throw new RuntimeException("Ya existe");
        }else{
            return this.transformtoLoanTransactionRS(loanTransactionRepository.save(newLoanTransaction));
        }
    }

    private List<LoanTransactionRS> transactionToListLoanTransactionRS(List<LoanTransaction> loanTransactions){
        List<LoanTransactionRS> loanTransactionRSList = new ArrayList<>();
        for(LoanTransaction loanTransaction : loanTransactions){
            LoanTransactionRS loanTransactionRS = LoanTransactionRS.builder()
                    .id(loanTransaction.getId())
                    .uniqueKey(loanTransaction.getUniqueKey())
                    .type(loanTransaction.getType())
                    .bookingDate(loanTransaction.getBookingDate())
                    .valueDate(loanTransaction.getValueDate())
                    .status(loanTransaction.getStatus())
                    .amount(loanTransaction.getAmount())
                    .applyTax(loanTransaction.getApplyTax())
                    .parentLoanTrxKey(loanTransaction.getParentLoanTrxKey())
                    .notes(loanTransaction.getNotes())
                    .build();
            loanTransactionRSList.add(loanTransactionRS);
        }
        return loanTransactionRSList;
    }

    //Response
    private LoanTransactionRS transformtoLoanTransactionRS(LoanTransaction loanTransaction){
        return LoanTransactionRS.builder()
                .id(loanTransaction.getId())
                .uniqueKey(loanTransaction.getUniqueKey())
                .type(loanTransaction.getType())
                .bookingDate(loanTransaction.getBookingDate())
                .valueDate(loanTransaction.getValueDate())
                .status(loanTransaction.getStatus())
                .amount(loanTransaction.getAmount())
                .applyTax(loanTransaction.getApplyTax())
                .parentLoanTrxKey(loanTransaction.getParentLoanTrxKey())
                .notes(loanTransaction.getNotes())
                .build();
    }

    //Request REVISAR
    private LoanTransaction transformLoanTransactionRQ(LoanTransactionRQ loanTransactionRQ){
        return LoanTransaction.builder()

                .uniqueKey(UUID.randomUUID().toString())
                .creationDate(new Date())
                .bookingDate(new Date())
                .valueDate(new Date())
                .build();
    }
}