package ec.edu.espe.banquito.requirements.service;

import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueRS;
import ec.edu.espe.banquito.requirements.controller.DTO.LoanRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.LoanRS;
import ec.edu.espe.banquito.requirements.model.InterestAccrue;
import ec.edu.espe.banquito.requirements.model.Loan;
import ec.edu.espe.banquito.requirements.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class LoanService {
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<LoanRS> getAllLoan() {
        List<Loan> loans = this.loanRepository.findAll();
        List<LoanRS> loansList = new ArrayList<>();
        for (Loan loan : loans) {
            loansList.add(this.transformLoan(loan));
        }
        return loansList;
    }

    public LoanRS getLoanByCustomerId(Integer customerId){
        Loan optionalLoan = loanRepository.findByCustomerId(customerId);
        return this.transformLoan(optionalLoan);
    }

    public LoanRS getLoanByGroupCompanyId(Integer groupCompanyId){
        Loan optionalLoan = loanRepository.findByGroupCompanyId(groupCompanyId);
        return this.transformLoan(optionalLoan);
    }

    @Transactional
    public LoanRS createLoan(LoanRQ loanRQ) {
        try {
            Loan loanRequest = this.transformLoanRQ(loanRQ);
            if (loanRequest.getCustomerId() != null && loanRequest.getGroupCompanyId() == null){
                Loan optionalLoan = this.loanRepository.findByCustomerIdAndLoanProductId(loanRequest.getCustomerId(), loanRequest.getLoanProductId());
                if(optionalLoan == null){
                    loanRequest.setUniqueKey(UUID.randomUUID().toString());
                    loanRequest.setStatus("APR");
                    return this.transformLoan(this.loanRepository.save(loanRequest));
                }else {
                    throw new RuntimeException("Error al crear el préstamo");
                }
            } else if (loanRequest.getGroupCompanyId() != null && loanRequest.getCustomerId() == null){
                Loan optionalLoan = this.loanRepository.findByGroupCompanyIdAndLoanProductId(loanRequest.getGroupCompanyId(), loanRequest.getLoanProductId());
                if(optionalLoan == null){
                    loanRequest.setUniqueKey(UUID.randomUUID().toString());
                    loanRequest.setStatus("APR");
                    return this.transformLoan(this.loanRepository.save(loanRequest));
                }else {
                    throw new RuntimeException("Error al crear el préstamo");
                }
            }else{
                throw new RuntimeException();
            }
        }catch (RuntimeException re){
            throw re;
        }
    }

    @Transactional
    public LoanRS updateLoan(LoanRQ loanRQ){
        try{
            Loan loanRequest = this.transformLoanRQ(loanRQ);
            if (loanRequest.getCustomerId() != null && loanRequest.getGroupCompanyId() == null) {
                Loan optionalLoan = this.loanRepository.findByCustomerIdAndLoanProductId(loanRequest.getCustomerId(), loanRequest.getLoanProductId());
                if(optionalLoan != null){
                    optionalLoan.setName(loanRequest.getName());
                    optionalLoan.setAmount(loanRequest.getAmount());
                    optionalLoan.setTerm(loanRequest.getTerm());
                    optionalLoan.setGracePeriod(loanRequest.getGracePeriod());
                    optionalLoan.setGracePeriodType(loanRequest.getGracePeriodType());
                    optionalLoan.setMonthlyFee(loanRequest.getMonthlyFee());
                    optionalLoan.setDaysLate(loanRequest.getDaysLate());
                    optionalLoan.setInterestRate(loanRequest.getInterestRate());
                    optionalLoan.setRedraw(loanRequest.getRedraw());
                    optionalLoan.setRedrawBalance(loanRequest.getRedrawBalance());
                    return this.transformLoan(this.loanRepository.save(optionalLoan));
                }else{
                    throw new RuntimeException("Error al editar el préstamo");
                }
            } else if (loanRequest.getGroupCompanyId() != null && loanRequest.getCustomerId() == null){
                Loan optionalLoan = this.loanRepository.findByGroupCompanyIdAndLoanProductId(loanRequest.getGroupCompanyId(), loanRequest.getLoanProductId());
                if(optionalLoan != null){
                    optionalLoan.setName(loanRequest.getName());
                    optionalLoan.setAmount(loanRequest.getAmount());
                    optionalLoan.setTerm(loanRequest.getTerm());
                    optionalLoan.setGracePeriod(loanRequest.getGracePeriod());
                    optionalLoan.setGracePeriodType(loanRequest.getGracePeriodType());
                    optionalLoan.setMonthlyFee(loanRequest.getMonthlyFee());
                    optionalLoan.setDaysLate(loanRequest.getDaysLate());
                    optionalLoan.setInterestRate(loanRequest.getInterestRate());
                    optionalLoan.setRedraw(loanRequest.getRedraw());
                    optionalLoan.setRedrawBalance(loanRequest.getRedrawBalance());
                    return this.transformLoan(this.loanRepository.save(optionalLoan));
                }else {
                    throw new RuntimeException("Error al editar el préstamo");
                }
            }else{
                throw new RuntimeException();
            }
        }catch(RuntimeException re){
            throw re;
        }
    }

    @Transactional
    public LoanRS statusLoan(LoanRQ loanRQ){
        try{
            Loan loanRequest = this.transformLoanRQ(loanRQ);
            if (loanRequest.getCustomerId() != null && loanRequest.getGroupCompanyId() == null) {
                Loan optionalLoan = this.loanRepository.findByCustomerIdAndLoanProductId(loanRequest.getCustomerId(), loanRequest.getLoanProductId());
                if(optionalLoan != null){
                    optionalLoan.setStatus(loanRequest.getStatus());
                    return this.transformLoan(this.loanRepository.save(optionalLoan));
                }else{
                    throw new RuntimeException("Error al editar el estado del préstamo");
                }
            } else if (loanRequest.getGroupCompanyId() != null && loanRequest.getCustomerId() == null){
                Loan optionalLoan = this.loanRepository.findByGroupCompanyIdAndLoanProductId(loanRequest.getGroupCompanyId(), loanRequest.getLoanProductId());
                if(optionalLoan != null){
                    optionalLoan.setStatus(loanRequest.getStatus());
                    return this.transformLoan(this.loanRepository.save(optionalLoan));
                }else {
                    throw new RuntimeException("Error al editar el estado del préstamo");
                }
            }else{
                throw new RuntimeException();
            }
        }catch(RuntimeException re){
            throw re;
        }
    }

    private LoanRS transformLoan(Loan loan) {
        LoanRS loanRS = LoanRS
                .builder()
                .groupCompanyId(loan.getGroupCompanyId())
                .customerId(loan.getCustomerId())
                .interestAccrueId(loan.getInterestAccrueId())
                .guarantorId(loan.getGuarantorId())
                .branchId(loan.getBranchId())
                .loanProductId(loan.getLoanProductId())
                .assetId(loan.getAssetId())
                .uniqueKey(loan.getUniqueKey())
                .loanHolderType(loan.getLoanHolderType())
                .loanHolderCode(loan.getLoanHolderCode())
                .name(loan.getName())
                .amount(loan.getAmount())
                .term(loan.getTerm())
                .gracePeriod(loan.getGracePeriod())
                .gracePeriodType(loan.getGracePeriodType())
                .status(loan.getStatus())
                .approvalDate(loan.getApprovalDate())
                .dueDate(loan.getDueDate())
                .monthlyFee(loan.getMonthlyFee())
                .daysLate(loan.getDaysLate())
                .interestRate(loan.getInterestRate())
                .redraw(loan.getRedraw())
                .redrawBalance(loan.getRedrawBalance())
                .build();
        return loanRS;
    }

    private Loan transformLoanRQ(LoanRQ rq) {
        Loan loan = Loan
                .builder()
                .groupCompanyId(rq.getGroupCompanyId())
                .customerId(rq.getCustomerId())
                .interestAccrueId(rq.getInterestAccrueId())
                .guarantorId(rq.getGuarantorId())
                .branchId(rq.getBranchId())
                .loanProductId(rq.getLoanProductId())
                .assetId(rq.getAssetId())
                .loanHolderType(rq.getLoanHolderType())
                .loanHolderCode(rq.getLoanHolderCode())
                .name(rq.getName())
                .amount(rq.getAmount())
                .term(rq.getTerm())
                .gracePeriod(rq.getGracePeriod())
                .gracePeriodType(rq.getGracePeriodType())
                .approvalDate(rq.getApprovalDate())
                .dueDate(rq.getDueDate())
                .monthlyFee(rq.getMonthlyFee())
                .daysLate(rq.getDaysLate())
                .interestRate(rq.getInterestRate())
                .redraw(rq.getRedraw())
                .redrawBalance(rq.getRedrawBalance())
                .build();
        return loan;
    }
    
}
